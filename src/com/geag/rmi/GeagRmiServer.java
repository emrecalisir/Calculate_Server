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
		System.out.println("getResponse called and data:" + data);
		List<RectangleFace> listOfFaces = new ArrayList<RectangleFace>();
		GeagFaceDetector geagFaceDetector = new GeagFaceDetector();
		MatOfRect faceDetections = geagFaceDetector.detectFaces(data);

		int numberOfFacesDetected = faceDetections.toArray().length;
		System.out
				.println("number of faces detected: " + numberOfFacesDetected);
		String result = "";
		RectangleFace rectangleFace = null;
		for (Rect rect : faceDetections.toArray()) {
			result += rect.x + "," + (rect.x + rect.width) + "," + rect.y + ","
					+ (rect.y + rect.height);
			result += ";";
		}
		return result;

	}
}