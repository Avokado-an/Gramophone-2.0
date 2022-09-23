package com.anton.gramophone.service.impl;

import com.anton.gramophone.entity.ChatRoom;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.ChatDto;
import com.anton.gramophone.repository.ChatRoomRepository;
import com.anton.gramophone.repository.UserRepository;
import com.anton.gramophone.service.ChatRoomService;
import com.anton.gramophone.service.specification.ChatRoomSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImplementation implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Override
    public List<ChatRoom> findByUser(User user) {
        //List<ChatRoom> chats = chatRoomRepository.findAllByParticipantsContains(user);
        List<ChatRoom> chats = chatRoomRepository.findAll(Specification.where(ChatRoomSpecification.findUserChatRooms(user)));
        for (ChatRoom chat : chats) {
            defineOneToOnChatParameters(chat, user);
            defineOneUserChatParameters(chat, user);
        }
        return chats;
    }

    @Override
    public ChatRoom showChat(User user, String id) {
        Long chatId = Long.parseLong(id);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(chatId);
        if (chatRoom.get().getParticipants().contains(user)) {
            defineOneToOnChatParameters(chatRoom.get(), user);
            defineOneUserChatParameters(chatRoom.get(), user);
        }
        return chatRoom.get();
    }

    @Override
    public ChatRoom createChatRoom(User currentUser, ChatDto chatDto) {
        Set<User> users =
                chatDto.getMembersUserNames().stream().map(userRepository::findUserByEmail).collect(Collectors.toSet());
        users.add(currentUser);
        ChatRoom newChatRoom = new ChatRoom();
        newChatRoom.setParticipants(users);
        newChatRoom.setName(chatDto.getChatName());
        chatRoomRepository.save(newChatRoom);
        return newChatRoom;
    }

    @Override
    public ChatRoom createChatRoom(User currentUser, String recipientId) {
        ChatRoom chatRoom = new ChatRoom();
        try {
            Long id = Long.parseLong(recipientId);
            Optional<User> recipient = userRepository.findById(id);
            if (recipient.isPresent()) {
                chatRoom.setParticipants(Stream.of(currentUser, recipient.get()).collect(Collectors.toSet()));
                chatRoomRepository.save(chatRoom);
            }
        } catch (NumberFormatException ignored) {

        }
        return chatRoom;
    }

    private ChatRoom defineOneToOnChatParameters(ChatRoom chatRoom, User currentUser) {
        if (chatRoom.getParticipants().size() == 2) {
            for (User recipient : chatRoom.getParticipants()) {
                if (!recipient.equals(currentUser)) {
                    chatRoom.setName(recipient.getFirstName() + " " + recipient.getLastName());
                }
            }
        }
        return chatRoom;
    }

    private ChatRoom defineOneUserChatParameters(ChatRoom chatRoom, User currentUser) {
        if (chatRoom.getParticipants().size() == 1) {
            chatRoom.setName(currentUser.getFirstName() + " " + currentUser.getLastName());
        }
        return chatRoom;
    }
}
