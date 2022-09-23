package com.anton.gramophone.validator;

import com.anton.gramophone.entity.Gender;
import com.anton.gramophone.entity.dto.RegistrationDto;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final String NON_LETTER_NUMBER_REGEX = "[\\W]";
    private static final int MIN_LOGIN_PASSWORD_LENGTH = 4;
    private static final int MAX_LOGIN_PASSWORD_LENGTH = 29;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+$";

    public boolean validateUserRegistrationData(RegistrationDto profile) {
        return profile.getPassword().equals(profile.getRepeatedPassword()) &&
                // todo think about it - validateGender(profile.getGender()) &&
                validateEmail(profile.getEmail());
    }

    public boolean validateUsernameOrPassword(String username) {
        boolean isAppropriateLine = true;
        if (username != null) {
            Pattern pattern = Pattern.compile(NON_LETTER_NUMBER_REGEX);
            Matcher matcher = pattern.matcher(username);
            if (matcher.find() || username.length() > MAX_LOGIN_PASSWORD_LENGTH || username.length() < MIN_LOGIN_PASSWORD_LENGTH) {
                isAppropriateLine = false;
            }
        } else {
            isAppropriateLine = false;
        }
        return isAppropriateLine;
    }

    public boolean validateGender(String gender) {
        boolean isValidGender = true;
        try {
            Gender.valueOf(gender);
        } catch (IllegalArgumentException | NullPointerException e) {
            isValidGender = false;
        }
        return isValidGender;
    }

    public boolean validateEmail(String email) {
        boolean wasSuccessfulValidation = false;
        if (email != null) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            wasSuccessfulValidation = matcher.find();
        }
        return wasSuccessfulValidation;
    }
}
