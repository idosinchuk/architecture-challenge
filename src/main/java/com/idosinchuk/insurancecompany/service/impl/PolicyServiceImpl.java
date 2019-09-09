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
import com.idosinchuk.insurancecompany.controller.PolicyController;
import com.idosinchuk.insurancecompany.dto.PolicyRequestDTO;
import com.idosinchuk.insurancecompany.dto.PolicyResponseDTO;
import com.idosinchuk.insurancecompany.entity.HolderEntity;
import com.idosinchuk.insurancecompany.entity.PolicyEntity;
import com.idosinchuk.insurancecompany.entity.ProductEntity;
import com.idosinchuk.insurancecompany.entity.VehicleEntity;
import com.idosinchuk.insurancecompany.repository.HolderRepository;
import com.idosinchuk.insurancecompany.repository.PolicyRepository;
import com.idosinchuk.insurancecompany.repository.ProductRepository;
import com.idosinchuk.insurancecompany.repository.VehicleRepository;
import com.idosinchuk.insurancecompany.service.PolicyService;
import com.idosinchuk.insurancecompany.util.ArrayListCustomMessage;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

/**
 * Implementation for policy service
 * 
 * @author Igor Dosinchuk
 *
 */
@Service("PolicyService")
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private HolderRepository holderRepository;

	@Autowired
	private ModelMapper modelMapper;

	public static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

	/**
	 * {@inheritDoc}
	 */
	public Page<PolicyResponseDTO> getAllPolicies(Pageable pageable) {

		logger.info("Fetching all policies");

		Page<PolicyEntity> entityResponse = policyRepository.findAll(pageable);

		// Convert Entity response to DTO
		return modelMapper.map(entityResponse, new TypeToken<Page<PolicyResponseDTO>>() {
		}.getType());

	}

	/**
	 * {@inheritDoc}
	 */
	public ResponseEntity<?> getPolicies(int id) {

		PolicyResponseDTO policyResponseDTO = null;

		try {
			PolicyEntity entityResponse = policyRepository.findById(id);

			policyResponseDTO = modelMapper.map(entityResponse, PolicyResponseDTO.class);

			return new ResponseEntity<>(policyResponseDTO, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public ResponseEntity<?> addPolicy(PolicyRequestDTO policyRequestDTO) {

		logger.info(("Process add new policy"));

		Resources<CustomMessage> resource = null;

		try {
			PolicyEntity entityRequest = modelMapper.map(policyRequestDTO, PolicyEntity.class);

			PolicyEntity policyEntity = policyRepository.findByPolicyCode(policyRequestDTO.getPolicyCode());

			// Check if policyCode exists in the database
			if (policyEntity.getPolicyCode() != null) {
				List<CustomMessage> customMessageList = ArrayListCustomMessage.setMessage(
						"The requested policy actually exists in database with the same policyCode",
						HttpStatus.BAD_REQUEST);
				resource = new Resources<>(customMessageList);
				resource.add(linkTo(PolicyController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			// Check if product exists in the database
			ProductEntity productEntity = productRepository.findById(policyRequestDTO.getProductId());

			if (productEntity != null) {
				entityRequest.setProduct(productEntity);
			}

			// Check if holder exists in the database
			HolderEntity holderEntity = holderRepository.findById(policyRequestDTO.getHolderId());

			if (holderEntity != null) {
				entityRequest.setHolder(holderEntity);
			}

			// Check if vehicle exists in the database
			VehicleEntity vehicleEntity = vehicleRepository.findById(policyRequestDTO.getVehicleId());

			if (vehicleEntity != null) {
				entityRequest.setVehicle(vehicleEntity);
			}

			PolicyEntity entityResponse = policyRepository.save(entityRequest);

			modelMapper.map(entityResponse, PolicyResponseDTO.class);

			List<CustomMessage> customMessageList = ArrayListCustomMessage.setMessage("Created new policy",
					HttpStatus.CREATED);

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(PolicyController.class).withSelfRel());
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
	public ResponseEntity<?> updatePolicy(PolicyRequestDTO policyRequestDTO) {

		logger.info("Process patch policy");

		Resources<CustomMessage> resource = null;

		try {

			List<CustomMessage> customMessageList = null;

			// Find policy by id for check if exists in DB
			PolicyEntity policyEntity = policyRepository.findById(policyRequestDTO.getId());

			PolicyResponseDTO policyResponseDTO = modelMapper.map(policyEntity, PolicyResponseDTO.class);

			// If exists
			if (policyResponseDTO != null) {
				customMessageList = ArrayListCustomMessage.setMessage("Patch policy process", HttpStatus.OK);

				// The policy's code will always be the same, so we do not allow it to be
				// updated, for them we overwrite the field with the original value.
				policyRequestDTO.setPolicyCode(policyResponseDTO.getPolicyCode());

				PolicyEntity entityRequest = modelMapper.map(policyRequestDTO, PolicyEntity.class);

				if (policyRequestDTO.getProductId() != 0) {
					ProductEntity productEntity = productRepository.findById(policyRequestDTO.getProductId());

					// Check if product exists in the database
					if (productEntity != null) {
						entityRequest.setProduct(productEntity);
					} else {
						customMessageList = ArrayListCustomMessage.setMessage(
								"Product ID " + policyRequestDTO.getVehicleId() + " does not exist!",
								HttpStatus.BAD_REQUEST);
						resource = new Resources<>(customMessageList);
						resource.add(linkTo(PolicyController.class).withSelfRel());
						return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
					}
				}

				if (policyRequestDTO.getHolderId() != 0) {
					HolderEntity holderEntity = holderRepository.findById(policyRequestDTO.getHolderId());

					// Check if holder exists in the database
					if (holderEntity != null) {
						entityRequest.setHolder(holderEntity);
					} else {
						customMessageList = ArrayListCustomMessage.setMessage(
								"Holder ID " + policyRequestDTO.getVehicleId() + " does not exist!",
								HttpStatus.BAD_REQUEST);
						resource = new Resources<>(customMessageList);
						resource.add(linkTo(PolicyController.class).withSelfRel());
						return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
					}
				}

				if (policyRequestDTO.getVehicleId() != 0) {
					VehicleEntity vehicleEntity = vehicleRepository.findById(policyRequestDTO.getVehicleId());

					// Check if vehicle exists in the database
					if (vehicleEntity != null) {
						entityRequest.setVehicle(vehicleEntity);
					} else {
						customMessageList = ArrayListCustomMessage.setMessage(
								"Vehicle ID " + policyRequestDTO.getVehicleId() + " does not exist!",
								HttpStatus.BAD_REQUEST);
						resource = new Resources<>(customMessageList);
						resource.add(linkTo(PolicyController.class).withSelfRel());
						return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
					}
				}

				PolicyEntity entityResponse = policyRepository.save(entityRequest);

				modelMapper.map(entityResponse, PolicyResponseDTO.class);

			} else {
				customMessageList = ArrayListCustomMessage
						.setMessage("Policy ID" + policyRequestDTO.getId() + " Not Found!", HttpStatus.BAD_REQUEST);

				resource = new Resources<>(customMessageList);
				resource.add(linkTo(PolicyController.class).withSelfRel());

				return new ResponseEntity<>(resource, HttpStatus.BAD_REQUEST);
			}

			resource = new Resources<>(customMessageList);
			resource.add(linkTo(PolicyController.class).slash(policyRequestDTO.getId()).withSelfRel());
		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}

		return new ResponseEntity<>(resource, HttpStatus.OK);

	}
}
