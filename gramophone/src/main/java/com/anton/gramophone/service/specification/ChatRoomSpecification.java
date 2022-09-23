package com.anton.gramophone.service.specification;

import com.anton.gramophone.entity.ChatRoom;
import com.anton.gramophone.entity.Message;
import com.anton.gramophone.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class ChatRoomSpecification {
    public static Specification<ChatRoom> findUserChatRooms(User user) {
        return (root, query, builder) -> {
            Join<ChatRoom, Message> chatRoomMessagesJoin = root.join("messages");
            query.groupBy(chatRoomMessagesJoin.get("chatRoom"));
            query.orderBy(builder.desc(builder.max(chatRoomMessagesJoin.get("creationDateTime"))));
            return builder.isMember(user, root.get("participants"));
        };
    }
}
