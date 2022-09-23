package com.anton.gramophone.service.impl;

import com.anton.gramophone.entity.ChatRoom;
import com.anton.gramophone.entity.Message;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.EditMessageDto;
import com.anton.gramophone.entity.dto.TextDto;
import com.anton.gramophone.repository.ChatRoomRepository;
import com.anton.gramophone.repository.MessageRepository;
import com.anton.gramophone.service.ChatRoomService;
import com.anton.gramophone.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImplementation implements MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    @Override
    public ChatRoom deleteMessage(User user, String chatId, Long messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent() && message.get().getAuthorId().equals(user.getId())) {
            messageRepository.deleteById(messageId);
        }
        return chatRoomService.showChat(user, chatId);
    }

    @Transactional
    @Override
    public ChatRoom editMessage(User user, String chatId, EditMessageDto redactedMessage) {
        Optional<Message> messageToEdit = messageRepository.findById(redactedMessage.getId());
        if (messageToEdit.isPresent() && messageToEdit.get().getAuthorId().equals(user.getId())) {
            messageToEdit.get().setText(redactedMessage.getText());
            messageRepository.save(messageToEdit.get());
        }
        return chatRoomService.showChat(user, chatId);
    }

    @Transactional
    @Override
    public ChatRoom createMessage(User owner, String chatId, TextDto newMessage) {
        Message createdMessage = new Message();
        Optional<ChatRoom> chatRoom = defineChatRoom(chatId);
        if (chatRoom.isPresent()) {
            createdMessage.setText(newMessage.getText());
            createdMessage.setAuthorId(owner.getId());
            createdMessage.setChatRoom(chatRoom.get());
            createdMessage.setCreationDateTime(LocalDateTime.now());
            messageRepository.save(createdMessage);
        }
        return chatRoomService.showChat(owner, chatId);
    }

    @Override
    public Message updateMessages(Message message) {
        return messageRepository.save(message);
    }

    private Optional<ChatRoom> defineChatRoom(String chatId) {
        try {
            Long id = Long.parseLong(chatId);
            return chatRoomRepository.findById(id);
        } catch (NumberFormatException ignored) {

        }
        return Optional.empty();
    }
}
