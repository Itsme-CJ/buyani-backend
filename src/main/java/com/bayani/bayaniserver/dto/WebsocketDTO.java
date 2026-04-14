package com.bayani.bayaniserver.dto;

import lombok.Data;

@Data
public class WebsocketDTO {
  Integer userId;
  Integer storeId;
  Integer chatRoomId;
  String name;
  String content;
  String to;
}
