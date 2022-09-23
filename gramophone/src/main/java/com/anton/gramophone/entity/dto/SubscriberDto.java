package com.anton.gramophone.entity.dto;

import com.anton.gramophone.entity.Gender;
import lombok.Data;

@Data
public class SubscriberDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private boolean isEnabled;
    private String status;
    private String profilePicture;
}
