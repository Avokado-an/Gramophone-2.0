package com.anton.gramophone.service;

import com.anton.gramophone.entity.ChatRoom;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.ChatDto;

import java.util.List;

public interface ChatRoomService {
    List<ChatRoom> findByUser(User user);

    ChatRoom showChat(User user, String chatId);

    ChatRoom createChatRoom(User currentUser, ChatDto chatDto);

    ChatRoom createChatRoom(User currentUser, String recipientId);
}
