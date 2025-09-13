package com.studentspring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.studentspring.entity.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    default Optional<AppUser> logAndFindByUsername(String username) {
    Logger logger = LoggerFactory.getLogger(UserRepository.class);
    logger.debug("🔎 Checking if username exists in DB: {}", username);
    return findByUsername(username);
}
}





