package com.bayani.bayaniserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[rating]")
public class Rating extends Abstract implements IEntity {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rating_id")
	private Integer ratingId;

	@Column(name = "rate")
	private Integer rate;

	@JsonIgnore
  @ManyToOne
  @JoinColumn(name = "store_id")
	private Store store;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id")
	private User user;
}
