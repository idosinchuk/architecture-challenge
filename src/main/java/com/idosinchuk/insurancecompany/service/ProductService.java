package com.idosinchuk.insurancecompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.idosinchuk.insurancecompany.dto.ProductRequestDTO;
import com.idosinchuk.insurancecompany.dto.ProductResponseDTO;

/**
 * 
 * Service for product
 * 
 * @author Igor Dosinchuk
 *
 */
public interface ProductService {

	/**
	 * Retrieve list of all products according to the search criteria.
	 * 
	 * @param pageable object for pagination
	 * @return Page of {@link ProductResponseDTO}
	 */
	Page<ProductResponseDTO> getAllProducts(Pageable pageable);

	/**
	 * Find product by the id.
	 * 
	 * @param productCode product code
	 * @return {@link ProductResponseDTO}
	 */
	ProductResponseDTO getProduct(String productCode);

	/**
	 * Add a product..
	 * 
	 * @param productRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> addProduct(ProductRequestDTO productRequestDTO);

	/**
	 * Update the product.
	 * 
	 * @param productCode       product code
	 * @param productRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> updateProduct(String productCode, ProductRequestDTO productRequestDTO);
}
