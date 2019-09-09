package com.idosinchuk.insurancecompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	 * @param id holder identifier
	 * @return {@link HolderResponseDTO}
	 */
	HolderResponseDTO getHolder(int id);

	/**
	 * Add a holder..
	 * 
	 * @param holderRequestDTO object to save
	 * 
	 * @return {@link HolderResponseDTO}
	 */
	HolderResponseDTO addHolder(HolderRequestDTO holderRequestDTO);

	/**
	 * Update the holder.
	 * 
	 * @param holderRequestDTO object to save
	 * 
	 * @return {@link HolderResponseDTO}
	 */
	HolderResponseDTO updateHolder(HolderRequestDTO holderRequestDTO);
}
