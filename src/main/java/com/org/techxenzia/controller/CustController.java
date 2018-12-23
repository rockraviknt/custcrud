package com.org.techxenzia.controller;

import com.org.techxenzia.exception.ResourceNotFoundException;
import com.org.techxenzia.model.Customer;
import com.org.techxenzia.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;


@RestController
@RequestMapping("/api")
public class CustController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CustomerRepository custRepository;

    @GetMapping("/customers")
    public List<Customer> getAllcustomers() {
    	logger.debug("Listing Customers");
        return custRepository.findAll();
    }
    
    @GetMapping("/searchCustomer")
    public List<Customer> getCustByName(@RequestParam(value = "search") String searchTerm) {
    	logger.debug("getting customers by name");
        return custRepository.findByName(searchTerm);
    }

    @PostMapping("/customers")
    public Customer createcust(@Valid @RequestBody Customer cust) {
        return custRepository.save(cust);
    }

    @GetMapping("/customers/{id}")
    public Customer getcustById(@PathVariable(value = "id") Long custId) {
        return custRepository.findById(custId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", custId));
    }

    @PutMapping("/customers/{id}")
    public Customer updatecust(@PathVariable(value = "id") Long custId,
    									@Valid @RequestBody Customer custDetails) {

        Customer cust = custRepository.findById(custId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", custId));
        cust.setname(custDetails.getname());
        cust.setaddress(custDetails.getaddress());
        cust.setPhoneno(custDetails.getPhoneno());
        Customer updatedcust = custRepository.save(cust);
        return updatedcust;
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity deletecust(@PathVariable(value = "id") Long custId) {
        Customer cust = custRepository.findById(custId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", custId));

        custRepository.delete(cust);

        return ResponseEntity.ok().build();
    }
}
