package com.icesi.nursing.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.icesi.nursing.model.Medicine;

@Repository
public class MedicineRepository implements IMedicineRepository {

	private Map<String, Medicine> medicines = new HashMap<String, Medicine>();

	@Override
	public Medicine addMedicine(Medicine medicine) {
		medicines.put(medicine.getConsecutive(), medicine);
		return medicine;
	}

	@Override
	public Medicine getMedicine(String consecutive) {
		return medicines.get(consecutive);
	}

	@Override
	public Medicine removeMedicine(String consecutive) {
		return medicines.remove(consecutive);
	}

	@Override
	public Medicine updateMedicine(Medicine medicine) {
		medicines.replace(medicine.getConsecutive(), medicine);
		return medicine;

	}

}
