package com.anton.gramophone.service;

import com.anton.gramophone.entity.dto.CommentDto;
import com.anton.gramophone.entity.dto.EditCommentDto;
import com.anton.gramophone.entity.dto.TextFileDto;

import java.util.List;

public interface CommentService {
    List<CommentDto> addComment(TextFileDto comment, Long authorId, String postId);

    List<CommentDto> findCommentsByPost(String postId);

    List<CommentDto> editComment(EditCommentDto newComment, Long userId, String postId);

    List<CommentDto> deleteComment(Long commentId, Long userId, String postId);
}
