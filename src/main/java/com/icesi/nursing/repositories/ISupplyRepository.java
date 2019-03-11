package com.icesi.nursing.repositories;

import com.icesi.nursing.model.Supply;

public interface ISupplyRepository {

	public Supply getSupply(String consecutive);

	public Supply addSupply(Supply supply);

	public Supply removeSupply(String consecutive);

	public Supply updateSupply(Supply supply);
}
