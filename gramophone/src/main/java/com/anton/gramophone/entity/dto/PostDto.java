package com.anton.gramophone.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long id;
    private String ownerFullName;
    private String ownerPictureReference;
    private Long ownerId;
    private LocalDateTime creationDateTime;
    private String text;
    private Long likes;
    private String pictureReference;
    private String fileReference;
}
