package com.anton.gramophone.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String ownerFullName;
    private String ownerPictureReference;
    private Long ownerId;
    private LocalDateTime creationTime;
    private String text;
    private Long likes;
    private String fileReference;
}
