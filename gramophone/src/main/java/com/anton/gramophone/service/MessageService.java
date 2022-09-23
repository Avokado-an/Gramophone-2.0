package com.anton.gramophone.service;

import com.anton.gramophone.entity.ChatRoom;
import com.anton.gramophone.entity.Message;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.EditMessageDto;
import com.anton.gramophone.entity.dto.TextDto;

public interface MessageService {
    ChatRoom deleteMessage(User user, String chatId, Long messageId);

    ChatRoom editMessage(User user, String chatId, EditMessageDto redactedMessage);

    ChatRoom createMessage(User owner, String chatId, TextDto newMessage);

    Message updateMessages(Message message);
}
