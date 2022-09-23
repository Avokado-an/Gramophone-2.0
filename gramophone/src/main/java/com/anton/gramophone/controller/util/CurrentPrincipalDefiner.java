package com.anton.gramophone.controller.util;

import com.anton.gramophone.entity.User;
import com.anton.gramophone.security.JwtAuthenticationException;
import com.anton.gramophone.security.JwtTokenProvider;
import com.anton.gramophone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrentPrincipalDefiner {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public User getPrincipal() {
        String username = currentUsername();
        return userService.loadUserByUsername(username);
    }

    public String currentUsername() {
        Optional<HttpServletRequest> request = getCurrentHttpRequest();
        if (request.isPresent()) {
            String token = jwtTokenProvider.resolveToken(request.get());
            return jwtTokenProvider.getUsername(token);
        } else {
            throw new JwtAuthenticationException("What the hell is happening: cant get username of current user");
        }
    }

    private Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }
}
