package com.sample.ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.ec.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
