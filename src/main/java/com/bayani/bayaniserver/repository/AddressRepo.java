package com.bayani.bayaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.Address;

public interface AddressRepo extends JpaRepository<Address, Integer> {

}
