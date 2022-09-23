package com.anton.gramophone.repository;

import com.anton.gramophone.entity.ChatRoom;
import com.anton.gramophone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, JpaSpecificationExecutor<ChatRoom> {
    List<ChatRoom> findAllByParticipantsContains(User user);
}
