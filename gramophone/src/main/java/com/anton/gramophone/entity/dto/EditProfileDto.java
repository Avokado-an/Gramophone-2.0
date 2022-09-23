package com.anton.gramophone.entity.dto;

import com.anton.gramophone.entity.Gender;
import lombok.Data;

@Data
public class EditProfileDto {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String status;
    private String profilePicture;
}
