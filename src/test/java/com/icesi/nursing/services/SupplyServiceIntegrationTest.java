package com.icesi.nursing.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.icesi.nursing.repositories.IMedicineRepository;
import com.icesi.nursing.repositories.IPatientRepository;
import com.icesi.nursing.services.IInventaryService;
import com.icesi.nursing.services.SupplyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = App.class)
public class SupplyServiceIntegrationTest {

	@Autowired
	private SupplyService supplyService;

	@Autowired
	private IPatientRepository patientRepository;

	@Autowired
	private IInventaryService inventaryService;

	@Autowired
	private IMedicineRepository medicineRepository;

	private Supply supply;

	private Patient patient;

	private Medicine medicine;

	private InventaryMedicine inventaryMedicine;

	@Before
	public void init() {

		patient = new Patient("1107527450", "Miguel", "Torres", State.ACTIVO);

		medicine = new Medicine("3312", "marimba", "maria y juana", "TecnoQuimicas", AdmininistrationType.CAPSULA,
				"no aplica");

		inventaryMedicine = new InventaryMedicine(medicine, 30,"Bodega", new Date());


		supply = new Supply("3312-1", medicine, 20,patient, new Date(), "Cancer de ojo");


		medicineRepository.addMedicine(medicine);

		patientRepository.addPatient(patient);

		inventaryService.addInventaryMedicine(inventaryMedicine);

	}
	// TODO Desde aqui punto 3a

	// TODO Test para agregar un suministro con una medicina inexistente
	@Test(expected = IllegalArgumentException.class)
	public void testAddSupplyWithNonExistMedicine() {

		Medicine newMedicine = new Medicine("DOESNTEXISTCONSECUTIVe", "brutal", "brutalMedicine", "brutalLaboratory",
				AdmininistrationType.CAPSULA, "NoIndications");
		supply.setMedicine(newMedicine);
		supplyService.addSupply(supply);

		fail();

	}

	// TODO Test para agregar suministro con paciente no existente
	@Test(expected = Exception.class)
	public void testNonExistPatient() {

		patient.setDocument("NONEXISTDOCUMENT");

		supplyService.addSupply(supply);

		fail();

	}

	// TODO Test para agregar suministro con paciente inactivo
	@Test(expected = Exception.class)
	public void testInactivePatient() {

		patient.setState(State.INACTIVO);

		supplyService.addSupply(supply);

		fail();
	}

	// TODO Test para agregar suministro con cantidad no disponible de medicine
	@Test(expected = Exception.class)
	public void testUnavailableMedicineQuantity() {

		supply.setQuantity(35);

		inventaryMedicine.setAvailableQuantity(inventaryMedicine.getAvailableQuantity() - supply.getQuantity());

		supplyService.addSupply(supply);

		fail();

	}

	@Test
	public void testGetSupply() {

		supplyService.addSupply(supply);

		Supply validSupply = supplyService.getSupply(supply.getConsecutive());

		assertNotNull(validSupply.getConsecutive());
		assertNotNull(validSupply.getMedicine());
		assertNotNull(validSupply.getPatient());
		assertNotNull(validSupply.getDate());
		assertNotNull(validSupply.getPathology());

		assertEquals(supply.getConsecutive(), validSupply.getConsecutive());
		assertEquals(supply.getDate(), validSupply.getDate());
		assertEquals(supply.getMedicine(), validSupply.getMedicine());
		assertEquals(supply.getPatient(), validSupply.getPatient());
		assertEquals(supply.getQuantity(), validSupply.getQuantity());
		assertEquals(supply.getPathology(), validSupply.getPathology());
		assertEquals(supply.getObservations(), validSupply.getObservations());

	}

	@Test(expected = IllegalStateException.class)
	public void testGetNonExistSupply() {

		supplyService.getSupply("DOESN'T EXIST CONSECUTIVE");
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetWithNullConsecutive() {
		supplyService.getSupply(null);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateNonValidSupply() {
		supply.setConsecutive("");

		supplyService.updateSupply(supply);

		fail();

	}

	@Test(expected = NullPointerException.class)
	public void testUpdateNullSupply() {

		supplyService.updateSupply(null);

		fail();

	}

	@Test(expected = IllegalStateException.class)
	public void testUpdateNonExistSupply() {

		supplyService.updateSupply(supply);

		fail();

	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveExistSupply() {

		Supply validSupply = supplyService.removeSupply(supply.getConsecutive());

		assertNotNull(validSupply.getConsecutive());
		assertNotNull(validSupply.getMedicine());
		assertNotNull(validSupply.getPatient());
		assertNotNull(validSupply.getDate());
		assertNotNull(validSupply.getPathology());

		assertEquals(supply.getConsecutive(), validSupply.getConsecutive());
		assertEquals(supply.getMedicine().getConsecutive(), validSupply.getMedicine().getConsecutive());
		assertEquals(supply.getPatient(), validSupply.getPatient());
		assertEquals(supply.getPathology(), validSupply.getPathology());
		assertEquals(supply.getObservations(), validSupply.getObservations());

		// Suministro ha sido removido

		// Lanzara una excepcion si el suministro no existe
		supplyService.getSupply(supply.getConsecutive());

		fail();
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveNonExistSupply() {
		supplyService.removeSupply("DOESN'T EXIST CONSECUTIVE");
		fail();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullConsecutive() {
		supplyService.removeSupply(null);
		fail();
	}

	@Test
	public void testAddValidSupply() {

		Supply validSupply = supplyService.addSupply(supply);

		assertEquals(supply, validSupply);

		assertNotNull(validSupply.getConsecutive());
		assertNotNull(validSupply.getMedicine());
		assertNotNull(validSupply.getPatient());
		assertNotNull(validSupply.getDate());
		assertNotNull(validSupply.getPathology());

		assertEquals(supply.getConsecutive(), validSupply.getConsecutive());
		assertEquals(supply.getDate(), validSupply.getDate());
		assertEquals(supply.getMedicine(), validSupply.getMedicine());
		assertEquals(supply.getPatient(), validSupply.getPatient());
		assertEquals(supply.getQuantity(), validSupply.getQuantity());
		assertEquals(supply.getPathology(), validSupply.getPathology());
		assertEquals(supply.getObservations(), validSupply.getObservations());

	}

	@Test(expected = Exception.class)
	public void testAddNonValidSupply() {

		supply.setQuantity(0);

		supplyService.addSupply(supply);

		fail();

	}

	// TODO Desde aqui punto 3b

	// TODO Test para actualizar la cantidad de medicamento disponible.
	@Test
	public void testAddValidSupplyB() throws Exception {

		int quantityAfterSupply = inventaryMedicine.getAvailableQuantity() - supply.getQuantity();

		Supply validSupply = supplyService.addSupply(supply);

		assertEquals(supply, validSupply);

		assertNotNull(validSupply.getConsecutive());
		assertNotNull(validSupply.getMedicine());
		assertNotNull(validSupply.getPatient());
		assertNotNull(validSupply.getDate());
		assertNotNull(validSupply.getPathology());

		assertEquals(supply.getConsecutive(), validSupply.getConsecutive());
		assertEquals(supply.getDate(), validSupply.getDate());
		assertEquals(supply.getMedicine(), validSupply.getMedicine());
		assertEquals(supply.getPatient(), validSupply.getPatient());
		assertEquals(supply.getQuantity(), validSupply.getQuantity());
		assertEquals(supply.getPathology(), validSupply.getPathology());
		assertEquals(supply.getObservations(), validSupply.getObservations());

		// Verifica si el inventario disminuyo luego de agregar el suministro.
		List<InventaryMedicine> inventary = inventaryService
				.getInventaryMedicine(inventaryMedicine.getMedicine().getConsecutive());

		int totalAvailable = 0;
		for (InventaryMedicine item : inventary) {
			totalAvailable += item.getAvailableQuantity();
		}
		assertEquals(quantityAfterSupply, totalAvailable);
	}

	@After
	public void destroy() {

		medicineRepository.removeMedicine(medicine.getConsecutive());

		patientRepository.removePatient(patient.getDocument());

		inventaryService.removeInventaryMedicine(inventaryMedicine.getMedicine().getConsecutive());

	}

}
