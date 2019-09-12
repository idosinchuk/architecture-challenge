package com.idosinchuk.architecturechallenge.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.architecturechallenge.insurancecompany.entity.HolderHistoricalEntity;

/**
 * Repository for holder historical
 * 
 * @author Igor Dosinchuk
 *
 */
public interface HolderHistoricalRepository extends JpaRepository<HolderHistoricalEntity, Integer> {

	HolderHistoricalEntity findByPassportNumber(String passportNumber);

}
