package com.sample.ec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.ec.model.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

}
