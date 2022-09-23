package com.anton.gramophone.service;

import com.anton.gramophone.entity.Post;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.IdDto;
import com.anton.gramophone.entity.dto.PostDto;
import com.anton.gramophone.entity.dto.TextFileDto;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<PostDto> findUserPosts(User user);

    List<PostDto> findUserPosts(String userId);

    void addPost(User user, TextFileDto post);

    boolean removePost(String id);

    boolean editPost(String postId, TextFileDto replacingPost);

    List<PostDto> showSubscriptionPosts(User user);

    Optional<PostDto> findById(String id);

    List<PostDto> likePost(IdDto postId, User currentUser);

    List<PostDto> removeLikeFromPost(IdDto postId, User currentUser);
}
