package com.gsu.cloud.calculator.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/calculator")
public class CalculatorApi {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String Triple() {
		return "xx";
	}
	
	/*
	@POST
	@Path("/calculate")
	@Consumes(MediaType.APPLICATION_JSON)
	public String Triple(@QueryParam("number") String number) {
		
		Integer number_result = 0;
		if (!number.equalsIgnoreCase("")) {
			number_result = Integer.parseInt(number);
			number_result = number_result * number_result * number_result;
		}
		return number_result.toString();

	}
*/

}
