package com.icesi.nursing.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icesi.nursing.model.Patient;
import com.icesi.nursing.model.State;
import com.icesi.nursing.model.Supply;
import com.icesi.nursing.repositories.IPatientRepository;
import com.icesi.nursing.repositories.ISupplyRepository;

@Service
public class SupplyService implements ISupplyService {

	@Autowired
	private ISupplyRepository supplyRepository;

	@Autowired
	private IInventaryService inventaryService;

	@Autowired
	private IPatientRepository patientRepository;

	@Override
	public Supply getSupply(String consecutive) {

		if (consecutive == null) {
			throw new IllegalArgumentException("consecutive was null");
		}

		Supply supply = supplyRepository.getSupply(consecutive);
		if (supply == null) {
			throw new IllegalStateException("Supply No. " + consecutive + " doesn't exists");
		}
		return supply;
	}

	@Override
	public Supply addSupply(Supply supply) {

		if (supply == null) {
			throw new NullPointerException("Supply was null");
		}
		if (supply.getConsecutive() == null || supply.getConsecutive().isEmpty()) {
			throw new IllegalArgumentException("supply consecutive was null or empty");
		}
		if (supply.getDate() == null) {
			throw new IllegalArgumentException("supply date was null");
		}
		if (supply.getMedicine() == null) {
			throw new IllegalArgumentException("supply medicine was null");
		}
		if (supply.getPatient() == null) {
			throw new IllegalArgumentException("supply patient was null");
		}
		if (supply.getPathology() == null || supply.getPathology().isEmpty()) {
			throw new IllegalArgumentException("supply pathology was null or empty");
		}

		Patient patient = patientRepository.getPatient(supply.getPatient().getDocument());
		// El paciente no existe
		if (patient == null) {
			throw new IllegalArgumentException("Patient doesn't exists");
		}

		// El paciente esta inactivo
		if (patient.getState() == State.INACTIVO) {
			throw new IllegalArgumentException("Patient is inactive");
		}


		try {
			//Comprueba si hay inventario
			inventaryService.getInventaryMedicine(supply.getMedicine().getConsecutive());

		} catch (IllegalStateException e) {
			throw new IllegalArgumentException("Inventary medicine doesn't exist - " + e.getMessage());
		}

		if (supply.getQuantity() <= 0) {
			throw new IllegalArgumentException("Supply's quantity must be greater than 0");
		}

		supplyRepository.addSupply(supply);

		// Si no hay suficiente medicina en el inventario lanzara una excepcion

		inventaryService.supplyMedicine(supply.getMedicine().getConsecutive(), supply.getQuantity());

		return supply;
	}

	@Override
	public Supply removeSupply(String consecutive) {
		Supply supply = getSupply(consecutive);
		supply = supplyRepository.removeSupply(supply.getConsecutive());
		return supply;
	}

	@Override
	public Supply updateSupply(Supply supply) {
		if (supply == null) {
			throw new NullPointerException("supply was null");
		}
		if (supply.getConsecutive() == null || supply.getConsecutive().isEmpty()) {
			throw new IllegalArgumentException("supply consecutive was null or empty");
		}
		if (supply.getDate() == null) {
			throw new IllegalArgumentException("supply date was null");
		}
		if (supply.getMedicine() == null) {
			throw new IllegalArgumentException("supply medicine was null");
		}
		if (supply.getPatient() == null) {
			throw new IllegalArgumentException("supply patient was null");
		}
		if (supply.getPathology() == null || supply.getPathology().isEmpty()) {
			throw new IllegalArgumentException("supply pathology was null or empty");
		}
		Supply oldSupply = getSupply(supply.getConsecutive());

		if (oldSupply == null) {
			throw new IllegalStateException("Doesn't exist supply No. " + supply.getConsecutive());
		}

		supplyRepository.updateSupply(supply);

		return supply;
	}

}
