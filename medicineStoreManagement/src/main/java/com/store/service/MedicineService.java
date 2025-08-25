package com.store.service;

import java.util.List;

import com.store.model.Medicine;

public interface MedicineService {
	Medicine saveMedicine(Medicine medicine);
	List<Medicine> getAllMedicines();
    Medicine sellMedicine(Long id, int sellQty);
}
