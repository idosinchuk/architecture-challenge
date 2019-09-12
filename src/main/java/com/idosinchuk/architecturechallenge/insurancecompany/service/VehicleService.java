package com.idosinchuk.architecturechallenge.insurancecompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.idosinchuk.architecturechallenge.insurancecompany.dto.VehicleRequestDTO;
import com.idosinchuk.architecturechallenge.insurancecompany.dto.VehicleResponseDTO;

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
	 * Find vehicle by the licensePlate.
	 * 
	 * @param licensePlate vehicle license plate
	 * @return {@link VehicleResponseDTO}
	 */
	VehicleResponseDTO getVehicle(String licensePlate);

	/**
	 * Add a vehicle..
	 * 
	 * @param vehicleRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> addVehicle(VehicleRequestDTO vehicleRequestDTO);

	/**
	 * Update the vehicle.
	 * 
	 * @param licensePlate      vehicle license plate
	 * @param vehicleRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> updateVehicle(String licensePlate, VehicleRequestDTO vehicleRequestDTO);
}
