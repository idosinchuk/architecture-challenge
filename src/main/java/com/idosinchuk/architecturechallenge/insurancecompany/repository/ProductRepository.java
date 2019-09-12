package com.idosinchuk.architecturechallenge.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.architecturechallenge.insurancecompany.entity.ProductEntity;

/**
 * Repository for product
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	ProductEntity findByProductCode(String productCode);

}
