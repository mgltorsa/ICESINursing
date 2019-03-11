package com.icesi.nursing.services;

import com.icesi.nursing.model.Supply;

public interface ISupplyService {

	public Supply getSupply(String consecutive);
	
	public Supply addSupply(Supply supply);
	
	public Supply removeSupply(String consecutive);
	
	public Supply updateSupply(Supply supply);
}
