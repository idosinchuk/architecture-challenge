package com.idosinchuk.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.insurancecompany.entity.ProductEntity;

/**
 * Repository for product
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	ProductEntity findByProductCode(String productCode);

}
