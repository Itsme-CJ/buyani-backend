package com.buyani.buyaniserver.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[role]")
public class Role extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn(name = "role_id", referencedColumnName = "role_id")
	private User user;
}
