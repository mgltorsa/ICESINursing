package com.icesi.nursing.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icesi.nursing.App;
import com.icesi.nursing.model.AdmininistrationType;
import com.icesi.nursing.model.InventaryMedicine;
import com.icesi.nursing.model.Medicine;
import com.icesi.nursing.model.Patient;
import com.icesi.nursing.model.State;
import com.icesi.nursing.model.Supply;
import com.icesi.nursing.model.UrgencyAttention;
import com.icesi.nursing.repositories.IMedicineRepository;
import com.icesi.nursing.repositories.IPatientRepository;
import com.icesi.nursing.repositories.IUrgencyRepository;
import com.icesi.nursing.services.IInventaryService;
import com.icesi.nursing.services.UrgencyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class)
public class UrgencyServiceIntegrationTest {

	@Autowired
	private UrgencyService urgencyService;

	@Autowired
	private IUrgencyRepository urgencyRepository;

	@Autowired
	private IPatientRepository patientRepository;

	@Autowired
	private IInventaryService inventaryService;

	@Autowired
	private IMedicineRepository medicineRepository;

	private UrgencyAttention urgencyAttention;

	private Supply supply;

	private Patient patient;

	private Medicine medicine;

	private InventaryMedicine inventary;

	// TODO Init
	@Before
	public void init() {

		MockitoAnnotations.initMocks(this);

		patient = new Patient("1107527450", "Miguel", "Torres", State.ACTIVO);

		medicine = new Medicine("3312", "marimba", "maria y juana", "TecnoQuimicas", AdmininistrationType.CAPSULA,
				"no aplica");

		inventary = new InventaryMedicine(medicine, 30, "bodega", new Date());

		supply = new Supply("3312-1", medicine, 20, patient, new Date(), "Morirá");

		List<Supply> supplies = new ArrayList<>();
		supplies.add(supply);

		urgencyAttention = new UrgencyAttention("3312", new Date(), patient, "general description", "se inyecto", true);
		urgencyAttention.setSupplies(supplies);

		patientRepository.addPatient(patient);
		medicineRepository.addMedicine(medicine);
		inventaryService.addInventaryMedicine(inventary);

	}

	// TODO Desde aqui punto 3c

	@Test
	public void testAddValidAttention() {

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

		patientRepository.removePatient(patient.getDocument());

		urgencyService.addAttention(urgencyAttention);

		fail();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddAttentionWithNonExistMedicine() {

		medicineRepository.removeMedicine(medicine.getConsecutive());

		urgencyService.addAttention(urgencyAttention);

		fail();

	}

	@Test
	public void testAddAttentionWithoutSupplies() {

		urgencyAttention.setSupplies(null);

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

		Patient newPatient = new Patient("NEWPATIENT", "NEWPATIENT", "NEWPATIENT", State.ACTIVO);

		supply.setPatient(newPatient);

		urgencyService.addAttention(urgencyAttention);

	}

	@Test
	public void testGetAttention() {

		urgencyRepository.addAttention(urgencyAttention);
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

		urgencyRepository.addAttention(urgencyAttention);
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

		urgencyRepository.addAttention(urgencyAttention);

		UrgencyAttention newAttention = new UrgencyAttention(urgencyAttention.getConsecutive(),
				urgencyAttention.getDate(), patient, "ANOTHER DESCRIPTION", "SE INYECTO EN EL BRAZO", false);
		newAttention.setDispatchedPlace("DISPATCHED IN CALIFORNIA");

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

	@After
	public void clear() {
		urgencyRepository.removeAttention(urgencyAttention.getConsecutive());
		patientRepository.removePatient(patient.getDocument());
		inventaryService.removeInventaryMedicine(medicine.getConsecutive());
		medicineRepository.removeMedicine(medicine.getConsecutive());

	}
}
