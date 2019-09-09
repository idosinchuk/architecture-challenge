package com.idosinchuk.insurancecompany.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
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

import com.idosinchuk.insurancecompany.common.CustomMessage;
import com.idosinchuk.insurancecompany.dto.ProductRequestDTO;
import com.idosinchuk.insurancecompany.dto.ProductResponseDTO;
import com.idosinchuk.insurancecompany.service.ProductService;
import com.idosinchuk.insurancecompany.util.ArrayListCustomMessage;
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
	 * Retrieve product by the id.
	 * 
	 * @param id product identifier
	 * @return ResponseEntity with status and productResponseDTO
	 */
	@GetMapping(path = "/products/{id}")
	@ResponseBody
	@ApiOperation(value = "Retrieve product by the id.")
	public ResponseEntity<?> getProducts(@PathVariable("id") int id) {

		logger.info("Fetching product with ID {}", id);

		ProductResponseDTO product = null;

		try {
			// Search product in BD by ID
			product = productService.getProduct(id);

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

		Resources<CustomMessage> resource = null;

		try {

			productService.addProduct(productRequestDTO);

			List<CustomMessage> customMessageList = ArrayListCustomMessage.setMessage("Created new product",
					HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProductController.class).withSelfRel());
			// resource.add(linkTo(ProducerDirectorController.class).withRel("product_director"));

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	/**
	 * El método PATCH solicita que se aplique un conjunto de cambios descritos en
	 * la entidad de solicitud al recurso identificado por el URI de Solicitud.
	 * 
	 * @param id                product identifier
	 * @param productRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/products/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Update the product.")
	public ResponseEntity<?> updateProducts(@PathVariable("id") int id,
			@RequestBody ProductRequestDTO productRequestDTO) {

		logger.info("Process patch product");

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find product by id for check if exists in DB
			ProductResponseDTO productResponseDTO = productService.getProduct(id);

			// If exists
			if (productResponseDTO != null) {

				customMessageList = ArrayListCustomMessage.setMessage("Patch product process", HttpStatus.OK);

				// Set the product id to the wanted request object
				productRequestDTO.setId(id);

				productService.updateProduct(productRequestDTO);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Product Id" + id + " Not Found!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(ProductController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(ProductController.class).slash(id).withSelfRel());
			// resource.add(linkTo(ProducerDirectorController.class).withRel("product_director"));

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}
