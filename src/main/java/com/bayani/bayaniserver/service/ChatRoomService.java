package com.bayani.bayaniserver.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bayani.bayaniserver.dto.ChatRoomDTO;
import com.bayani.bayaniserver.entity.ChatRoom;
import com.bayani.bayaniserver.entity.Store;
import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.repository.ChatRoomRepo;
import com.bayani.bayaniserver.repository.StoreRepo;
import com.bayani.bayaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatRoomService {
  @Autowired
  StoreRepo storeRepo;

  @Autowired
  ChatRoomRepo chatRoomRepo;

  @Autowired
  UserRepo userRepo;

  public ChatRoom createChatRoom(ChatRoomDTO request) {

    Integer storeId = request.getStoreId();

    Optional<Store> storeoOptional = storeRepo.findByStoreId(storeId);
    if (storeoOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ChatRoom chatRoom = new ChatRoom();
    chatRoom.setName(request.getName());
    chatRoom.setStore(storeoOptional.get());
    chatRoom.setName(request.getName());
    chatRoom.setType(request.getType());
    
    return chatRoomRepo.save(chatRoom);
  }

  public ChatRoom getChatRoom(ChatRoomDTO request) {
    Integer storeId = request.getStoreId();
    Integer userId = request.getUserId();

    Optional<ChatRoom> sOptional = chatRoomRepo.findByStoreStoreIdAndUserUserId(storeId, userId);
    if (sOptional.isPresent()) {
      return sOptional.get();
    }


    Optional<Store> storeoOptional = storeRepo.findByStoreId(storeId);
    if (storeoOptional.isEmpty()) {
      log.info("store not found {}", storeId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Optional<User> userOptional = userRepo.findByUserId(userId);
    if (userOptional.isEmpty()) {
      log.info("user not found {}", userId);
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ChatRoom chatRoom = new ChatRoom();
    chatRoom.setName(storeoOptional.get().getName());
    chatRoom.setStore(storeoOptional.get());
    chatRoom.setType(4);
    chatRoom.setUser(userOptional.get());
    
    return chatRoomRepo.save(chatRoom);
  }
}
