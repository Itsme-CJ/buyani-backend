package com.bayani.bayaniserver.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[chat_room]")
public class ChatRoom extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Integer chatRoomId;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private Integer type;

	@JsonIgnore
  @ManyToOne
  @JoinColumn(name = "store_id")
	private Store store;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "chatRoom")
	private List<Message> messages;
}
