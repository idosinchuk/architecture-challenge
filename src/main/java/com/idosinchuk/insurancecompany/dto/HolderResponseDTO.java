package com.idosinchuk.insurancecompany.dto;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Response DTO for holder
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "HolderResponse", description = "Model response for holder.")
public class HolderResponseDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@ApiModelProperty(value = "Holder name", example = "Igor")
	private String holderName;

	@ApiModelProperty(value = "Holder surname", example = "Dosinchuk")
	private String holderSurname;

	@ApiModelProperty(value = "Phone number", example = "987654321")
	private String phoneNumber;

	@ApiModelProperty(value = "Email", example = "idosinchuk@example.com")
	private String email;

	@ApiModelProperty(value = "Passport number", example = "PS9393474S")
	private String passportNumber;
}
