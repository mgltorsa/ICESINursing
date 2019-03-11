package com.icesi.nursing.services;

import com.icesi.nursing.model.Patient;

public interface IPatientService {
	
	public Patient addPatient(Patient patient);

	public Patient getPatient(String id);

	public Patient removePatient(String id);

	public Patient updatePatient(Patient patient);

}
