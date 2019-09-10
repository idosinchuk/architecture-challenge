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
import com.idosinchuk.insurancecompany.controller.HolderController;
import com.idosinchuk.insurancecompany.controller.ProductController;
import com.idosinchuk.insurancecompany.dto.HolderRequestDTO;
import com.idosinchuk.insurancecompany.dto.HolderResponseDTO;
import com.idosinchuk.insurancecompany.entity.HolderEntity;
import com.idosinchuk.insurancecompany.entity.HolderHistoricalEntity;
import com.idosinchuk.insurancecompany.repository.HolderHistoricalRepository;
import com.idosinchuk.insurancecompany.repository.HolderRepository;
import com.idosinchuk.insurancecompany.service.HolderService;
import com.idosinchuk.insurancecompany.util.ArrayListCustomMessage;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

/**
 * Implementation for holder service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("HolderService")
public class HolderServiceImpl implements HolderService {

	@Autowired
	private HolderRepository holderRepository;

	@Autowired
	private HolderHistoricalRepository holderHistoricalRepository;

	@Autowired
	private ModelMapper modelMapper;

	public static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	/**
	 * {@inheritDoc}
	 */
	public Page<HolderResponseDTO> getAllHolders(Pageable pageable) {

		Page<HolderEntity> entityResponse = holderRepository.findAll(pageable);

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<Page<HolderResponseDTO>>() {
		}.getType());

	}

	/**
	 * {@inheritDoc}
	 */
	public HolderResponseDTO getHolder(String passportNumber) {

		HolderEntity entityResponse = holderRepository.findByPassportNumber(passportNumber);

		return modelMapper.map(entityResponse, HolderResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> addHolder(HolderRequestDTO holderRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {
			List<CustomMessage> customMessageList = null;

			// Check if productCode exists in DB
			HolderEntity holderEntity = holderRepository.findByPassportNumber(holderRequestDTO.getPassportNumber());

			// If exists
			if (holderEntity != null) {
				customMessageList = ArrayListCustomMessage.setMessage(
						"Passport number" + holderRequestDTO.getPassportNumber() + " already exists.",
						HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(HolderController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			HolderEntity entityRequest = modelMapper.map(holderRequestDTO, HolderEntity.class);

			holderRepository.save(entityRequest);

			customMessageList = ArrayListCustomMessage.setMessage("Created new holder", HttpStatus.CREATED);

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
	public ResponseEntity<?> updateHolder(String passportNumber, HolderRequestDTO holderRequestDTO) {

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find product by passportNumber for check if exists in DB
			HolderEntity holderEntity = holderRepository.findByPassportNumber(passportNumber);

			// If exists
			if (holderEntity != null) {

				customMessageList = ArrayListCustomMessage.setMessage("Patch holder process", HttpStatus.OK);

				// The passport number will always be the same, so we do not allow it to be
				// updated, for them we overwrite the field with the original value.
				holderRequestDTO.setPassportNumber(passportNumber);
				holderRequestDTO.setId(holderEntity.getId());

				HolderEntity entityRequest = modelMapper.map(holderRequestDTO, HolderEntity.class);

				// Check if there are changes
				if (!holderEntity.equals(entityRequest)) {
					holderRepository.save(entityRequest);

					// Save the holder information in a historical table
					HolderHistoricalEntity holderHistoricalEntity = modelMapper.map(holderRequestDTO,
							HolderHistoricalEntity.class);
					holderHistoricalRepository.save(holderHistoricalEntity);
				} else {
					customMessageList = ArrayListCustomMessage.setMessage("There are no changes, please try again",
							HttpStatus.BAD_REQUEST);

					resource = new Resources<>(customMessageList);
					resource.add(linkTo(HolderController.class).withSelfRel());

					return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
				}
			} else {
				customMessageList = ArrayListCustomMessage
						.setMessage("Passport number " + passportNumber + " Not Found!", HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(HolderController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(HolderController.class).slash(passportNumber).withSelfRel());

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}
}
