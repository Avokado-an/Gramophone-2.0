package com.anton.gramophone.security;

import com.anton.gramophone.entity.Role;
import com.anton.gramophone.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {
    JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail(),
                user.isEnabled(), defineAuthorities(user.getRoles()));
    }

    private static Set<SimpleGrantedAuthority> defineAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());
    }
}
