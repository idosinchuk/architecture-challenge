package com.idosinchuk.insurancecompany.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idosinchuk.insurancecompany.dto.ProductRequestDTO;
import com.idosinchuk.insurancecompany.dto.ProductResponseDTO;
import com.idosinchuk.insurancecompany.entity.ProductEntity;
import com.idosinchuk.insurancecompany.repository.ProductRepository;
import com.idosinchuk.insurancecompany.service.ProductService;

/**
 * Implementation for product service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("ProductService")
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * {@inheritDoc}
	 */
	public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {

		Page<ProductEntity> entityResponse = productRepository.findAll(pageable);

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<Page<ProductResponseDTO>>() {
		}.getType());

	}

	/**
	 * {@inheritDoc}
	 */
	public ProductResponseDTO getProduct(int id) {

		ProductEntity entityResponse = productRepository.findById(id);

		return modelMapper.map(entityResponse, ProductResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO) {

		ProductEntity entityRequest = modelMapper.map(productRequestDTO, ProductEntity.class);

		ProductEntity entityResponse = productRepository.save(entityRequest);

		return modelMapper.map(entityResponse, ProductResponseDTO.class);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ProductResponseDTO updateProduct(ProductRequestDTO productRequestDTO) {

		ProductEntity entityRequest = modelMapper.map(productRequestDTO, ProductEntity.class);

		ProductEntity entityResponse = productRepository.save(entityRequest);

		return modelMapper.map(entityResponse, ProductResponseDTO.class);

	}
}
