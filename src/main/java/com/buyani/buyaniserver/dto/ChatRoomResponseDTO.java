package com.buyani.buyaniserver.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponseDTO {
    private Integer chatRoomId;
    private String name;
    private Integer type;
    private Integer userId;
    private Integer storeId;
    private Date ts;
}