package com.icesi.nursing.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Medicine {

	@NonNull
	private String consecutive;

	@NonNull
	private String name;

	@NonNull
	private String genericName;

	@NonNull
	private String laboratory;

	@NonNull
	private AdmininistrationType administrationType;

	@NonNull
	private String indications;

	private String contraIndications;

}
