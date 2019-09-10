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
import com.idosinchuk.insurancecompany.controller.VehicleController;
import com.idosinchuk.insurancecompany.dto.VehicleRequestDTO;
import com.idosinchuk.insurancecompany.dto.VehicleResponseDTO;
import com.idosinchuk.insurancecompany.entity.VehicleEntity;
import com.idosinchuk.insurancecompany.repository.VehicleRepository;
import com.idosinchuk.insurancecompany.service.VehicleService;
import com.idosinchuk.insurancecompany.util.ArrayListCustomMessage;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

/**
 * Implementation for vehicle service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("VehicleService")
public class VehicleServiceImpl implements VehicleService {

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private ModelMapper modelMapper;

	public static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public Page<VehicleResponseDTO> getAllVehicles(Pageable pageable) {

		Page<VehicleEntity> entityResponse = vehicleRepository.findAll(pageable);

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<Page<VehicleResponseDTO>>() {
		}.getType());

	}

	/**
	 * {@inheritDoc}
	 */
	public VehicleResponseDTO getVehicle(String licensePlate) {

		VehicleEntity entityResponse = vehicleRepository.findByLicensePlate(licensePlate);

		return modelMapper.map(entityResponse, VehicleResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> addVehicle(VehicleRequestDTO vehicleRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			// Check if vehicle license plate exists in DB
			VehicleEntity vehicleEntity = vehicleRepository.findByLicensePlate(vehicleRequestDTO.getLicensePlate());

			// If exists
			if (vehicleEntity != null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"Vehicle license plate" + vehicleRequestDTO.getLicensePlate() + " already exists in database!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(VehicleController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			VehicleEntity entityRequest = modelMapper.map(vehicleRequestDTO, VehicleEntity.class);

			vehicleRepository.save(entityRequest);

			customMessageList = ArrayListCustomMessage.setMessage("Created new vehicle", HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(VehicleController.class).withSelfRel());

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
	public ResponseEntity<?> updateVehicle(String licensePlate, VehicleRequestDTO vehicleRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find vehicle by licensePlate for check if exists in DB
			VehicleEntity vehicleEntity = vehicleRepository.findByLicensePlate(licensePlate);

			// If exists
			if (vehicleEntity != null) {

				customMessageList = ArrayListCustomMessage.setMessage("Patch vehicle process", HttpStatus.OK);

				// The vehicle id and license plate will always be the same, so we do not allow
				// it to be
				// updated, for them we overwrite the field with the original value.
				vehicleRequestDTO.setLicensePlate(licensePlate);
				vehicleRequestDTO.setId(vehicleEntity.getId());

				VehicleEntity entityRequest = modelMapper.map(vehicleRequestDTO, VehicleEntity.class);
				vehicleRepository.save(entityRequest);

			} else {
				customMessageList = ArrayListCustomMessage.setMessage("License plate " + licensePlate + " Not Found!",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(VehicleController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(VehicleController.class).slash(licensePlate).withSelfRel());

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}
}
