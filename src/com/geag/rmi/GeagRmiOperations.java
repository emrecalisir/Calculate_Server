package com.geag.rmi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.scilab.modules.javasci.Scilab;

import com.geag.jscience.JScienceCalculation;
import com.geag.ocr.Tess;
import com.geag.opencv.GeagFaceDetector;
import com.geag.opencv.GeagOpenCvInitializer;
import com.geag.util.Util;
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
		System.out.println("getResponseOfFaceDetection STARTED" + data);

		Long bStartTime = 0L, bEndTime = 0L;

		bStartTime = System.currentTimeMillis();
		System.out.println("bStartTime: " + bStartTime);

		List<RectangleFace> listOfFaces = null;
		GeagFaceDetector geagFaceDetector = null;
		MatOfRect faceDetections = null;
		int numberOfFacesDetected = 0;
		String result = "";

		try {
			listOfFaces = new ArrayList<RectangleFace>();
			geagFaceDetector = new GeagFaceDetector();
			GeagOpenCvInitializer init = new GeagOpenCvInitializer();
			init.initOpenCv();

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

			System.out
					.println("getResponseOfFaceDetection COMPLETED. Number of faces: "
							+ numberOfFacesDetected
							+ " in "
							+ (bEndTime - bStartTime) + " ms");

		} catch (Exception ex) {
			System.out.println(ex);
		}
		return result;

	}

	@Override
	public String getResponseOfOcr(String imageData) {
		System.out.println("getResponseOfOcrDetection STARTED");
		Long bStartTime = 0L, bEndTime = 0L;
		bStartTime = System.currentTimeMillis();

		Util util = new Util();
		GeagOpenCvInitializer init = new GeagOpenCvInitializer();
		init.initOpenCv();
		
		BufferedImage img = util.convertImageRowDataToBufferedImage(imageData);
		Tess tess = new Tess();
		String result = tess.performOcrOperationFromBufferedImage(img);
		bEndTime = System.currentTimeMillis();
		result += ";" + (bEndTime - bStartTime);
		System.out.println("getResponseOfOcrDetection COMPLETED. Response: "
				+ result + " in " + (bEndTime - bStartTime) + " ms");
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

	public String getResponseOfMatriceMultiplicationWithJScience(double[][] a) {
		// System.out
		// .println("getResponseOfMatriceMultiplicationWithJScience STARTED");
		bStartTime = System.currentTimeMillis();
		try {

			JScienceCalculation jScienceCalculation = new JScienceCalculation();

			result = jScienceCalculation.multiplyMatrices(a, a);
			bEndTime = System.currentTimeMillis();

			result += ";" + (bEndTime - bStartTime);
			// System.out.println(result + " ms");

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		// System.out
		// .println("getResponseOfMatriceMultiplicationWithJScience COMPLETED");

		return result;
	}

}
