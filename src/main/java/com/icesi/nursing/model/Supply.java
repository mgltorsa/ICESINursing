package com.icesi.nursing.model;

import java.util.Date;

import lombok.Data;
import lombok.NonNull;

@Data
public class Supply {

	@NonNull
	private String consecutive;

	@NonNull
	private Medicine medicine;

	@NonNull
	private Integer quantity;

	@NonNull
	private Patient patient;

	@NonNull
	private Date date;

	private String observations;

	@NonNull
	private String pathology;

}
