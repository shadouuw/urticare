package com.amine.urticare.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class CustomTokenEnhancer implements OAuth2TokenCustomizer<JwtEncodingContext> {


    @Override
    public void customize(JwtEncodingContext context) {
        // Add client_id and scope claims from RegisteredClient
        if (context.getRegisteredClient() != null) {
            context.getClaims().claim("client_id", context.getRegisteredClient().getClientId());

            Set<String> scopes = context.getAuthorizedScopes();
            context.getClaims().claim("scope", String.join(" ", scopes));
        }

        // Add user details and authorities if authenticated
        if (context.getPrincipal() instanceof Authentication authentication &&
                authentication.getPrincipal() instanceof UserDetails userDetails) {

            // Extract authorities directly from the authenticated user
            Set<String> authorities = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            // Split roles and permissions
            List<String> roles = authorities.stream()
                    .filter(a -> a.startsWith("ROLE_")).toList();
            List<String> permissions = authorities.stream()
                    .filter(a -> !a.startsWith("ROLE_")).toList();

            // Add claims to the token
            context.getClaims().claim("roles", roles);
            context.getClaims().claim("permissions", permissions);
            context.getClaims().claim("authorities", authorities); // optional, for introspection
            context.getClaims().claim("username", userDetails.getUsername());
        }
    }
}
