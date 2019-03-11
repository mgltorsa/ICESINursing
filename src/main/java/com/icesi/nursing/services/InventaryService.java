package com.icesi.nursing.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icesi.nursing.model.InventaryMedicine;
import com.icesi.nursing.model.Medicine;
import com.icesi.nursing.repositories.IInventaryRepository;
import com.icesi.nursing.repositories.IMedicineRepository;

@Service
public class InventaryService implements IInventaryService {

	@Autowired
	private IInventaryRepository inventaryRepository;

	@Autowired
	private IMedicineRepository medicineRepository;

	@Override
	public InventaryMedicine addInventaryMedicine(InventaryMedicine inventary) {
		if (inventary == null) {
			throw new NullPointerException("inventary was null");
		}

		Medicine medicine = medicineRepository.getMedicine(inventary.getMedicine().getConsecutive());

		if (medicine == null) {
			throw new IllegalStateException("Medicine doesn't exist");
		}

		return inventaryRepository.addInventaryMedicine(inventary);
	}

	@Override
	public InventaryMedicine updateInventaryMedicine(InventaryMedicine inventary) {
		if (inventary == null) {
			throw new NullPointerException("inventary was null");
		}

		Medicine medicine = medicineRepository.getMedicine(inventary.getMedicine().getConsecutive());

		if (medicine == null) {
			throw new IllegalStateException("Medicine doesn't exist");
		}
		return null;
	}

	@Override
	public List<InventaryMedicine> removeInventaryMedicine(String medicineConsecutive) {

		if (medicineConsecutive == null) {
			throw new NullPointerException("consecutive was null");
		}
		return inventaryRepository.removeInventaryMedicine(medicineConsecutive);
	}

	@Override
	public List<InventaryMedicine> getInventaryMedicine(String medicineConsecutive) {
		if (medicineConsecutive == null) {
			throw new IllegalArgumentException("consecutive was null");
		}

		Medicine medicine = medicineRepository.getMedicine(medicineConsecutive);

		if (medicine == null) {
			throw new IllegalStateException("Medicine doesn't exist");
		}
		List<InventaryMedicine> inventaries = inventaryRepository.getInventaryMedicine(medicineConsecutive);
		if (inventaries == null || inventaries.size() == 0) {
			throw new IllegalStateException("inventary doesn't exist");
		}
		return inventaries;
	}

	@Override
	public List<InventaryMedicine> supplyMedicine(String medicineConsecutive, int quantitySupplied) {
		if (medicineConsecutive == null) {
			throw new NullPointerException("consecutive was null");
		}

		if (quantitySupplied <= 0) {
			throw new IllegalArgumentException("quantity must be greater than 0");
		}

		List<InventaryMedicine> inventaries = getInventaryMedicine(medicineConsecutive);

		int totalOnInventary = 0;

		List<InventaryMedicine> updatedInventary = new ArrayList<InventaryMedicine>();

		for (InventaryMedicine inventary : inventaries) {
			int availableQuantity = inventary.getAvailableQuantity();
			if (availableQuantity > 0) {
				totalOnInventary += availableQuantity;
				updatedInventary.add(inventary);
			}

			if (totalOnInventary >= quantitySupplied) {
				break;
			}
		}

		if (totalOnInventary < quantitySupplied) {
			throw new IllegalStateException("Total on inventary must be greater than quantity supplied");
		}

		int tempSupplied = quantitySupplied;

		// actualiza los inventarios usados
		for (InventaryMedicine inventary : updatedInventary) {

			int oldQuantity = inventary.getAvailableQuantity();

			int newQuantity = oldQuantity - tempSupplied >= 0 ? oldQuantity - tempSupplied : 0;

			tempSupplied -= oldQuantity;

			inventary.setAvailableQuantity(newQuantity);

			updateInventaryMedicine(inventary);

			if (tempSupplied <= 0) {
				break;
			}
		}
		return updatedInventary;
	}

}
