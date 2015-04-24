package com.geag.rmi;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.scilab.modules.javasci.Scilab;
import org.scilab.modules.types.ScilabDouble;
import org.scilab.modules.types.ScilabType;

import com.geag.jscience.JScienceCalculation;
import com.geag.opencv.GeagFaceDetector;
import com.geag.util.Util;
import com.gsu.cloud.calculator.model.RectangleFace;

import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;
import lipermi.net.Server;

public class GeagRmiServer implements GeagRmiInterface, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9127409418776496616L;

	public GeagRmiServer() {
		try {

			CallHandler callHandler = new CallHandler();
			callHandler.registerGlobal(GeagRmiInterface.class, this);
			Server server = new Server();
			server.bind(7777, callHandler);
			server.addServerListener(new IServerListener() {

				@Override
				public void clientDisconnected(Socket socket) {
					System.out.println("Client Disconnected: "
							+ socket.getInetAddress());
				}

				@Override
				public void clientConnected(Socket socket) {
					System.out.println("Client Connected: "
							+ socket.getInetAddress());
				}
			});
			System.out.println("Server Listening");
		} catch (Exception e) {
			System.out.println(e);

		}
	}

	@Override
	public String getResponse(String data) {
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

	@Override
	public String getResponseOfScientificOperation(String data) {
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

	@Override
	public String getResponseOfJScienceOperation(double[][] a, double[][] b) {
		Long bStartTime = 0L, bEndTime = 0L;
		bStartTime = System.currentTimeMillis();

		String result = "";
		try {

			JScienceCalculation jScienceCalculation = new JScienceCalculation();

			jScienceCalculation.calculateWithJScience(a, b);
			bEndTime = System.currentTimeMillis();

			result += (bEndTime - bStartTime);
			System.out.println(result + " ms");

		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return result;
	}

}