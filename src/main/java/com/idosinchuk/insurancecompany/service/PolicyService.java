package com.idosinchuk.insurancecompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.idosinchuk.insurancecompany.dto.PolicyRequestDTO;
import com.idosinchuk.insurancecompany.dto.PolicyResponseDTO;

/**
 * 
 * Service for policy
 * 
 * @author Igor Dosinchuk
 *
 */
public interface PolicyService {

	/**
	 * Retrieve list of all policies according to the search criteria.
	 * 
	 * @param pageable object for pagination
	 * @return Page of {@link PolicyResponseDTO}
	 */
	Page<PolicyResponseDTO> getAllPolicies(Pageable pageable);

	/**
	 * Find policy by the id.
	 * 
	 * @param id policy identifier
	 * @return {@link PolicyResponseDTO}
	 */
	PolicyResponseDTO getPolicy(int id);

	/**
	 * Add a policy..
	 * 
	 * @param policyRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> addPolicy(PolicyRequestDTO policyRequestDTO);

	/**
	 * Update the policy.
	 * 
	 * @param policyRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> updatePolicy(PolicyRequestDTO policyRequestDTO);
}
