package com.idosinchuk.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.insurancecompany.entity.HolderEntity;

/**
 * Repository for holder
 * 
 * @author Igor Dosinchuk
 *
 */
public interface HolderRepository extends JpaRepository<HolderEntity, Integer> {

	HolderEntity findById(int id);

	HolderEntity findByPassportNumber(String passportNumber);

}
