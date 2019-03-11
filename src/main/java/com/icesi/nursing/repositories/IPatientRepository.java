package com.icesi.nursing.repositories;

import com.icesi.nursing.model.Patient;

public interface IPatientRepository {

	public Patient addPatient(Patient patient);

	public Patient getPatient(String id);

	public Patient removePatient(String id);

	public Patient updatePatient(Patient patient);
}
