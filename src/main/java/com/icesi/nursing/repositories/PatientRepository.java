package com.icesi.nursing.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.icesi.nursing.model.Patient;

@Repository
public class PatientRepository implements IPatientRepository {

	private Map<String, Patient> patients = new HashMap<String, Patient>();

	@Override
	public Patient addPatient(Patient patient) {
		patients.put(patient.getDocument(), patient);
		return patient;
	}

	@Override
	public Patient getPatient(String id) {
		return patients.get(id);
	}

	@Override
	public Patient removePatient(String id) {
		return patients.remove(id);
	}

	@Override
	public Patient updatePatient(Patient patient) {
		patients.replace(patient.getDocument(), patient);
		return patient;
	}

}
