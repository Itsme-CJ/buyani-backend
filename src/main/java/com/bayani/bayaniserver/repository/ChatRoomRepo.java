package com.bayani.bayaniserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.ChatRoom;

public interface ChatRoomRepo extends JpaRepository<ChatRoom, Integer> {
  List<ChatRoom> findByStoreStoreId(Integer storeId);

  List<ChatRoom> findByType(Integer type);

  Optional<ChatRoom> findByChatRoomId(Integer chatRoomId);

  Optional<ChatRoom> findByStoreStoreIdAndUserUserId(Integer storeId, Integer userId);
  
  List<ChatRoom> findByUserUserId(Integer userId);
}
