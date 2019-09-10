package com.idosinchuk.insurancecompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.idosinchuk.insurancecompany.dto.HolderRequestDTO;
import com.idosinchuk.insurancecompany.dto.HolderResponseDTO;

/**
 * 
 * Service for holder
 * 
 * @author Igor Dosinchuk
 *
 */
public interface HolderService {

	/**
	 * Retrieve list of all holders according to the search criteria.
	 * 
	 * @param pageable object for pagination
	 * @return Page of {@link HolderResponseDTO}
	 */
	Page<HolderResponseDTO> getAllHolders(Pageable pageable);

	/**
	 * Find holder by the id.
	 * 
	 * @param passportNumber holder passport number
	 * @return {@link HolderResponseDTO}
	 */
	HolderResponseDTO getHolder(String passportNumber);

	/**
	 * Add a holder..
	 * 
	 * @param holderRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> addHolder(HolderRequestDTO holderRequestDTO);

	/**
	 * Update the holder.
	 * 
	 * @param passportNumber   holder passport number
	 * @param holderRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> updateHolder(String passportNumber, HolderRequestDTO holderRequestDTO);
}
