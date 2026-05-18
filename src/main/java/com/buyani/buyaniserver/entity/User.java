package com.buyani.buyaniserver.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "app_user")
public class User extends Abstract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email_address", unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "status")
    private String status;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "pin")
    private Integer pin;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    // ✅ NEW — tracks whether the user verified their email
    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private List<ReservationList> reservationLists;
}