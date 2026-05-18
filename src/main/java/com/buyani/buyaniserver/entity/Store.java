package com.buyani.buyaniserver.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[store]")
public class Store extends Abstract {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_id")
  private Integer storeId;

  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "image")
  private String image;

  @Column(name = "status")
  private String status;

  @Column(name = "is_reservation_activated")
  private Integer isReservationActivated;

  @Column(name = "first_address")
  private String firstAddress;

  @Column(name = "second_address")
  private String secondAddress;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "postal_code")
  private String postalCode;

  @Column(name = "pin_location")
  private String pinLocation;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "description")
  private String description;

  @Column(name = "account_name")
  private String accountName;

  @Column(name = "account_number")
  private String accountNumber;

  @Column(name = "qr_code")
  private String qrCode;

  @OneToMany(mappedBy = "store")
  private List<ProductCategory> productCategories;

  @OneToMany(mappedBy = "store")
  private List<ChatRoom> chatRooms;

  @OneToMany(mappedBy = "store")
  private List<Reservation> reservations;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "store", cascade = CascadeType.ALL)
  private List<OpeningHour> openingHours;
}