package com.buyani.buyaniserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buyani.buyaniserver.entity.Message;

public interface MessageRepo extends JpaRepository<Message, Integer> {
  List<Message> findByChatRoomChatRoomIdOrderByTs(Integer chatRoomId);
}
