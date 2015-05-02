package com.geag.rmi;


public class GeagRmiMain {
	public static void main(String[] args) {

		System.out.println(System.getProperty("java.library.path"));

		
		GeagRmiServer geagRmiServer = new GeagRmiServer();
		
		System.out.println(System.getProperty("java.library.path"));

	}
}