package com.icesi.nursing.services;

import com.icesi.nursing.model.Medicine;

public interface IMedicineService {

	public Medicine addInventaryMedicine(Medicine inventary);
	public Medicine updateInventaryMedicine(Medicine inventary);
	public Medicine removeInventaryMedicine(String consecutive);
	public Medicine getInventaryMedicine(String medicineConsecutive);
}
