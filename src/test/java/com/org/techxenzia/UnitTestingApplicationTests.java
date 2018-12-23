package com.org.techxenzia;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.org.techxenzia.controller.test.CustControllerIntegrationTest;
import com.org.techxenzia.controller.test.CustControllerTest;
import com.org.techxenzia.repository.test.CustomerRepositoryTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({
		CustControllerTest.class,
		CustControllerIntegrationTest.class,
		CustomerRepositoryTest.class
})
@SpringBootTest
public class UnitTestingApplicationTests {

	@Test
	public void contextLoads() {
	}

}
