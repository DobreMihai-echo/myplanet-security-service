package com.dissertation.security.controller;

import com.dissertation.security.auth.JwtTokenProvider;
import com.dissertation.security.exceptions.AppException;
import com.dissertation.security.model.Role;
import com.dissertation.security.model.RoleName;
import com.dissertation.security.model.User;
import com.dissertation.security.payloads.requests.LoginRequest;
import com.dissertation.security.payloads.requests.SignUpRequest;
import com.dissertation.security.payloads.responses.ApiResponse;
import com.dissertation.security.payloads.responses.JwtAuthenticationResponse;
import com.dissertation.security.repository.RoleRepository;
import com.dissertation.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins ="*",maxAge = 3600)
public class AuthController {

    @Autowired
    AuthenticationManager manager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,loginRequest.getUsername()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {

        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return new ResponseEntity(new ApiResponse(false,"Username is already taken!"),
                        HttpStatus.BAD_REQUEST);
            }

            //Creating user's account

            User user = new User(signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),
                    signUpRequest.getUsername(),
                    signUpRequest.getPassword());

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(()->new AppException("User Role not set."));

            user.setRoles(Collections.singleton(userRole));

            User result = userRepository.save(user);

//            URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
//                    .buildAndExpand(result.getUsername()).toUri();

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "User registered successfully"));

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
