package com.idosinchuk.architecturechallenge.insurancecompany.dto;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for vehicle
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "VehicleResponse", description = "Model response for vehicle.")
public class VehicleResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@NotNull
	@ApiModelProperty(value = "Brand", example = "Jaguar")
	private String brand;

	@NotNull
	@ApiModelProperty(value = "License plate", example = "6846JNR")
	private String licensePlate;

}
