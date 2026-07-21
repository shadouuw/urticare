package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.MyUserDetails;
import com.amine.urticare.model.entities.User;
import com.amine.urticare.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserService} with the required dependencies.
     *
     * @param userRepository   repository for accessing {@link User} data
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email for authentication purposes.
     * Used by Spring Security during login attempts.Only enabled users are returned.
     *
     * @param email the email of the user to look up
     * @return the {@link User} entity implementing {@link UserDetails}
     * @throws UsernameNotFoundException if the user is not found or disabled
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmailIgnoreCaseAndIsEnabled(email, true)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));

            return new MyUserDetails(user);
        } catch (UsernameNotFoundException ex) {

            log.warn("User not found with email: {}", email);
            throw ex;
        } catch (Exception ex) {

            log.error("Unexpected error while loading user by email {}", email, ex);
            throw new UsernameNotFoundException("Unable to load user with email " + email, ex);
        }
    }
}
