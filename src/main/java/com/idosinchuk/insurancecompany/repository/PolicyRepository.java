package com.idosinchuk.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.insurancecompany.entity.PolicyEntity;

/**
 * Repository for policy
 * 
 * @author Igor Dosinchuk
 *
 */
public interface PolicyRepository extends JpaRepository<PolicyEntity, Integer> {

	PolicyEntity findById(int id);

	PolicyEntity findByPolicyCode(String policyCode);

}
