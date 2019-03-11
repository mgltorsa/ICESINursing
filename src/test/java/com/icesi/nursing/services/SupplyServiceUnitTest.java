package com.icesi.nursing.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
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
import com.icesi.nursing.model.InventaryMedicine;
import com.icesi.nursing.model.Medicine;
import com.icesi.nursing.model.Patient;
import com.icesi.nursing.model.State;
import com.icesi.nursing.model.Supply;
import com.icesi.nursing.repositories.IPatientRepository;
import com.icesi.nursing.repositories.ISupplyRepository;
import com.icesi.nursing.services.IInventaryService;
import com.icesi.nursing.services.SupplyService;

@RunWith(MockitoJUnitRunner.class)
public class SupplyServiceUnitTest {

	@Autowired
	@InjectMocks
	private SupplyService supplyService;

	@Mock
	private ISupplyRepository supplyRepository;

	@Mock
	private IInventaryService inventaryService;

	@Mock
	private IPatientRepository patientRepository;

	private Supply supply;

	private Patient patient;

	private Medicine medicine;

	private InventaryMedicine inventaryMedicine;

	// TODO Init

	@Before
	public void init() {

		MockitoAnnotations.initMocks(this);

		patient = new Patient("1107527450", "Miguel", "Torres", State.ACTIVO);

		medicine = new Medicine("3312", "marimba", "maria y juana", "TecnoQuimicas", AdmininistrationType.CAPSULA,
				"no aplica");

		inventaryMedicine = new InventaryMedicine(medicine, 30,"Bodega", new Date());


		supply = new Supply("3312-1", medicine, 20,patient, new Date(), "Cancer de ojo");


	}

	// TODO Desde aqui punto 3a

	// TODO Test para verificar que la medicina exista
	@Test(expected = IllegalArgumentException.class)
	public void testAddSupplyWithNonExistMedicine() {

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		when(inventaryService.getInventaryMedicine(medicine.getConsecutive())).thenThrow(IllegalStateException.class);

		supplyService.addSupply(supply);

		fail();

	}

	// TODO Test para agregar un suministro con una cantidad no disponible de
	// medicina.
	@Test(expected = Exception.class)
	public void testUnavailableMedicineQuantity() {


		inventaryMedicine.setAvailableQuantity(0);

		List<InventaryMedicine> inventaries = new ArrayList<InventaryMedicine>();

		inventaries.add(inventaryMedicine);


		when(inventaryService.supplyMedicine(medicine.getConsecutive(), 30)).thenThrow(Exception.class);
		supplyService.addSupply(supply);

		fail();

	}

	// TODO Test para agregar suministro con paciente no existente
	@Test(expected = Exception.class)
	public void testNonExistPatient() {

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(null);

		supplyService.addSupply(supply);

		fail();

	}

	// TODO Test para agregar suministro de paciente inactivo
	@Test(expected = Exception.class)
	public void testInactivePatient() {

		patient.setState(State.INACTIVO);
		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		supplyService.addSupply(supply);

		fail();
	}

	@Test
	public void testGetSupply() {
		when(supplyRepository.getSupply(supply.getConsecutive())).thenReturn(supply);

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
		when(supplyRepository.getSupply(supply.getConsecutive())).thenReturn(null);
		supplyService.getSupply(supply.getConsecutive());
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
		when(supplyRepository.getSupply(supply.getConsecutive())).thenReturn(null);

		supplyService.updateSupply(supply);

		fail();

	}

	public void testRemoveExistSupply() {
		when(supplyRepository.getSupply(supply.getConsecutive())).thenReturn(supply);

		when(supplyRepository.removeSupply(supply.getConsecutive())).thenReturn(supply);

		Supply validSupply = supplyService.removeSupply(supply.getConsecutive());

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

		// El suministro ha sido removido
		when(supplyRepository.getSupply(supply.getConsecutive())).thenReturn(null);

		// Debe lanzar una excepcion si el suministro no existe
		supplyService.getSupply(supply.getConsecutive());

		fail();
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveNonExistSupply() {
		supplyService.removeSupply(supply.getConsecutive());
		fail();

	}

	@Test(expected = IllegalArgumentException.class)
	public void testRemoveWithNullConsecutive() {
		supplyService.removeSupply(null);
		fail();
	}

	@Test
	public void testAddValidSupply() {

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		List<InventaryMedicine> inventaries = new ArrayList<InventaryMedicine>();
		inventaries.add(inventaryMedicine);
		when(inventaryService.getInventaryMedicine(medicine.getConsecutive())).thenReturn(inventaries);

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

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);
		List<InventaryMedicine> inventaries = new ArrayList<InventaryMedicine>();
		inventaries.add(inventaryMedicine);

		when(inventaryService.getInventaryMedicine(medicine.getConsecutive())).thenReturn(inventaries);

		supplyService.addSupply(supply);

		fail();

	}

	// TODO Desde aqui punto 3b

	// TODO Test para actualizar la cantidad de medicamento disponible.
	@Test
	public void testAddValidSupplyB() {

		when(patientRepository.getPatient(patient.getDocument())).thenReturn(patient);

		List<InventaryMedicine> inventaries = new ArrayList<InventaryMedicine>();
		inventaries.add(inventaryMedicine);

		when(inventaryService.getInventaryMedicine(medicine.getConsecutive())).thenReturn(inventaries);

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

		// Verifica si llamo al metodo para actualizar la cantidad de medicamento en
		// inventario
		verify(inventaryService).supplyMedicine(medicine.getConsecutive(), supply.getQuantity());

	}
}
