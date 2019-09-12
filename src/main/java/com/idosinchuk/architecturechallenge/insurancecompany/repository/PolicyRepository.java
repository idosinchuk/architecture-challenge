package com.idosinchuk.architecturechallenge.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.architecturechallenge.insurancecompany.entity.PolicyEntity;

/**
 * Repository for policy
 * 
 * @author Igor Dosinchuk
 *
 */
public interface PolicyRepository extends JpaRepository<PolicyEntity, Integer> {

	PolicyEntity findByPolicyCode(String policyCode);

}
