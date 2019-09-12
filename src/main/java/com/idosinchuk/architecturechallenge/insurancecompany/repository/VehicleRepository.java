package com.idosinchuk.architecturechallenge.insurancecompany.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idosinchuk.architecturechallenge.insurancecompany.entity.VehicleEntity;

/**
 * Repository for vehicle
 * 
 * @author Igor Dosinchuk
 *
 */
public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {

	VehicleEntity findByLicensePlate(String licensePlate);

}
