package com.icesi.nursing.repositories;

import com.icesi.nursing.model.Medicine;

public interface IMedicineRepository {

	public Medicine addMedicine(Medicine inventary);

	public Medicine getMedicine(String consecutive);

	public Medicine removeMedicine(String consecutive);

	public Medicine updateMedicine(Medicine inventary);
}
