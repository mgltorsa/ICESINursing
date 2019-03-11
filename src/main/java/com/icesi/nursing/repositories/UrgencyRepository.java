package com.icesi.nursing.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.icesi.nursing.model.UrgencyAttention;

@Repository
public class UrgencyRepository implements IUrgencyRepository {

	private Map<String, UrgencyAttention> attentions = new HashMap<String, UrgencyAttention>();

	@Override
	public UrgencyAttention getAttention(String consecutive) {

		return attentions.get(consecutive);
	}

	@Override
	public UrgencyAttention addAttention(UrgencyAttention attention) {
		attentions.put(attention.getConsecutive(), attention);
		return attention;
	}

	@Override
	public UrgencyAttention removeAttention(String consecutive) {
		return attentions.remove(consecutive);
	}

	@Override
	public UrgencyAttention updateAttention(UrgencyAttention attention) {
		attentions.replace(attention.getConsecutive(), attention);
		return attention;
	}

}
