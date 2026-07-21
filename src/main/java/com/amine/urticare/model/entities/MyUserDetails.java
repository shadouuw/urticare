package com.amine.urticare.model.entities;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Custom implementation of {@link UserDetails} that adapts the application's {@link User}
 * entity to Spring Security's authentication framework.
 *
 */
@ToString
public class MyUserDetails implements UserDetails {

	@Serial
	private static final long serialVersionUID = 1L;

	/** The wrapped application user entity. */
	private final User user;

	/** {@inheritDoc} */
	public MyUserDetails(User user) {
		this.user = user;
	}


	/** {@inheritDoc} */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		Optional.ofNullable(this.user)
				.map(User::getRoles)
				.orElse(Collections.emptySet())
				.forEach(role -> {
					// Add role authority
					Optional.ofNullable(role.getName())
							.filter(r -> !r.isBlank())
							.ifPresent(r ->
									authorities.add(new SimpleGrantedAuthority(r.toUpperCase()))
							);
							Optional.ofNullable(role.getPermissions())
									.orElse(Collections.emptySet())
									.forEach(permission -> Optional.ofNullable(permission.getActions())
                                            .orElse(Collections.emptySet())
                                            .forEach(action -> Optional.ofNullable(action.getName())
                                                    .filter(a -> !a.isBlank() && permission.getName() != null)
                                                    .ifPresent(a -> {
                                                        String permissionAction = permission.getName().toUpperCase() + ":" + a.toUpperCase();
                                                        authorities.add(new SimpleGrantedAuthority(permissionAction));
                                                    })));
				});

		return authorities;
	}

	/** {@inheritDoc} */
	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	/** {@inheritDoc} */
	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEnabled() {
		return this.user.getIsEnabled();
	}

	/** {@inheritDoc} */
	public User getUserDetails() {
		return user;
	}
}
