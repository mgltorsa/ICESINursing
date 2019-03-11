package com.icesi.nursing.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.icesi.nursing.model.AdmininistrationType;
import com.icesi.nursing.model.Medicine;
import com.icesi.nursing.model.Patient;
import com.icesi.nursing.model.State;
import com.icesi.nursing.model.Supply;
import com.icesi.nursing.model.UrgencyAttention;
import com.icesi.nursing.repositories.IPatientRepository;
import com.icesi.nursing.repositories.IUrgencyRepository;
import com.icesi.nursing.services.ISupplyService;
import com.icesi.nursing.services.UrgencyService;

@RunWith(MockitoJUnitRunner.class)
public class UrgencyServiceUnitTest {

	@Autowired
	@InjectMocks
	private UrgencyService urgencyService;

	@Mock
	private IUrgencyRepository urgencyRepository;

	@Mock
	private ISupplyService supplyService;

	@Mock
	private IPatientRepository patientRepository;

	private UrgencyAttention urgencyAttention;

	private Supply supply;

	private Patient patient;

	private Medicine medicine;

	// TODO Init
	@Before
	public void init() {

		MockitoAnnotations.initMocks(this);

		patient = new Patient("1107527450", "Miguel", "Torres", State.ACTIVO);

		medicine = new Medicine("3312", "marimba", "maria y juana", "TecnoQuimicas", AdmininistrationType.CAPSULA,
				"no aplica");

		supply = new Supply("3312-1", medicine, 30, patient, new Date(), "Morirá");

		List<Supply> supplies = new ArrayList<>();
		supplies.add(supply);

		urgencyAttention = new UrgencyAttention("3312", new Date(), patient, "general description", "se inyecto", true);
		urgencyAttention.setSupplies(supplies);

	}

	// TODO Desde aqui punto 3c

	@Test
	public void testAddValidAttention() {

		when(supplyService.addSupply(supply)).thenReturn(supply);

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		when(urgencyRepository.addAttention(urgencyAttention)).thenReturn(urgencyAttention);

		UrgencyAttention oldAttention = urgencyService.addAttention(urgencyAttention);

		assertNotNull(oldAttention);
		assertNotNull(oldAttention.getConsecutive());
		assertNotNull(oldAttention.getDate());
		assertNotNull(oldAttention.getForwarded());
		assertNotNull(oldAttention.getPatient());
		assertNotNull(oldAttention.getProcedure());
		assertNotNull(oldAttention.getSupplies());

		assertEquals(urgencyAttention.getConsecutive(), oldAttention.getConsecutive());
		assertEquals(urgencyAttention.getDate().toString(), oldAttention.getDate().toString());
		assertEquals(urgencyAttention.getForwarded(), oldAttention.getForwarded());
		assertEquals(urgencyAttention.getPatient().getDocument(), oldAttention.getPatient().getDocument());
		assertEquals(urgencyAttention.getProcedure(), oldAttention.getProcedure());

	}

	@Test(expected = IllegalStateException.class)
	public void testAddAtenttionWithNonExistPatient() {

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(null);

		urgencyService.addAttention(urgencyAttention);

		fail();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAttentionWithNonExistMedicine() {

		when(supplyService.addSupply(supply)).thenThrow(IllegalStateException.class);

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		urgencyService.addAttention(urgencyAttention);

		fail();

	}

	@Test
	public void testAddAttentionWithoutSupplies() {

		urgencyAttention.setSupplies(null);

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		when(urgencyRepository.addAttention(urgencyAttention)).thenReturn(urgencyAttention);

		UrgencyAttention oldAttention = urgencyService.addAttention(urgencyAttention);

		assertNotNull(oldAttention);
		assertNotNull(oldAttention.getConsecutive());
		assertNotNull(oldAttention.getDate());
		assertNotNull(oldAttention.getForwarded());
		assertNotNull(oldAttention.getPatient());
		assertNotNull(oldAttention.getProcedure());
		assertNull(oldAttention.getSupplies());

		assertEquals(urgencyAttention.getConsecutive(), oldAttention.getConsecutive());
		assertEquals(urgencyAttention.getDate().toString(), oldAttention.getDate().toString());
		assertEquals(urgencyAttention.getForwarded(), oldAttention.getForwarded());
		assertEquals(urgencyAttention.getPatient().getDocument(), oldAttention.getPatient().getDocument());
		assertEquals(urgencyAttention.getProcedure(), oldAttention.getProcedure());

	}

	// Test para agregar una atencion que tiene suministros con paciente diferente
	@Test(expected = IllegalArgumentException.class)
	public void testAttentionWithDifferentPatient() {

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		Patient newPatient = new Patient("NEWPATIENT", "NEWPATIENT", "NEWPATIENT", State.ACTIVO);

		supply.setPatient(newPatient);

		urgencyService.addAttention(urgencyAttention);

	}

	@Test
	public void testGetAttention() {

		when(urgencyRepository.getAttention(urgencyAttention.getConsecutive())).thenReturn(urgencyAttention);

		UrgencyAttention oldAttention = urgencyService.getAttention(urgencyAttention.getConsecutive());

		assertNotNull(oldAttention);
		assertNotNull(oldAttention.getConsecutive());
		assertNotNull(oldAttention.getDate());
		assertNotNull(oldAttention.getForwarded());
		assertNotNull(oldAttention.getPatient());
		assertNotNull(oldAttention.getProcedure());
		assertNotNull(oldAttention.getSupplies());

		assertEquals(urgencyAttention.getConsecutive(), oldAttention.getConsecutive());
		assertEquals(urgencyAttention.getDate().toString(), oldAttention.getDate().toString());
		assertEquals(urgencyAttention.getForwarded(), oldAttention.getForwarded());
		assertEquals(urgencyAttention.getPatient().getDocument(), oldAttention.getPatient().getDocument());
		assertEquals(urgencyAttention.getProcedure(), oldAttention.getProcedure());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetNonExistAttention() {
		urgencyService.getAttention(urgencyAttention.getConsecutive());
		fail();
	}

	@Test(expected = NullPointerException.class)
	public void testGetAttentionWithNullConsecutive() {
		urgencyService.getAttention(null);
		fail();
	}

	@Test
	public void testRemoveAttention() {

		when(urgencyRepository.getAttention(urgencyAttention.getConsecutive())).thenReturn(urgencyAttention);

		when(urgencyRepository.removeAttention(urgencyAttention.getConsecutive())).thenReturn(urgencyAttention);

		UrgencyAttention oldAttention = urgencyService.removeAttention(urgencyAttention.getConsecutive());

		assertNotNull(oldAttention);
		assertNotNull(oldAttention.getConsecutive());
		assertNotNull(oldAttention.getDate());
		assertNotNull(oldAttention.getForwarded());
		assertNotNull(oldAttention.getPatient());
		assertNotNull(oldAttention.getProcedure());
		assertNotNull(oldAttention.getSupplies());

		assertEquals(urgencyAttention.getConsecutive(), oldAttention.getConsecutive());
		assertEquals(urgencyAttention.getDate().toString(), oldAttention.getDate().toString());
		assertEquals(urgencyAttention.getForwarded(), oldAttention.getForwarded());
		assertEquals(urgencyAttention.getPatient().getDocument(), oldAttention.getPatient().getDocument());
		assertEquals(urgencyAttention.getProcedure(), oldAttention.getProcedure());
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveNonExistAttention() {
		urgencyService.removeAttention(urgencyAttention.getConsecutive());
		fail();
	}

	@Test(expected = NullPointerException.class)
	public void testRemoveAttentionWithNullConsecutive() {
		urgencyService.removeAttention(null);
		fail();
	}

	@Test
	public void testUpdateAttention() {
		UrgencyAttention newAttention = new UrgencyAttention(urgencyAttention.getConsecutive(),
				urgencyAttention.getDate(), patient, "ANOTHER DESCRIPTION", "SE INYECTO EN EL BRAZO", false);
		newAttention.setDispatchedPlace("DISPATCHED IN CALIFORNIA");

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);
		when(urgencyRepository.getAttention(urgencyAttention.getConsecutive())).thenReturn(urgencyAttention);
		when(urgencyRepository.updateAttention(newAttention)).thenReturn(newAttention);

		UrgencyAttention oldAttention = urgencyService.updateAttention(newAttention);

		assertNotNull(oldAttention);
		assertNotNull(oldAttention.getConsecutive());
		assertNotNull(oldAttention.getDate());
		assertNotNull(oldAttention.getForwarded());
		assertNotNull(oldAttention.getPatient());
		assertNotNull(oldAttention.getProcedure());
		assertNotNull(oldAttention.getDispatchedPlace());

		assertEquals(urgencyAttention.getConsecutive(), oldAttention.getConsecutive());
		assertEquals(urgencyAttention.getDate().toString(), oldAttention.getDate().toString());
		assertNotEquals(urgencyAttention.getForwarded(), oldAttention.getForwarded());
		assertNotEquals(urgencyAttention.getGeneralDescription(), oldAttention.getGeneralDescription());
		assertNotEquals(urgencyAttention.getDispatchedPlace(), oldAttention.getDispatchedPlace());
		assertNotEquals(urgencyAttention.getForwarded(), oldAttention.getDispatchedPlace());

	}

}
