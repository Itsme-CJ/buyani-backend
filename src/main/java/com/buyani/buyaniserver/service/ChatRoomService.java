package com.buyani.buyaniserver.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buyani.buyaniserver.dto.ChatRoomDTO;
import com.buyani.buyaniserver.entity.ChatRoom;
import com.buyani.buyaniserver.entity.Store;
import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.repository.ChatRoomRepo;
import com.buyani.buyaniserver.repository.StoreRepo;
import com.buyani.buyaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatRoomService {

  @Autowired StoreRepo storeRepo;
  @Autowired ChatRoomRepo chatRoomRepo;
  @Autowired UserRepo userRepo;

  public ChatRoom createChatRoom(ChatRoomDTO request) {
    Integer storeId = request.getStoreId();
    Integer userId  = request.getUserId();

    Optional<Store> storeOptional = storeRepo.findByStoreId(storeId);
    if (storeOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Optional<User> userOpt = userRepo.findByUserId(userId);
    if (userOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ChatRoom chatRoom = new ChatRoom();
    chatRoom.setName(request.getName());
    chatRoom.setStore(storeOptional.get());
    chatRoom.setUser(userOpt.get());
    chatRoom.setType(request.getType());
    return chatRoomRepo.save(chatRoom);
  }

  public ChatRoom getChatRoom(ChatRoomDTO request) {
    Integer storeId = request.getStoreId();
    Integer userId  = request.getUserId();

    Optional<ChatRoom> existing = chatRoomRepo.findByStoreStoreIdAndUserUserId(storeId, userId);
    if (existing.isPresent()) {
      return existing.get();
    }

    Optional<Store> storeOptional = storeRepo.findByStoreId(storeId);
    if (storeOptional.isEmpty()) {
      log.info("store not found {}", storeId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Optional<User> userOpt = userRepo.findByUserId(userId);
    if (userOpt.isEmpty()) {
      log.info("user not found {}", userId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ChatRoom chatRoom = new ChatRoom();
    chatRoom.setName(storeOptional.get().getName());
    chatRoom.setStore(storeOptional.get());
    chatRoom.setUser(userOpt.get());
    chatRoom.setType(4);
    return chatRoomRepo.save(chatRoom);
  }

  public List<ChatRoom> getChatRoomsByUserId(Integer userId) {
    return chatRoomRepo.findByUserUserId(userId);
  }

  public List<ChatRoom> getChatRoomsByStoreId(Integer storeId) {
    return chatRoomRepo.findByStoreStoreId(storeId);
  }

  public ChatRoom getChatRoomById(Integer id) {
    return chatRoomRepo.findById(id).orElse(null);
  }
}