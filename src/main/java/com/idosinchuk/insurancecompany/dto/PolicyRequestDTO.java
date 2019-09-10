package com.idosinchuk.insurancecompany.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for producer
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProducerRequest", description = "Model request for manage producer.")
public class PolicyRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@NotNull
	@ApiModelProperty(value = "Policy code", example = "RJHD21JD")
	private String policyCode;

	@NotNull
	@ApiModelProperty(value = "Policy cost", example = "100")
	private BigDecimal cost;

	@NotNull
	@ApiModelProperty(value = "Product code", example = "1")
	private String productCode;

	@NotNull
	@ApiModelProperty(value = "Passport number", example = "PS9393474S")
	private String passportNumber;

	@NotNull
	@ApiModelProperty(value = "License Plate", example = "1")
	private String licensePlate;

}
