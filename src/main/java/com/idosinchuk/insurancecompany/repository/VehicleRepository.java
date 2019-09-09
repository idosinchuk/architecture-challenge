package com.idosinchuk.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.insurancecompany.entity.VehicleEntity;

/**
 * Repository for vehicle
 * 
 * @author Igor Dosinchuk
 *
 */
public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {

	VehicleEntity findById(int id);

}
