package com.anton.gramophone.controller;

import com.anton.gramophone.controller.util.CurrentPrincipalDefiner;
import com.anton.gramophone.entity.ChatRoom;
import com.anton.gramophone.entity.User;
import com.anton.gramophone.entity.dto.ChatDto;
import com.anton.gramophone.entity.dto.EditMessageDto;
import com.anton.gramophone.entity.dto.IdDto;
import com.anton.gramophone.entity.dto.TextDto;
import com.anton.gramophone.service.ChatRoomService;
import com.anton.gramophone.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {
    private final CurrentPrincipalDefiner currentPrincipalDefiner;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @GetMapping
    public List<ChatRoom> viewChatRooms() {
        User user = currentPrincipalDefiner.getPrincipal();
        return chatRoomService.findByUser(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoom createChatRoom(@RequestBody ChatDto chatDto) {
        User user = currentPrincipalDefiner.getPrincipal();
        return chatRoomService.createChatRoom(user, chatDto);
    }

    @PostMapping("/{recipientId}")
    public ChatRoom createChatRoom(@PathVariable String recipientId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return chatRoomService.createChatRoom(user, recipientId);
    }

    @GetMapping("/{chatId}")
    public ChatRoom viewChatRoom(@PathVariable String chatId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return chatRoomService.showChat(user, chatId);
    }

    @PostMapping(value = "/conversation/{chatId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoom addMessage(@PathVariable String chatId, @RequestBody TextDto newMessage) {
        User user = currentPrincipalDefiner.getPrincipal();
        return messageService.createMessage(user, chatId, newMessage);
    }

    @DeleteMapping(value = "/conversation/{chatId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoom deleteMessage(@PathVariable String chatId, @RequestBody IdDto messageId) {
        User user = currentPrincipalDefiner.getPrincipal();
        return messageService.deleteMessage(user, chatId, messageId.getId());
    }

    @PutMapping(value = "/conversation/{chatId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ChatRoom editMessage(@PathVariable String chatId, @RequestBody EditMessageDto editedMessage) {
        User user = currentPrincipalDefiner.getPrincipal();
        return messageService.editMessage(user, chatId, editedMessage);
    }
}
