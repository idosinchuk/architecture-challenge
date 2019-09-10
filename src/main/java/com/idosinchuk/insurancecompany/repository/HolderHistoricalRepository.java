package com.idosinchuk.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.insurancecompany.entity.HolderHistoricalEntity;

/**
 * Repository for holder historical
 * 
 * @author Igor Dosinchuk
 *
 */
public interface HolderHistoricalRepository extends JpaRepository<HolderHistoricalEntity, Integer> {

	HolderHistoricalEntity findByPassportNumber(String passportNumber);

}
