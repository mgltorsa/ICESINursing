package com.icesi.nursing.services;

import java.util.List;

import com.icesi.nursing.model.InventaryMedicine;

public interface IInventaryService {
	
	public InventaryMedicine addInventaryMedicine(InventaryMedicine inventary);
	public InventaryMedicine updateInventaryMedicine(InventaryMedicine inventary);
	public List<InventaryMedicine> removeInventaryMedicine(String medicineConsecutive);
	public List<InventaryMedicine> getInventaryMedicine(String medicineConsecutive);
	public List<InventaryMedicine> supplyMedicine(String medicineConsecutive, int quantitySupplied);

}
