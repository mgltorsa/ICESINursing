package com.icesi.nursing.repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

import com.icesi.nursing.model.InventaryMedicine;

@Repository
public class InventaryRepository implements IInventaryRepository {

	private List<InventaryMedicine> inventaries = new ArrayList<InventaryMedicine>();

	@Override
	public InventaryMedicine addInventaryMedicine(InventaryMedicine inventary) {
		inventaries.add(inventary);
		return inventary;
	}

	@Override
	public List<InventaryMedicine> getInventaryMedicine(String consecutive) {

		List<InventaryMedicine> inventaries = new ArrayList<>();

		Stream<InventaryMedicine> stream = this.inventaries.stream()
				.filter(e -> e.getMedicine().getConsecutive().equals(consecutive));

		Iterator<InventaryMedicine> iterator = stream.iterator();

		if (iterator.hasNext()) {
			inventaries.add(iterator.next());
		}

		return inventaries;
	}

	@Override
	public List<InventaryMedicine> removeInventaryMedicine(String consecutive) {

		List<InventaryMedicine> inventaries = new ArrayList<>();

		for (int i = 0; i < this.inventaries.size(); i++) {
			InventaryMedicine inventary = this.inventaries.get(i);
			if (inventary.getMedicine().getConsecutive().equals(consecutive)) {
				inventaries.add(inventary);
				this.inventaries.remove(i);
			}

		}

		return inventaries;
	}

	@Override
	public InventaryMedicine updateInventaryMedicine(InventaryMedicine inventary) {

		for (int i = 0; i < this.inventaries.size(); i++) {
			InventaryMedicine oldInventary = this.inventaries.get(i);
			if (oldInventary.getMedicine().getConsecutive().equals(inventary.getMedicine().getConsecutive())
					&& oldInventary.getUbication().equals(inventary.getUbication())
					&& oldInventary.getExpirationDate().equals(inventary.getExpirationDate())) {

				inventaries.set(i, inventary);
				break;
			}

		}

		return inventary;

	}

}
