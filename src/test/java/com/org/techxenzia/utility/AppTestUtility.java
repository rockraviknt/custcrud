package com.org.techxenzia.utility;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AppTestUtility {

	 public static String asJsonString(final Object obj) {
	        try {
	            return new ObjectMapper().writeValueAsString(obj);
	        } catch (Exception exp) {
	            throw new RuntimeException(exp);
	        }
	    }
}
