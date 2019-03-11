package com.icesi.nursing.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Patient {

	@NonNull
	private String document;

	@NonNull
	private String names;

	@NonNull
	private String lastnames;

	private String academicProgram;

	private String academicDependency;

	@NonNull private State state;

}
