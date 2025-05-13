package com.rishi.book.manager.controller;

import com.rishi.book.manager.dto.RequestDto;
import com.rishi.book.manager.model.User;
import com.rishi.book.manager.model.UserRole;
import com.rishi.book.manager.repository.UserRepository;
import com.rishi.book.manager.repository.UserRoleRepository;
import com.rishi.book.manager.service.JwtService;
import com.rishi.book.manager.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MyUserDetailsService myUserDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            MyUserDetailsService myUserDetailsService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserRoleRepository userRoleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.myUserDetailsService = myUserDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RequestDto requestDto) {
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        Set<UserRole> roles = new HashSet<>();
        UserRole userRole = userRoleRepository.findByName("USER");
        roles.add(userRole);

        User newUser = new User(
                requestDto.getUsername(),
                passwordEncoder.encode(requestDto.getPassword()),
                roles
        );

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequestDto requestDto) {
        try{
            //1. authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword()
                    )
            );

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(requestDto.getUsername());

            String jwtToken = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(jwtToken);
        }catch (Exception e){

            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
