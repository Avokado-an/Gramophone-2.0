package com.anton.gramophone.repository;

import com.anton.gramophone.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
