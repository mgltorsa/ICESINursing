package com.icesi.nursing.repositories;

import java.util.List;

import com.icesi.nursing.model.InventaryMedicine;

public interface IInventaryRepository {

	public InventaryMedicine addInventaryMedicine(InventaryMedicine inventary);

	public List<InventaryMedicine> getInventaryMedicine(String consecutive);

	public List<InventaryMedicine> removeInventaryMedicine(String consecutive);

	public InventaryMedicine updateInventaryMedicine(InventaryMedicine inventary);
}
