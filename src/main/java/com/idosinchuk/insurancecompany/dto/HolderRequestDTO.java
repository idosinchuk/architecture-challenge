package com.idosinchuk.insurancecompany.dto;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for holder
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "HolderRequest", description = "Model request for holder.")
public class HolderRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@NotNull
	@ApiModelProperty(value = "Holder name", example = "Igor")
	private String holderName;

	@NotNull
	@ApiModelProperty(value = "Holder surname", example = "Dosinchuk")
	private String holderSurname;

	@NotNull
	@ApiModelProperty(value = "Phone number", example = "987654321")
	private String phoneNumber;

	@NotNull
	@ApiModelProperty(value = "Email", example = "idosinchuk@example.com")
	private String email;

	@NotNull
	@ApiModelProperty(value = "Passport number", example = "PS9393474S")
	private String passportNumber;
}
