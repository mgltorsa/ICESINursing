package com.icesi.nursing.services;

import com.icesi.nursing.model.UrgencyAttention;

public interface IUrgencyService {

public UrgencyAttention getAttention(String consecutive);
	
	public UrgencyAttention addAttention(UrgencyAttention attention);
	
	public UrgencyAttention removeAttention(String consecutive);
	
	public UrgencyAttention updateAttention(UrgencyAttention attention);
}
