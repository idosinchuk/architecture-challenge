package com.idosinchuk.insurancecompany.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.idosinchuk.insurancecompany.dto.ProductRequestDTO;
import com.idosinchuk.insurancecompany.dto.ProductResponseDTO;
import com.idosinchuk.insurancecompany.service.ProductService;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for insurance products
 * 
 * @author Igor Dosinchuk
 *
 */
@RestController
@Api(value = "API Rest for insurance products.")
@RequestMapping(value = "/api/v1")
public class ProductController {

	public static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	ProductService productService;

	/**
	 * Retrieve list of all products according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all products, headers and status
	 */
	@GetMapping(path = "/products")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all products according to the search criteria.")
	public ResponseEntity<PagedResources<ProductResponseDTO>> getAllProducts(Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler, @RequestHeader("User-Agent") String userAgent) {

		logger.info("Fetching all products");

		Page<ProductResponseDTO> product = null;

		try {
			// Find products in DB with paging filters
			product = productService.getAllProducts(pageable);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.put(HttpHeaders.USER_AGENT, Arrays.asList(userAgent));

		PagedResources<ProductResponseDTO> pagedResources = pagedResourcesAssembler.toResource(product);

		return new ResponseEntity<>(pagedResources, headers, HttpStatus.OK);
	}

	/**
	 * Retrieve product by the productCode.
	 * 
	 * @param productCode product code
	 * @return ResponseEntity with status and productResponseDTO
	 */
	@GetMapping(path = "/products/{productCode}")
	@ResponseBody
	@ApiOperation(value = "Retrieve product by the productCode.")
	public ResponseEntity<?> getProducts(@PathVariable("productCode") String productCode) {

		logger.info("Fetching product with productCode {}", productCode);

		ProductResponseDTO product = null;

		try {
			// Search product in BD by productCode
			product = productService.getProduct(productCode);

			return new ResponseEntity<>(product, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

	}

	/**
	 * Add a product.
	 * 
	 * @param productRequestDTO object to save
	 * @return ResponseEntity with status and productResponseDTO
	 */
	@PostMapping(path = "/products", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Add a product.")
	public ResponseEntity<?> addProducts(@RequestBody ProductRequestDTO productRequestDTO) {

		logger.info(("Process add new product"));

		return productService.addProduct(productRequestDTO);

	}

	/**
	 * El método PATCH solicita que se aplique un conjunto de cambios descritos en
	 * la entidad de solicitud al recurso identificado por el URI de Solicitud.
	 * 
	 * @param productCode       product code
	 * @param productRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/products/{productCode}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Update the product.")
	public ResponseEntity<?> updateProducts(@PathVariable("productCode") String productCode,
			@RequestBody ProductRequestDTO productRequestDTO) {

		logger.info("Process patch product");

		return productService.updateProduct(productCode, productRequestDTO);
	}
}
