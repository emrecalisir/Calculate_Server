package com.geag.rmi;

import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabType;

public class GeagRmiMain {
	public static void main(String[] args) {

		System.out.println(System.getProperty("java.library.path"));

		
		GeagRmiServer geagRmiServer = new GeagRmiServer();
		

	}
}