package com.idosinchuk.architecturechallenge.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.architecturechallenge.insurancecompany.entity.HolderEntity;

/**
 * Repository for holder
 * 
 * @author Igor Dosinchuk
 *
 */
public interface HolderRepository extends JpaRepository<HolderEntity, Integer> {

	HolderEntity findByPassportNumber(String passportNumber);

}
