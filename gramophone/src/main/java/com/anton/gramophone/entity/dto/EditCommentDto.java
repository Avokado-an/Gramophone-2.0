package com.anton.gramophone.entity.dto;

import lombok.Data;

@Data
public class EditCommentDto {
    private Long id;
    private String text;
    private String fileReference;
    private String photoReference;
}
