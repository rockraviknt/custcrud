package com.org.techxenzia.controller.test;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.Collections;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.org.techxenzia.utility.AppTestUtility.asJsonString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.techxenzia.AppStarter;
import com.org.techxenzia.model.Customer;
import com.org.techxenzia.repository.CustomerRepository;


@RunWith(SpringRunner.class)
@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
	classes = AppStarter.class
)
@TestPropertySource(locations = "classpath:application.properties")

@AutoConfigureMockMvc
public class CustControllerIntegrationTest {
	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	CustomerRepository custRepository;
	
	Customer i1=null;
	Customer i2=null;
	
	@Test
	public void custControllerAllcustomersTest() throws Exception{
	
		mvc.perform(
					MockMvcRequestBuilders.get("/api/customers")
						.contentType(MediaType.APPLICATION_JSON))
				      	.andExpect(status().isOk())
				      	.andExpect(content()
				      	 .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				      	  .andExpect(jsonPath("$", hasSize(2)))
				          .andExpect(jsonPath("$[0].name", is(i1.getname())))
				          .andExpect(jsonPath("$[1].name", is(i2.getname())));

	}
	
	
	@Test
	public void createCustomerEndpointTest() throws Exception{
		
		mvc.perform( 
					MockMvcRequestBuilders.post("/api/customers")
										  .accept(MediaType.APPLICATION_JSON).content(asJsonString(i1))
										  .contentType(MediaType.APPLICATION_JSON)
										).andExpect(status().isOk())
										.andExpect(jsonPath("$", is(CoreMatchers.notNullValue())))
										.andExpect(jsonPath("$.id", is(i1.getId().intValue())))
								        .andExpect(jsonPath("$.name", is(i1.getname())))
								        .andExpect(jsonPath("$.address", is(i1.getaddress())))
								        .andExpect(jsonPath("$.phoneno", is(i1.getPhoneno())));
	}
	
	@Test
	public void updateCustomerEndPointTest() throws Exception{
			mvc.perform( 
					MockMvcRequestBuilders.put("/api/customers/{id}", i1.getId())
										  .content(asJsonString(i2))
										  .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
										  ).andExpect(status().isOk())
										.andExpect(jsonPath("$", is(CoreMatchers.notNullValue())))
								        .andExpect(jsonPath("$.name", is(i2.getname())))
								        .andExpect(jsonPath("$.address", is(i2.getaddress())))
								        .andExpect(jsonPath("$.phoneno", is(i2.getPhoneno())));
		   
	}
	
	@Test
	public void deleteCustomerEndPointTest() throws Exception {
		
	    mvc.perform(
	    		MockMvcRequestBuilders.delete("/api/customers/{id}", i1.getId()))
	            .andExpect(status().isOk());
	    
	    assertFalse(custRepository.findById(i1.getId()).isPresent());
	}
	
	@Before
	public void doSetup() {
		i1=createTestEmployee("bob","Paris","999999999");
        i2=createTestEmployee("alex", "Japan","999999789");
	}
	
    @After
    public void resetDb() {
    	custRepository.deleteAll();
    }
    
    private Customer createTestEmployee(String name, String add, String phone) {
        Customer cust = new Customer();
        cust.setname(name);
        cust.setaddress(add);
        cust.setPhoneno(phone);
        return custRepository.save(cust);
    }
}
