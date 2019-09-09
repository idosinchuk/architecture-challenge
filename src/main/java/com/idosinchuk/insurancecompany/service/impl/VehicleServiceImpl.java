package com.idosinchuk.insurancecompany.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.idosinchuk.insurancecompany.dto.VehicleRequestDTO;
import com.idosinchuk.insurancecompany.dto.VehicleResponseDTO;
import com.idosinchuk.insurancecompany.entity.VehicleEntity;
import com.idosinchuk.insurancecompany.repository.VehicleRepository;
import com.idosinchuk.insurancecompany.service.VehicleService;

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
	public VehicleResponseDTO getVehicle(int id) {

		VehicleEntity entityResponse = vehicleRepository.findById(id);

		return modelMapper.map(entityResponse, VehicleResponseDTO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public VehicleResponseDTO addVehicle(VehicleRequestDTO vehicleRequestDTO) {

		VehicleEntity entityRequest = modelMapper.map(vehicleRequestDTO, VehicleEntity.class);

		VehicleEntity entityResponse = vehicleRepository.save(entityRequest);

		return modelMapper.map(entityResponse, VehicleResponseDTO.class);

	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public VehicleResponseDTO updateVehicle(VehicleRequestDTO vehicleRequestDTO) {

		VehicleEntity entityRequest = modelMapper.map(vehicleRequestDTO, VehicleEntity.class);

		VehicleEntity entityResponse = vehicleRepository.save(entityRequest);

		return modelMapper.map(entityResponse, VehicleResponseDTO.class);

	}
}
