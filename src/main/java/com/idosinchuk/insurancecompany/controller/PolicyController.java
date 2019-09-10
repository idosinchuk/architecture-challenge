package com.idosinchuk.insurancecompany.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.idosinchuk.insurancecompany.dto.PolicyRequestDTO;
import com.idosinchuk.insurancecompany.dto.PolicyResponseDTO;
import com.idosinchuk.insurancecompany.service.PolicyService;
import com.idosinchuk.insurancecompany.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller for insurance policies
 * 
 * @author Igor Dosinchuk
 *
 */
@RestController
@Api(value = "API Rest for insurance policies.")
@RequestMapping(value = "/api/v1")
public class PolicyController {

	public static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

	@Autowired
	PolicyService policyService;

	/**
	 * Retrieve list of all policies according to the search criteria.
	 * 
	 * @param pageable paging fields
	 * @return ResponseEntity with paged list of all policies, headers and status
	 */
	@GetMapping(path = "/policies")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@ApiOperation(value = "Retrieve list of all policies according to the search criteria.")
	public ResponseEntity<PagedResources<PolicyResponseDTO>> getAllPolicies(Pageable pageable,
			PagedResourcesAssembler pagedResourcesAssembler, @RequestHeader("User-Agent") String userAgent) {

		logger.info("Fetching all policies");

		Page<PolicyResponseDTO> policy = null;

		try {
			// Find policies in DB with paging filters
			policy = policyService.getAllPolicies(pageable);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());
		}

		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.put(HttpHeaders.USER_AGENT, Arrays.asList(userAgent));

		PagedResources<PolicyResponseDTO> pagedResources = pagedResourcesAssembler.toResource(policy);

		return new ResponseEntity<>(pagedResources, headers, HttpStatus.OK);
	}

	/**
	 * Retrieve policy by the policyCode.
	 * 
	 * @param policyCode policy code
	 * @return ResponseEntity with status and policyResponseDTO
	 */
	@GetMapping(path = "/policies/{policyCode}")
	@ResponseBody
	@ApiOperation(value = "Retrieve policy by the policyCode.")
	public ResponseEntity<?> getPolicies(@PathVariable("policyCode") String policyCode) {

		logger.info("Fetching product with policyCode {}", policyCode);

		PolicyResponseDTO policyResponseDTO = null;

		try {
			// Search product in BD by policyCode
			policyResponseDTO = policyService.getPolicies(policyCode);

			return new ResponseEntity<>(policyResponseDTO, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("An error occurred! {}", e.getMessage());
			return CustomErrorType.returnResponsEntityError(e.getMessage());

		}
	}

	/**
	 * Add a policy.
	 * 
	 * @param policyRequestDTO object to save
	 * @return ResponseEntity with status and policyResponseDTO
	 */
	@PostMapping(path = "/policies", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Add a policy.")
	public ResponseEntity<?> addPolicies(@RequestBody PolicyRequestDTO policyRequestDTO) {

		logger.info("Process add policy");

		return policyService.addPolicy(policyRequestDTO);

	}

	/**
	 * Update a policy
	 * 
	 * @param policyCode       policy code
	 * @param policyRequestDTO object to update
	 * @return ResponseEntity with resource and status
	 */
	@PatchMapping(path = "/policies/{policyCode}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@ApiOperation(value = "Update the policy.")
	public ResponseEntity<?> updatePolicies(@PathVariable("policyCode") String policyCode,
			@RequestBody PolicyRequestDTO policyRequestDTO) {

		logger.info("Process patch policy");

		return policyService.updatePolicy(policyCode, policyRequestDTO);
	}
}
