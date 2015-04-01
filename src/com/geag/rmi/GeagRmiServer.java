package com.geag.rmi;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

import com.geag.opencv.GeagFaceDetector;
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
		Long bStartTime= 0L, bEndTime = 0L;

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
}