package com.fracturedmind.HeroRPG.Services;

import com.fracturedmind.HeroRPG.Models.CharacterStats;
import com.fracturedmind.HeroRPG.Models.User;
import com.fracturedmind.HeroRPG.Repos.CharacterStatsRepository;
import com.fracturedmind.HeroRPG.Repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CharacterStatsRepository characterStatsRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }

        // Create User and encrypt password before saving
        User user = new User(username, passwordEncoder.encode(password));
        user = userRepository.save(user);

        // Create CharacterStats tied to this user
        CharacterStats characterStats = new CharacterStats(user);
        characterStatsRepository.save(characterStats);

        return user;
    }
}
