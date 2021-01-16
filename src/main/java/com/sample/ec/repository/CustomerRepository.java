package com.sample.ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sample.ec.model.EcCustomer;

@Transactional
public interface CustomerRepository extends JpaRepository<EcCustomer, Integer> {


}
