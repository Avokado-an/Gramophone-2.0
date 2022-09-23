package com.anton.gramophone.service.impl;

import com.anton.gramophone.entity.Role;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.EditProfileDto;
import com.anton.gramophone.entity.dto.RegistrationDto;
import com.anton.gramophone.entity.dto.UserProfileDto;
import com.anton.gramophone.entity.dto.UserSearchDto;
import com.anton.gramophone.repository.UserRepository;
import com.anton.gramophone.service.UserService;
import com.anton.gramophone.service.specification.UserSpecification;
import com.anton.gramophone.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Override
    public boolean save(RegistrationDto profile) {
        boolean wasUserSaved = false;
        if (userValidator.validateUserRegistrationData(profile)) {
            if (userRepository.findUserByEmail(profile.getEmail()) == null) {
                profile.setPassword(encoder.encode(profile.getPassword()));
                User user = modelMapper.map(profile, User.class);
                user.setRoles(new HashSet<>(Collections.singletonList(Role.USER)));
                user.setEnabled(true);
                userRepository.save(user);
                wasUserSaved = true;
            }
        }
        return wasUserSaved;
    }

    @Override
    public Optional<UserProfileDto> findById(String id) {
        try {
            Long idNumber = Long.parseLong(id);
            Optional<User> user = userRepository.findById(idNumber);
            return user.map(user1 -> modelMapper.map(user1, UserProfileDto.class));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> modelMapper.map(u, UserProfileDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserProfileDto> filterUsers(UserSearchDto userSearchDTO) {
        List<User> users = userRepository.findAll(Specification
                .where(UserSpecification.firstNameStartsWith(userSearchDTO.getFirstName()))
                .and(UserSpecification.lastNameStartsWith(userSearchDTO.getLastName()))
                .and(UserSpecification.userInstrumentsContain(
                        userSearchDTO.getInstrumentName(), userSearchDTO.getSkillLevel())));
        return users.stream().map(u -> modelMapper.map(u, UserProfileDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserProfileDto updateUser(String email, EditProfileDto profile) {
        User user = userRepository.findUserByEmail(email);
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setGender(profile.getGender());
        user.setStatus(profile.getStatus());
        user.setProfilePicture(profile.getProfilePicture());
        userRepository.save(user);
        return modelMapper.map(user, UserProfileDto.class);
    }

    @Override
    @Transactional
    public void subscribe(User currentUser, String idForSubscription) {
        try {
            Long subId = Long.parseLong(idForSubscription);
            Optional<User> userToSubscribeTo = userRepository.findById(subId);
            if (userToSubscribeTo.isPresent()) {
                currentUser.addSubscription(userToSubscribeTo.get());
                userToSubscribeTo.get().addSubscriber(currentUser);
                userRepository.save(currentUser);
                userRepository.save(userToSubscribeTo.get());
            }
        } catch (NumberFormatException ignored) {

        }
    }

    @Override
    public boolean isSubscriber(User currentUser, String idForSubscription) {
        try {
            Long subId = Long.parseLong(idForSubscription);
            Optional<User> fullCurrentUserInfo = userRepository.findById(currentUser.getId());
            return fullCurrentUserInfo.get().getSubscriptions()
                    .stream()
                    .anyMatch(user -> Objects.equals(user.getId(), subId));
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    @Override
    public void unsubscribe(User currentUser, String idForSubscription) {
        try {
            Long subId = Long.parseLong(idForSubscription);
            Optional<User> userToSubscribeTo = userRepository.findById(subId);
            if (userToSubscribeTo.isPresent()) {
                currentUser.removeSubscription(userToSubscribeTo.get());
                userToSubscribeTo.get().removeSubscriber(currentUser);
                userRepository.save(currentUser);
                userRepository.save(userToSubscribeTo.get());
            }
        } catch (NumberFormatException ignored) {
        }
    }

    @Override
    public List<UserProfileDto> showSubscribers(User currentUser) {
        return userRepository.findAllBySubscriptionsContains(currentUser)
                .stream()
                .map(user -> modelMapper.map(user, UserProfileDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserProfileDto> showSubscriptions(User currentUser) {
        return userRepository.findAllBySubscribersContains(currentUser)
                .stream()
                .map(user -> modelMapper.map(user, UserProfileDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserProfileDto> showSubscribers(String userId) {
        try {
            Long idNumber = Long.parseLong(userId);
            Optional<User> user = userRepository.findById(idNumber);
            return user.map(
                    foundUser -> userRepository.findAllBySubscriptionsContains(foundUser)
                            .stream()
                            .map(user1 -> modelMapper.map(user1, UserProfileDto.class))
                            .collect(Collectors.toList())
            ).orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<UserProfileDto> showSubscriptions(String userId) {
        try {
            Long idNumber = Long.parseLong(userId);
            Optional<User> user = userRepository.findById(idNumber);
            return user.map(
                    foundUser -> userRepository.findAllBySubscribersContains(foundUser)
                            .stream()
                            .map(user1 -> modelMapper.map(user1, UserProfileDto.class))
                            .collect(Collectors.toList())
            ).orElse(Collections.emptyList());
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }
}
