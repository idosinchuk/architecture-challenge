package com.idosinchuk.architecturechallenge.insurancecompany.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.idosinchuk.architecturechallenge.insurancecompany.dto.PolicyRequestDTO;
import com.idosinchuk.architecturechallenge.insurancecompany.dto.PolicyResponseDTO;

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
	 * Find policies by the policyCode.
	 * 
	 * @param policyCode policy code
	 * @return {@link PolicyResponseDTO}
	 */
	PolicyResponseDTO getPolicies(String policyCode);

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
	 * @param policyCode       policy code
	 * @param policyRequestDTO object to save
	 * 
	 * @return ResponseEntity
	 */
	ResponseEntity<?> updatePolicy(String policyCode, PolicyRequestDTO policyRequestDTO);
}
