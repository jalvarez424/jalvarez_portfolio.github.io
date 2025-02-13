package com.fracturedmind.HeroRPG.Controllers;

import com.fracturedmind.HeroRPG.DTO.UserRegistrationRequest;
import com.fracturedmind.HeroRPG.Models.User;
import com.fracturedmind.HeroRPG.Repos.UserRepository;
import com.fracturedmind.HeroRPG.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userRepository.findById(id).orElseThrow());
    }

    // Since we're sending JSON data, it's better to use a DTO (Data Transfer Object) instead of directly passing an User entity.
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationRequest request) {
        User newUser = userService.registerUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(newUser);
    }
}
