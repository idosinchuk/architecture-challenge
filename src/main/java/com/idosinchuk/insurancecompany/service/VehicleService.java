package com.idosinchuk.insurancecompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.idosinchuk.insurancecompany.dto.VehicleRequestDTO;
import com.idosinchuk.insurancecompany.dto.VehicleResponseDTO;

/**
 * 
 * Service for vehicle
 * 
 * @author Igor Dosinchuk
 *
 */
public interface VehicleService {

	/**
	 * Retrieve list of all vehicles according to the search criteria.
	 * 
	 * @param pageable object for pagination
	 * @return Page of {@link VehicleResponseDTO}
	 */
	Page<VehicleResponseDTO> getAllVehicles(Pageable pageable);

	/**
	 * Find vehicle by the id.
	 * 
	 * @param id vehicle identifier
	 * @return {@link VehicleResponseDTO}
	 */
	VehicleResponseDTO getVehicle(int id);

	/**
	 * Add a vehicle..
	 * 
	 * @param vehicleRequestDTO object to save
	 * 
	 * @return {@link VehicleResponseDTO}
	 */
	VehicleResponseDTO addVehicle(VehicleRequestDTO vehicleRequestDTO);

	/**
	 * Update the vehicle.
	 * 
	 * @param vehicleRequestDTO object to save
	 * 
	 * @return {@link VehicleResponseDTO}
	 */
	VehicleResponseDTO updateVehicle(VehicleRequestDTO vehicleRequestDTO);
}
