package com.icesi.nursing.model;

import java.util.Date;

import lombok.Data;
import lombok.NonNull;

@Data
public class InventaryMedicine {

	@NonNull
	private Medicine medicine;

	@NonNull
	private Integer availableQuantity;

	@NonNull
	private String ubication;

	@NonNull
	private Date expirationDate;

}
