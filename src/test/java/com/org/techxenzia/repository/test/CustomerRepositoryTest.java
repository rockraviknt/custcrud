package com.org.techxenzia.repository.test;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import com.org.techxenzia.AppStarter;
import com.org.techxenzia.model.Customer;
import com.org.techxenzia.repository.CustomerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	classes = AppStarter.class
)
@TestPropertySource(locations = "classpath:application.properties")

public class CustomerRepositoryTest {
	
	@Autowired
	CustomerRepository custRepository;
    
	Customer i1=null;
	Customer i2=null;
	
	@Test
	public void whenFindByName_thenReturnCustomer() {
	    i1= createTestCustomer("bob","Paris");
		i2=createTestCustomer("alex", "Japan");
        
		
	    List<Customer> found = custRepository.findByName(i1.getname());
	    // then
	    assertThat("No match found",found.size()==1);
	    assertEquals(i1.getname(), found.get(0).getname().toString());
	}
	
	
    @After
    public void resetDb() {
    	custRepository.deleteAll();
    }
    
    private Customer createTestCustomer(String name, String add) {
        Customer cust = new Customer();
        cust.setname(name);
        cust.setaddress(add);
        cust.setPhoneno("999999999");
        return custRepository.save(cust);
    }
	
}
