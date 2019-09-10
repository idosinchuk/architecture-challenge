package com.idosinchuk.insurancecompany.dto;

import java.math.BigDecimal;

import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for producer
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProducerResponse", description = "Model response for manage producer.")
public class PolicyResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Policy code", example = "RJHD21JD")
	private String policyCode;

	@ApiModelProperty(value = "Policy cost", example = "100")
	private BigDecimal cost;

	@ApiModelProperty(value = "Product code", example = "1")
	private ProductResponseDTO product;

	@ApiModelProperty(value = "Passport number", example = "PS9393474S")
	private HolderResponseDTO holder;

	@ApiModelProperty(value = "License Plate", example = "6846JNR")
	private VehicleResponseDTO vehicle;

}
