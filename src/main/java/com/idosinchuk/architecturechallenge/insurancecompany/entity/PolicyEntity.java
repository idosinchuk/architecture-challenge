package com.idosinchuk.architecturechallenge.insurancecompany.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity for Producer
 * 
 * @author Igor Dosinchuk
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@NoArgsConstructor
@Data
@Table(name = "policy")
public class PolicyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "policy_code", nullable = false)
	private String policyCode;

	@Column(name = "cost", nullable = false)
	private BigDecimal cost;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private ProductEntity product;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "holder_id", referencedColumnName = "id")
	private HolderEntity holder;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "vehicle_id", referencedColumnName = "id")
	private VehicleEntity vehicle;

}
