package com.amine.urticare.repository;

import com.amine.urticare.model.entities.TokenEntity;
import com.amine.urticare.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Token repository.
 */
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {


    /**
     * Find first by user id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    Optional<TokenEntity> findFirstByUserId(Long userId);

    /**
     * Delete all by user.
     *
     * @param user the user
     */
    void deleteAllByUser(User user);

    /**
     * Find by token token entity.
     *
     * @param token the token
     * @return the token entity
     */
    Optional<TokenEntity> findByAccessToken(String token);

}
