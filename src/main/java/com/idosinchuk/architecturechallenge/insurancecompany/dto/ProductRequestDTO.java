package com.idosinchuk.architecturechallenge.insurancecompany.dto;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Request DTO for product
 * 
 * @author Igor Dosinchuk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ApiModel(reference = "ProductRequest", description = "Model request for product.")
public class ProductRequestDTO {

	@Id
	@ApiModelProperty(value = "Id", example = "1")
	private int id;

	@NotNull
	@ApiModelProperty(value = "Product", example = "Full of risk")
	private String productName;

	@NotNull
	@ApiModelProperty(value = "Code of the product", example = "S6DHD78S")
	private String productCode;

}
