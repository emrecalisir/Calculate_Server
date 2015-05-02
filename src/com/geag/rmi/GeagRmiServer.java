package com.geag.rmi;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.sf.lipermi.handler.CallHandler; 
import net.sf.lipermi.net.Server;

import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.scilab.modules.javasci.Scilab;

import com.geag.jscience.JScienceCalculation;
import com.geag.opencv.GeagFaceDetector;
import com.gsu.cloud.calculator.model.RectangleFace;

public class GeagRmiServer implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8076180666989675053L;
	private static CallHandler callHandler;
	private static Server server; 
	/**
	 * 
	 */

	public GeagRmiServer() {
		try {

			callHandler= new CallHandler();
			GeagRmiOperations geagRmiOperations = new GeagRmiOperations();
			callHandler.registerGlobal(GeagRmiInterface.class, geagRmiOperations);
			server = new Server();
			server.bind(55661, callHandler);
			/*
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
			*/
			System.out.println("Server Listening");
		} catch (Exception e) {
			System.out.println(e);

		}
	}

	
	 
}