package com.anton.gramophone.security;

import com.anton.gramophone.entity.User;
import com.anton.gramophone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) {
        User user = userService.loadUserByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("username " + s + " is not present");
        }
        return JwtUserFactory.create(user);
    }
}
