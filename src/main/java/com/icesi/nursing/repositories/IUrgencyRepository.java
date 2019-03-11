package com.icesi.nursing.repositories;

import com.icesi.nursing.model.UrgencyAttention;

public interface IUrgencyRepository {

	public UrgencyAttention getAttention(String consecutive);

	public UrgencyAttention addAttention(UrgencyAttention attention);

	public UrgencyAttention removeAttention(String consecutive);

	public UrgencyAttention updateAttention(UrgencyAttention attention);

}
