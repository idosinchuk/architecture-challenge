package com.idosinchuk.insurancecompany.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idosinchuk.insurancecompany.common.CustomMessage;
import com.idosinchuk.insurancecompany.controller.ProductController;
import com.idosinchuk.insurancecompany.dto.ProductRequestDTO;
import com.idosinchuk.insurancecompany.dto.ProductResponseDTO;
import com.idosinchuk.insurancecompany.entity.ProductEntity;
import com.idosinchuk.insurancecompany.repository.ProductRepository;
import com.idosinchuk.insurancecompany.service.ProductService;
import com.idosinchuk.insurancecompany.util.ArrayListCustomMessage;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

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

	public static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

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
	public ProductResponseDTO getProduct(String productCode) {

		ProductEntity entityResponse = productRepository.findByProductCode(productCode);

		return modelMapper.map(entityResponse, ProductResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> addProduct(ProductRequestDTO productRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			// Check if productCode exists in DB
			ProductEntity productEntity = productRepository.findByProductCode(productRequestDTO.getProductCode());

			// If exists
			if (productEntity != null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"Product Code" + productRequestDTO.getProductCode() + " already exists in database!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProductController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			ProductEntity entityRequest = modelMapper.map(productRequestDTO, ProductEntity.class);

			productRepository.save(entityRequest);

			customMessageList = ArrayListCustomMessage.setMessage("Created new product", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProductController.class).withSelfRel());

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> updateProduct(String productCode, ProductRequestDTO productRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find product by productCode for check if exists in DB
			ProductEntity productEntity = productRepository.findByProductCode(productCode);

			// If exists
			if (productEntity != null) {

				customMessageList = ArrayListCustomMessage.setMessage("Patch product process", HttpStatus.OK);

				// The product code will always be the same, so we do not allow it to be
				// updated, for them we overwrite the field with the original value.
				productRequestDTO.setProductCode(productCode);

				ProductEntity entityRequest = modelMapper.map(productRequestDTO, ProductEntity.class);
				productRepository.save(entityRequest);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Product Code" + productCode + " Not Found!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProductController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProductController.class).slash(productCode).withSelfRel());

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}
}
