package com.store.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.store.model.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
	Optional<Medicine> findByMedicineName(String medicineName);
	
	Optional<Medicine> findByMedicineNameIgnoreCase(String medicineName);

}
