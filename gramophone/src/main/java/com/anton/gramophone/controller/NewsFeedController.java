package com.anton.gramophone.controller;

import com.anton.gramophone.controller.util.CurrentPrincipalDefiner;
import com.anton.gramophone.entity.Post;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.PostDto;
import com.anton.gramophone.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/newsFeed")
public class NewsFeedController {
    private final CurrentPrincipalDefiner currentPrincipalDefiner;
    private final PostService postService;

    @GetMapping
    public List<PostDto> showNews() {
        User principal = currentPrincipalDefiner.getPrincipal();
        return postService.showSubscriptionPosts(principal);
    }
}
