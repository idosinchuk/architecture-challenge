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
import com.idosinchuk.insurancecompany.dto.HolderRequestDTO;
import com.idosinchuk.insurancecompany.dto.HolderResponseDTO;
import com.idosinchuk.insurancecompany.service.HolderService;
import com.idosinchuk.insurancecompany.util.ArrayListCustomMessage;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for insurance holders
 * 
 * @author Igor Dosinchuk
 *
 */
@RestController
@Api(value = "API Rest for insurance holders.")
@RequestMapping(value = "/api/v1")
public class HolderController {

	public static final Logger logger = LoggerFactory.getLogger(HolderController.class);

	@Autowired
	HolderService holderService;

	/**
	 * Retrieve list of all holders according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all holders, headers and status
	 */
	@GetMapping(path = "/holders")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all holders according to the search criteria.")
	public ResponseEntity<PagedResources<HolderResponseDTO>> getAllHolders(Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler, @RequestHeader("User-Agent") String userAgent) {

		logger.info("Fetching all holders");

		Page<HolderResponseDTO> holder = null;

		try {
			// Find holders in DB with paging filters
			holder = holderService.getAllHolders(pageable);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.put(HttpHeaders.USER_AGENT, Arrays.asList(userAgent));

		PagedResources<HolderResponseDTO> pagedResources = pagedResourcesAssembler.toResource(holder);

		return new ResponseEntity<>(pagedResources, headers, HttpStatus.OK);
	}

	/**
	 * Retrieve holder by the id.
	 * 
	 * @param id holder identifier
	 * @return ResponseEntity with status and holderResponseDTO
	 */
	@GetMapping(path = "/holders/{id}")
	@ResponseBody
	@ApiOperation(value = "Retrieve holder by the id.")
	public ResponseEntity<?> getHolders(@PathVariable("id") int id) {

		logger.info("Fetching holder with ID {}", id);

		HolderResponseDTO holder = null;

		try {
			// Search holder in BD by ID
			holder = holderService.getHolder(id);

			return new ResponseEntity<>(holder, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

	}

	/**
	 * Add a holder.
	 * 
	 * @param holderRequestDTO object to save
	 * @return ResponseEntity with status and holderResponseDTO
	 */
	@PostMapping(path = "/holders", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Add a holder.")
	public ResponseEntity<?> addHolders(@RequestBody HolderRequestDTO holderRequestDTO) {

		logger.info(("Process add new holder"));

		Resources<CustomMessage> resource = null;

		try {

			holderService.addHolder(holderRequestDTO);

			List<CustomMessage> customMessageList = ArrayListCustomMessage.setMessage("Created new holder",
					HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(HolderController.class).withSelfRel());
			// resource.add(linkTo(ProducerDirectorController.class).withRel("holder_director"));

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
	 * @param id               holder identifier
	 * @param holderRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/holders/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Update the holder.")
	public ResponseEntity<?> updateHolders(@PathVariable("id") int id, @RequestBody HolderRequestDTO holderRequestDTO) {

		logger.info("Process patch holder");

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find holder by id for check if exists in DB
			HolderResponseDTO holderResponseDTO = holderService.getHolder(id);

			// If exists
			if (holderResponseDTO != null) {

				customMessageList = ArrayListCustomMessage.setMessage("Patch holder process", HttpStatus.OK);

				// Set the holder id to the wanted request object
				holderRequestDTO.setId(id);

				holderService.updateHolder(holderRequestDTO);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Holder Id" + id + " Not Found!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(HolderController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(HolderController.class).slash(id).withSelfRel());
			// resource.add(linkTo(ProducerDirectorController.class).withRel("holder_director"));

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}
