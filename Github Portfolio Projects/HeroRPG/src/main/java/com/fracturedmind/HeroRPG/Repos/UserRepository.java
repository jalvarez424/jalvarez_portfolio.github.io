package com.fracturedmind.HeroRPG.Repos;

import com.fracturedmind.HeroRPG.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
