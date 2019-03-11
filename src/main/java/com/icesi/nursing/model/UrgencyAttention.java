package com.icesi.nursing.model;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NonNull;

@Data
public class UrgencyAttention {

	@NonNull
	private String consecutive;

	@NonNull
	private Date date;

	@NonNull
	private Patient patient;

	@NonNull
	private String generalDescription;

	@NonNull
	private String procedure;

	@NonNull
	private Boolean forwarded;
	
	private String dispatchedPlace;

	private List<Supply> supplies;

}
