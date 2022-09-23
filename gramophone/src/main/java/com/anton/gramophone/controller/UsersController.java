package com.anton.gramophone.controller;

import com.anton.gramophone.entity.dto.UserProfileDto;
import com.anton.gramophone.entity.dto.UserSearchDto;
import com.anton.gramophone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController//todo almost ready (no search by genres)
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @GetMapping
    public List<UserProfileDto> viewUsers() {
        return userService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<UserProfileDto> filterUsers(@RequestBody UserSearchDto userSearchDTO) {
        return userService.filterUsers(userSearchDTO);
    }
}
