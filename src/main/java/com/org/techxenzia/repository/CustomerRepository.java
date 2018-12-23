package com.org.techxenzia.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.org.techxenzia.model.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("SELECT p FROM Customer p  WHERE lower(p.name) like lower(CONCAT(?1, '%'))")
    public List<Customer> findByName(String searchTerm);
	
	 /*@Query(value = "select * from customer p  WHERE lower(p.name) like lower(CONCAT(?1, '%'))",
	            nativeQuery = true
	    )
	   List<Customer> findByName(String searchTerm);*/
}
