package com.icesi.nursing.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.icesi.nursing.model.Supply;

@Repository
public class SupplyRepository implements ISupplyRepository {

	private Map<String, Supply> supplies = new HashMap<String, Supply>();

	@Override
	public Supply getSupply(String consecutive) {
		return supplies.get(consecutive);
	}

	@Override
	public Supply addSupply(Supply supply) {
		return supplies.put(supply.getConsecutive(), supply);
	}

	@Override
	public Supply removeSupply(String consecutive) {

		return supplies.remove(consecutive);
	}

	@Override
	public Supply updateSupply(Supply supply) {
		supplies.replace(supply.getConsecutive(), supply);
		return supply;
	}

}
