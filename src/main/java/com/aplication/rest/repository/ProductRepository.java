package com.aplication.rest.repository;

import com.aplication.rest.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}
