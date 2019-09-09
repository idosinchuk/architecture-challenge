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

	@ApiModelProperty(value = "Product", example = "")
	private ProductResponseDTO product;

	@ApiModelProperty(value = "Holder", example = "")
	private HolderResponseDTO holder;

	@ApiModelProperty(value = "Vehicle", example = "")
	private VehicleResponseDTO vehicle;

}
