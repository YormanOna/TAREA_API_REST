package com.aplication.rest.repository;

import com.aplication.rest.entities.Maker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MakerRepository extends CrudRepository<Maker, Long>{
}