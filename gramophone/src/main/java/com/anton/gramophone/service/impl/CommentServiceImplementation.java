package com.anton.gramophone.service.impl;

import com.anton.gramophone.entity.Comment;
import com.anton.gramophone.entity.Post;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.CommentDto;
import com.anton.gramophone.entity.dto.EditCommentDto;
import com.anton.gramophone.entity.dto.TextFileDto;
import com.anton.gramophone.repository.CommentRepository;
import com.anton.gramophone.repository.PostRepository;
import com.anton.gramophone.repository.UserRepository;
import com.anton.gramophone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImplementation implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CommentDto> addComment(TextFileDto commentContent, Long authorId, String postId) {
        try {
            Long postIdNumber = Long.parseLong(postId);
            Optional<Post> parentPost = postRepository.findById(postIdNumber);
            if (parentPost.isPresent()) {
                Comment comment = modelMapper.map(commentContent, Comment.class);
                comment.setPhotoReference(commentContent.getPictureReference());
                comment.setLikes(0);
                comment.setCreationTime(LocalDateTime.now());
                comment.setOwnerId(authorId);
                comment.setPost(parentPost.get());
                commentRepository.save(comment);
                return findCommentsByPost(postId);
            }
        } catch (NumberFormatException ignored) {

        }
        return Collections.emptyList();
    }

    @Override
    public List<CommentDto> findCommentsByPost(String postId) {
        try {
            Long postIdNumber = Long.parseLong(postId);
            return commentRepository.findAllByPostId(postIdNumber)
                    .stream()
                    .map(comment -> {
                        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
                        User commentOwner = userRepository.findById(comment.getOwnerId()).get();
                        commentDto.setOwnerFullName(
                                commentOwner.getFirstName() + " " + commentOwner.getLastName()
                        );
                        commentDto.setOwnerPictureReference(commentOwner.getProfilePicture());
                        return commentDto;
                    }).collect(Collectors.toList());
        } catch (NumberFormatException ignored) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<CommentDto> editComment(EditCommentDto editedComment, Long userId, String postId) {
        Optional<Comment> commentToEdit = commentRepository.findById(editedComment.getId());
        if (commentToEdit.isPresent() && commentToEdit.get().getOwnerId().equals(userId)) {
            commentToEdit.get().setText(editedComment.getText());
            commentToEdit.get().setFileReference(editedComment.getFileReference());
            commentToEdit.get().setPhotoReference(editedComment.getPhotoReference());
            commentRepository.save(commentToEdit.get());
            return findCommentsByPost(postId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<CommentDto> deleteComment(Long commentId, Long userId, String postId) {
        Optional<Comment> commentToDelete = commentRepository.findById(commentId);
        if (commentToDelete.isPresent() && commentToDelete.get().getOwnerId().equals(userId)) {
            commentRepository.delete(commentToDelete.get());
            return findCommentsByPost(postId);
        }
        return Collections.emptyList();
    }
}
