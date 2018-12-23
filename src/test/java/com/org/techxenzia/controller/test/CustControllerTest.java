package com.org.techxenzia.controller.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.hasSize;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static com.org.techxenzia.utility.AppTestUtility.asJsonString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.techxenzia.exception.ResourceNotFoundException;
import com.org.techxenzia.model.Customer;
import com.org.techxenzia.repository.CustomerRepository;


@RunWith(SpringRunner.class)
@WebMvcTest
public class CustControllerTest {

	@Autowired
    private MockMvc mvc;
	
	@MockBean
	CustomerRepository custRepository;
	
	//String customInput="{\"id\": 1,\"name\": \"Ravi\",\"address\": \"Delhi\",\"phoneno\": \"9999999999\"}";
	
	@Test
	public void getAllcustomersTest() throws Exception{
		
		Mockito.when(custRepository.findAll()).thenReturn(
				Collections.emptyList());
		
		MvcResult mvcResult= mvc.perform(
									MockMvcRequestBuilders.get("/api/customers")
										.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
										).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
										.andReturn();
		//System.out.println(mvcResult.getResponse());
		
		Mockito.verify(custRepository).findAll();
	}
	
	@Test
	public void getCustByNameTest() throws Exception{
		String searchTerm="Ra";
		List<Customer> list=new ArrayList();
		Customer c=createSetup();
		
		list.add(c);
		Mockito.when(custRepository.findByName(searchTerm))
									.thenReturn(list);
			mvc.perform(
				MockMvcRequestBuilders.get("/api/searchCustomer?search="+searchTerm)
				.accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(c.getId().intValue())))
	            .andExpect(jsonPath("$[0].name", is(c.getname())))
	            .andExpect(jsonPath("$[0].address", is(c.getaddress())))
	            .andExpect(jsonPath("$[0].phoneno", is(c.getPhoneno())));

      Mockito.verify(custRepository, Mockito.times(1)).findByName(searchTerm);
				
	}

	@Test
	public void createCustomerTest() throws Exception{
		
		Customer mockCustomer =createSetup();
		
		Mockito.when(custRepository.save(Mockito.anyObject())).thenReturn(mockCustomer);
		
			mvc.perform( 
					MockMvcRequestBuilders.post("/api/customers")
										  .accept(MediaType.APPLICATION_JSON).content(asJsonString(mockCustomer))
										  .contentType(MediaType.APPLICATION_JSON)
										).andExpect(status().isOk())
										.andExpect(jsonPath("$.id", is(mockCustomer.getId().intValue())))
								        .andExpect(jsonPath("$.name", is(mockCustomer.getname())))
								        .andExpect(jsonPath("$.address", is(mockCustomer.getaddress())))
								        .andExpect(jsonPath("$.phoneno", is(mockCustomer.getPhoneno())));
			
			Mockito.verify(custRepository, Mockito.times(1)).save(Mockito.anyObject());
		   
	}
	
	@Test
	public void updateCustomerTest() throws Exception{
		
		Customer mockCustomer =createSetup();
		Optional <Customer> inputParam=Optional.of(mockCustomer);
		Mockito.<Optional<Customer>>when(custRepository.findById(mockCustomer.getId())).thenReturn(inputParam);
		Mockito.when(custRepository.save(mockCustomer)).thenReturn(mockCustomer);
		
			mvc.perform( 
					MockMvcRequestBuilders.put("/api/customers/{id}", mockCustomer.getId())
										  .content(asJsonString(mockCustomer))
										  .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
										  ).andExpect(status().isOk());
			
			//System.out.println("\nResponsessss---**----******"+res.getResponse().getContentAsString());
			Mockito.verify(custRepository, Mockito.times(1)).findById(mockCustomer.getId());
			Mockito.verify(custRepository, Mockito.times(1)).save(mockCustomer);
		   
	}
	
	@Test
	public void deleteCustomerTest() throws Exception {
		Customer mockCustomer =createSetup();
		Optional <Customer> inputParam=Optional.of(mockCustomer);
	    Mockito.when(custRepository.findById(mockCustomer.getId())).thenReturn(inputParam);
	    Mockito.doNothing().when(custRepository).delete(mockCustomer);
	    
	    mvc.perform(
	    		MockMvcRequestBuilders.delete("/api/customers/{id}", mockCustomer.getId()))
	            .andExpect(status().isOk());
	    Mockito.verify(custRepository, Mockito.times(1)).findById(mockCustomer.getId());
		Mockito.verify(custRepository, Mockito.times(1)).delete(mockCustomer);
	}
	

  @Test
    public void getcustByIdTest() throws Exception {
	    Customer mockCustomer =createSetup();
	    Optional <Customer> inputParam=Optional.of(mockCustomer);

        Mockito.when(custRepository.findById(mockCustomer.getId())).thenReturn(inputParam);

        mvc.perform(MockMvcRequestBuilders.get("/api/customers/{id}", mockCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(mockCustomer.getId().intValue())))
                .andExpect(jsonPath("$.name", is(mockCustomer.getname())))
                .andExpect(jsonPath("$.address", is(mockCustomer.getaddress())))
                .andExpect(jsonPath("$.phoneno", is(mockCustomer.getPhoneno())));

        Mockito.verify(custRepository, Mockito.times(1)).findById(mockCustomer.getId());
        
    }

	
	public Customer createSetup() {
		Customer c=new Customer();
		c.setname("Ravi");
		c.setId(Long.valueOf(1));
		c.setPhoneno("9999999999");
		c.setaddress("Delhi");
		
		return c;
	}
	
	
	@After
	public void verifyInteraction() {
		Mockito.verifyNoMoreInteractions(custRepository);
	}
}
