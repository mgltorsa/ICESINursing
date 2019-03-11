package com.icesi.nursing.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class User {

	private Patient patient;

	@NonNull
	private String login;

	@NonNull
	private String names;

	@NonNull
	private String lastNames;

	@NonNull
	private char[] password;

	@NonNull
	private State state;
}
