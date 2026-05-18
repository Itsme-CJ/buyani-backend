package com.buyani.buyaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buyani.buyaniserver.entity.Address;

public interface AddressRepo extends JpaRepository<Address, Integer> {

}
