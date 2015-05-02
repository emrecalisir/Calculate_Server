package com.geag.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.scilab.modules.javasci.Scilab;

import com.geag.jscience.JScienceCalculation;
import com.geag.opencv.GeagFaceDetector;
import com.gsu.cloud.calculator.model.RectangleFace;

public class GeagRmiOperations extends UnicastRemoteObject implements
		GeagRmiInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3773272393204013854L;
	Long bStartTime = 0L, bEndTime = 0L;
	String result = "";

	public GeagRmiOperations() throws RemoteException {
	}

	@Override
	public String getResponseOfFaceDetection(String data) {
		Long bStartTime = 0L, bEndTime = 0L;

		bStartTime = System.currentTimeMillis();
		System.out.println("bStartTime: " + bStartTime);

		List<RectangleFace> listOfFaces = null;
		GeagFaceDetector geagFaceDetector = null;
		MatOfRect faceDetections = null;
		int numberOfFacesDetected = 0;
		String result = "";

		try {
			System.out.println("getResponse called");
			listOfFaces = new ArrayList<RectangleFace>();
			geagFaceDetector = new GeagFaceDetector();
			faceDetections = geagFaceDetector.detectFaces(data);

			numberOfFacesDetected = faceDetections.toArray().length;
			System.out.println("number of faces detected: "
					+ numberOfFacesDetected);

			for (Rect rect : faceDetections.toArray()) {
				result += rect.x + "," + (rect.x + rect.width) + "," + rect.y
						+ "," + (rect.y + rect.height);
				result += ";";
			}

			bEndTime = System.currentTimeMillis();
			result += (bEndTime - bStartTime);

			System.out.println("Number of faces: " + numberOfFacesDetected
					+ " in " + (bEndTime - bStartTime) + " ms");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return result;

	}

	public String getResponseOfMatriceMultiplicationWithScilab(String data) {
		Long bStartTime = 0L, bEndTime = 0L;
		try {

			bStartTime = System.currentTimeMillis();
			System.out.println("bStartTime: " + bStartTime);

			Scilab sci = new Scilab();

			if (sci.open()) {
				/* Send a Scilab instruction */
				sci.exec("K1=int(rand(2000,2000)*10);");
				sci.exec("K2=int(rand(2000,2000)*10);");
				sci.exec("inv(inv(inv(inv(inv(inv(inv(inv(inv(inv(K1*K2))))))))));");

				sci.close();
			} else {
				System.out.println("Could not start Scilab ");
			}

			/*
			 * Can be improved by other exceptions: AlreadyRunningException,
			 * InitializationException, UndefinedVariableException,
			 * UnknownTypeException, etc
			 */
		} catch (org.scilab.modules.javasci.JavasciException e) {
			System.err.println("An exception occurred: "
					+ e.getLocalizedMessage());
		}
		bEndTime = System.currentTimeMillis();
		System.out.println((bEndTime - bStartTime) + " ms");
		return "ss";
	}

	public String getResponseOfPolynomialMultiplicationWithJScience(
			String coeffs) {
		// String coeff = "3 4 5"
		Long bStartTime = 0L, bEndTime = 0L;
		bStartTime = System.currentTimeMillis();

		String result = "";
		try {

			JScienceCalculation jScienceCalculation = new JScienceCalculation();

			jScienceCalculation.multiplyPolynomes(coeffs);
			bEndTime = System.currentTimeMillis();

			result += (bEndTime - bStartTime);
			System.out.println(result + " ms");

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return result;
	}

	public String getResponseOfMatriceMultiplicationWithJScience(double[][] a,
			double[][] b) {
		//System.out
		//		.println("getResponseOfMatriceMultiplicationWithJScience STARTED");
		bStartTime = System.currentTimeMillis();
		try {

			JScienceCalculation jScienceCalculation = new JScienceCalculation();

			result = jScienceCalculation.multiplyMatrices(a, b);
			bEndTime = System.currentTimeMillis();

			result += ";" + (bEndTime - bStartTime);
			//System.out.println(result + " ms");

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		//System.out
			//	.println("getResponseOfMatriceMultiplicationWithJScience COMPLETED");

		return result;
	}

}
