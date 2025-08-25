package com.store.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.model.Medicine;
import com.store.repository.MedicineRepository;
import com.store.service.MedicineService;

@Service
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private MedicineRepository medicineRepository;

	@Override
	public Medicine saveMedicine(Medicine medicine) {

		if (medicine.getMedicineName() == null || medicine.getMedicineName().trim().isEmpty()) {
			throw new IllegalArgumentException("Medicine name must not be null or empty.");
		}

		Optional<Medicine> existingOpt = medicineRepository.findByMedicineNameIgnoreCase(medicine.getMedicineName());
		if (existingOpt.isPresent()) {
			Medicine existing = existingOpt.get();
			existing.setQuantity(existing.getQuantity() + medicine.getQuantity());
			existing.setPrice(existing.getPrice());
			existing.setDescription(medicine.getDescription()); // Optional
			existing.setCreatedOn(LocalDateTime.now());
			existing.setCreatedBy("RAIHAN");
			return medicineRepository.save(existing);

		} else {
			medicine.setCreatedOn(LocalDateTime.now());
			medicine.setCreatedBy("RAIHAN");
			return medicineRepository.save(medicine);
		}

	}

	@Override
	public List<Medicine> getAllMedicines() {
		return medicineRepository.findAll();
	}

	@Override
	public Medicine sellMedicine(Long id, int sellQty) {
		Medicine medicine = medicineRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Medicine not found"));
		if (medicine.getQuantity() < sellQty) {
			throw new RuntimeException("Insufficient quantity");
		}
		medicine.setQuantity(medicine.getQuantity() - sellQty);
		return medicineRepository.save(medicine);
	}

}
