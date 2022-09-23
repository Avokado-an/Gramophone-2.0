package com.anton.gramophone.service.impl;

import com.anton.gramophone.entity.Post;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.IdDto;
import com.anton.gramophone.entity.dto.PostDto;
import com.anton.gramophone.entity.dto.TextFileDto;
import com.anton.gramophone.repository.PostRepository;
import com.anton.gramophone.service.PostService;
import com.anton.gramophone.service.specification.PostSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImplementation implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostDto> findUserPosts(User user) {
        return reformatPostToDto(postRepository.findAllByUser(user));
    }

    @Override
    public List<PostDto> findUserPosts(String userId) {
        try {
            Long userIdValue = Long.parseLong(userId);
            return reformatPostToDto(postRepository.findAllByUserId(userIdValue));
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public void addPost(User owner, TextFileDto textFileDto) {
        Post post = modelMapper.map(textFileDto, Post.class);
        post.setLikes(0);
        post.setUser(owner);
        post.setCreationDateTime(LocalDateTime.now());
        postRepository.save(post);
    }

    @Transactional
    @Override
    public boolean removePost(String id) {
        try {
            Long trueId = Long.parseLong(id);
            int amount = postRepository.removeAllById(trueId);
            return amount == 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public boolean editPost(String postId, TextFileDto replacingPost) {
        boolean wasPostSaved = false;
        try {
            Long id = Long.parseLong(postId);
            Optional<Post> post = postRepository.findById(id);
            if (post.isPresent()) {
                post.get().setFileReference(replacingPost.getFileReference());
                post.get().setPictureReference(replacingPost.getPictureReference());
                post.get().setText(replacingPost.getText());
                postRepository.save(post.get());
                wasPostSaved = true;
            }
        } catch (NumberFormatException ignored) {
        }
        return wasPostSaved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> showSubscriptionPosts(User user) {
        List<Post> posts = postRepository.findAll(Specification.where(PostSpecification.findFriendPosts(user)));
        return reformatPostToDto(posts);
    }

    @Override
    public Optional<PostDto> findById(String id) {
        try {
            Long postId = Long.parseLong(id);
            Optional<Post> post = postRepository.findById(postId);
            return post.map(this::createDtoFromPost);
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public List<PostDto> likePost(IdDto postId, User currentUser) {
        try {
            Optional<Post> postToLike = postRepository.findById(postId.getId());
            if (postToLike.isPresent()) {
                postToLike.get().like();
                postRepository.save(postToLike.get());
            }
        } catch (NumberFormatException ignored) {

        }
        return reformatPostToDto(postRepository.findAllByUser(currentUser));
    }

    @Override
    public List<PostDto> removeLikeFromPost(IdDto postId, User currentUser) {
        try {
            Optional<Post> postToLike = postRepository.findById(postId.getId());
            if (postToLike.isPresent() && postToLike.get().getLikes() > 0) {
                postToLike.get().removeLike();
                postRepository.save(postToLike.get());
            }
        } catch (NumberFormatException ignored) {

        }
        return reformatPostToDto(postRepository.findAllByUser(currentUser));
    }

    private List<PostDto> reformatPostToDto(List<Post> posts) {
        List<PostDto> postDtos = new ArrayList<>();
        posts.forEach(post -> postDtos.add(createDtoFromPost(post)));
        return postDtos;
    }

    private PostDto createDtoFromPost(Post post) {
        PostDto dto = modelMapper.map(post, PostDto.class);
        dto.setOwnerFullName(post.getUser().getFirstName() + " " + post.getUser().getLastName());
        dto.setOwnerId(post.getUser().getId());
        dto.setOwnerPictureReference(post.getUser().getProfilePicture());
        return dto;
    }
}
