package com.anton.gramophone.controller;

import com.anton.gramophone.controller.util.CurrentPrincipalDefiner;
import com.anton.gramophone.entity.Post;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.*;
import com.anton.gramophone.service.CommentService;
import com.anton.gramophone.service.PostService;
import com.anton.gramophone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class PersonalProfileController {
    private final UserService userService;
    private final CommentService commentService;
    private final CurrentPrincipalDefiner currentPrincipalDefiner;
    private final PostService postService;

    @GetMapping
    public UserProfileDto showProfile() {
        User user = currentPrincipalDefiner.getPrincipal();
        return userService.findById(user.getId().toString()).get();
    }

    @GetMapping("/principal-id")
    public Long showPrincipalId() {
        User user = currentPrincipalDefiner.getPrincipal();
        return user.getId();
    }

    @GetMapping("/{id}")
    public Optional<UserProfileDto> showProfile(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/is-subscriber")
    public boolean isSubscriber(@PathVariable String id) {
        User user = currentPrincipalDefiner.getPrincipal();
        return userService.isSubscriber(user, id);
    }

    @GetMapping("/subscribers")
    public List<UserProfileDto> showCurrentUserSubscribers() {
        User user = currentPrincipalDefiner.getPrincipal();
        return userService.showSubscribers(user);
    }

    @GetMapping("/subscriptions")
    public List<UserProfileDto> showCurrentUserSubscriptions() {
        User user = currentPrincipalDefiner.getPrincipal();
        return userService.showSubscriptions(user);
    }

    @GetMapping("/{id}/subscribers")
    public List<UserProfileDto> showUserSubscribers(@PathVariable String id) {
        return userService.showSubscribers(id);
    }

    @GetMapping("/{id}/subscriptions")
    public List<UserProfileDto> showUserSubscriptions(@PathVariable String id) {
        return userService.showSubscriptions(id);
    }

    @PostMapping("/{id}")
    public Optional<UserProfileDto> subscribe(@PathVariable String id) {
        Optional<UserProfileDto> profile = userService.findById(id);
        if (profile.isPresent()) {
            User user = currentPrincipalDefiner.getPrincipal();
            userService.subscribe(user, id);
        }
        return profile;
    }

    @DeleteMapping("/{id}")
    public Optional<UserProfileDto> unsubscribe(@PathVariable String id) {
        Optional<UserProfileDto> profile = userService.findById(id);
        if (profile.isPresent()) {
            User user = currentPrincipalDefiner.getPrincipal();
            userService.unsubscribe(user, id);
        }
        return profile;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserProfileDto editProfile(@RequestBody EditProfileDto profile) {
        String currentUsername = currentPrincipalDefiner.currentUsername();
        return userService.updateUser(currentUsername, profile);
    }

    @GetMapping("/posts/{id}")
    public Optional<PostDto> viewPost(@PathVariable String id) {
        return postService.findById(id);
    }

    @GetMapping("/posts")
    public List<PostDto> viewCurrentUserPosts() {
        User user = currentPrincipalDefiner.getPrincipal();
        return postService.findUserPosts(user);
    }

    @GetMapping("/{id}/posts")
    public List<PostDto> viewUserPosts(@PathVariable String id) {
        return postService.findUserPosts(id);
    }

    @PostMapping(value = "/posts", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostDto> addPost(@RequestBody TextFileDto textFileDto) {
        User user = currentPrincipalDefiner.getPrincipal();
        postService.addPost(user, textFileDto);
        return postService.findUserPosts(user);
    }

    @PostMapping(value = "/posts/like", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostDto> likePost(@RequestBody IdDto postId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return postService.likePost(postId, user);
    }

    @DeleteMapping(value = "/posts/like", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostDto> removeLikeFromPost(@RequestBody IdDto postId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return postService.removeLikeFromPost(postId, user);
    }

    @PutMapping(value = "/posts/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PostDto> editPost(@RequestBody TextFileDto textFileDto, @PathVariable String id) {
        postService.editPost(id, textFileDto);
        User user = currentPrincipalDefiner.getPrincipal();
        return postService.findUserPosts(user);
    }

    @DeleteMapping("/posts/{id}")
    public List<PostDto> deletePost(@PathVariable String id) {
        postService.removePost(id);
        User user = currentPrincipalDefiner.getPrincipal();
        return postService.findUserPosts(user);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> showPostComments(@PathVariable String postId) {
        return commentService.findCommentsByPost(postId);
    }

    @PostMapping(value = "/posts/{postId}/comments", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CommentDto> addComment(@RequestBody TextFileDto textFileDto, @PathVariable String postId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return commentService.addComment(textFileDto, user.getId(), postId);
    }

    @PutMapping(value = "/posts/{postId}/comments", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<CommentDto> editComment(@RequestBody EditCommentDto editedComment, @PathVariable String postId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return commentService.editComment(editedComment, user.getId(), postId);
    }

    @DeleteMapping("/posts/{postId}/comments")
    public List<CommentDto> deleteComment(@RequestBody IdDto commentId, @PathVariable String postId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return commentService.deleteComment(commentId.getId(), user.getId(), postId);
    }
}
