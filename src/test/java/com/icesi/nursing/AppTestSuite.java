package com.icesi.nursing;

/**
 * @author Miguel Torres
 * Correr esta clase para correr todas los Tests
 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.icesi.nursing.services.SupplyServiceIntegrationTest;
import com.icesi.nursing.services.SupplyServiceUnitTest;
import com.icesi.nursing.services.UrgencyServiceIntegrationTest;
import com.icesi.nursing.services.UrgencyServiceUnitTest;


@RunWith(Suite.class)
@SuiteClasses({ SupplyServiceUnitTest.class, SupplyServiceIntegrationTest.class, UrgencyServiceUnitTest.class,
		UrgencyServiceIntegrationTest.class })
public class AppTestSuite {


	
}
