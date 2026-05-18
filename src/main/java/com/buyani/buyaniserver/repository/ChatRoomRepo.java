package com.buyani.buyaniserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.buyani.buyaniserver.entity.ChatRoom;

public interface ChatRoomRepo extends JpaRepository<ChatRoom, Integer> {
  List<ChatRoom> findByStoreStoreId(Integer storeId);

  List<ChatRoom> findByType(Integer type);

  Optional<ChatRoom> findByChatRoomId(Integer chatRoomId);

  Optional<ChatRoom> findByStoreStoreIdAndUserUserId(Integer storeId, Integer userId);

  List<ChatRoom> findByUserUserId(Integer userId);

  @Query("SELECT cr FROM ChatRoom cr WHERE cr.store.storeId = :storeId AND EXISTS (SELECT m FROM Message m WHERE m.chatRoom = cr)")
  List<ChatRoom> findByStoreIdWithMessages(@Param("storeId") Integer storeId);

  @Query("SELECT cr FROM ChatRoom cr WHERE cr.user.userId = :userId AND EXISTS (SELECT m FROM Message m WHERE m.chatRoom = cr)")
  List<ChatRoom> findByUserIdWithMessages(@Param("userId") Integer userId);
}
