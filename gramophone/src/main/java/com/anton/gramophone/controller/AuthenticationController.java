package com.anton.gramophone.controller;

import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.AuthenticationDto;
import com.anton.gramophone.entity.dto.RegistrationDto;
import com.anton.gramophone.security.JwtTokenProvider;
import com.anton.gramophone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody AuthenticationDto authenticationDTO) {
        try {
            String email = authenticationDTO.getEmail();
            String password = authenticationDTO.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userService.loadUserByUsername(email);
            if (user == null) {
                throw new UsernameNotFoundException("user not found");
            }
            String token = jwtTokenProvider.createToken(email, user.getRoles());
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean addUser(@RequestBody RegistrationDto profile) {
        return userService.save(profile);
    }
}
