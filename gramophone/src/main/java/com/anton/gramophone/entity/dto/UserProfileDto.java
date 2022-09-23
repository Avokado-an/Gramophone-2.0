package com.anton.gramophone.entity.dto;

import com.anton.gramophone.entity.Gender;
import com.anton.gramophone.entity.Instrument;
import com.anton.gramophone.entity.Post;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private boolean isEnabled;
    private String status;
    private String profilePicture;
    private List<Instrument> instruments;
}
