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
import com.idosinchuk.insurancecompany.dto.VehicleRequestDTO;
import com.idosinchuk.insurancecompany.dto.VehicleResponseDTO;
import com.idosinchuk.insurancecompany.service.VehicleService;
import com.idosinchuk.insurancecompany.util.ArrayListCustomMessage;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for insurance vehicles
 * 
 * @author Igor Dosinchuk
 *
 */
@RestController
@Api(value = "API Rest for insurance vehicles.")
@RequestMapping(value = "/api/v1")
public class VehicleController {

	public static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

	@Autowired
	VehicleService vehicleService;

	/**
	 * Retrieve list of all vehicles according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all vehicles, headers and status
	 */
	@GetMapping(path = "/vehicles")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all vehicles according to the search criteria.")
	public ResponseEntity<PagedResources<VehicleResponseDTO>> getAllVehicles(Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler, @RequestHeader("User-Agent") String userAgent) {

		logger.info("Fetching all vehicles");

		Page<VehicleResponseDTO> vehicle = null;

		try {
			// Find vehicles in DB with paging filters
			vehicle = vehicleService.getAllVehicles(pageable);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.put(HttpHeaders.USER_AGENT, Arrays.asList(userAgent));

		PagedResources<VehicleResponseDTO> pagedResources = pagedResourcesAssembler.toResource(vehicle);

		return new ResponseEntity<>(pagedResources, headers, HttpStatus.OK);
	}

	/**
	 * Retrieve vehicle by the id.
	 * 
	 * @param id vehicle identifier
	 * @return ResponseEntity with status and vehicleResponseDTO
	 */
	@GetMapping(path = "/vehicles/{id}")
	@ResponseBody
	@ApiOperation(value = "Retrieve vehicle by the id.")
	public ResponseEntity<?> getVehicles(@PathVariable("id") int id) {

		logger.info("Fetching vehicle with ID {}", id);

		VehicleResponseDTO vehicle = null;

		try {
			// Search vehicle in BD by ID
			vehicle = vehicleService.getVehicle(id);

			return new ResponseEntity<>(vehicle, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

	}

	/**
	 * Add a vehicle.
	 * 
	 * @param vehicleRequestDTO object to save
	 * @return ResponseEntity with status and vehicleResponseDTO
	 */
	@PostMapping(path = "/vehicles", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Add a vehicle.")
	public ResponseEntity<?> addVehicles(@RequestBody VehicleRequestDTO vehicleRequestDTO) {

		logger.info(("Process add new vehicle"));

		Resources<CustomMessage> resource = null;

		try {

			vehicleService.addVehicle(vehicleRequestDTO);

			List<CustomMessage> customMessageList = ArrayListCustomMessage.setMessage("Created new vehicle",
					HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(VehicleController.class).withSelfRel());
			// resource.add(linkTo(ProducerDirectorController.class).withRel("vehicle_director"));

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
	 * @param id                vehicle identifier
	 * @param vehicleRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/vehicles/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Update the vehicle.")
	public ResponseEntity<?> updateVehicles(@PathVariable("id") int id,
			@RequestBody VehicleRequestDTO vehicleRequestDTO) {

		logger.info("Process patch vehicle");

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find vehicle by id for check if exists in DB
			VehicleResponseDTO vehicleResponseDTO = vehicleService.getVehicle(id);

			// If exists
			if (vehicleResponseDTO != null) {

				customMessageList = ArrayListCustomMessage.setMessage("Patch vehicle process", HttpStatus.OK);

				// Set the vehicle id to the wanted request object
				vehicleRequestDTO.setId(id);

				vehicleService.updateVehicle(vehicleRequestDTO);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("Vehicle Id" + id + " Not Found!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(VehicleController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(VehicleController.class).slash(id).withSelfRel());
			// resource.add(linkTo(ProducerDirectorController.class).withRel("vehicle_director"));

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}
