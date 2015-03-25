package com.geag.rmi;

import java.net.Socket;

import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;
import lipermi.net.Server;


public class FaceDetectionRmiServer implements FaceDetectionRmiInterface {

	public FaceDetectionRmiServer(){
	 try {
         CallHandler callHandler = new CallHandler();
         callHandler.registerGlobal(FaceDetectionRmiInterface.class, this);
         Server server = new Server();
         server.bind(7777, callHandler);
         server.addServerListener(new IServerListener() {

             @Override
             public void clientDisconnected(Socket socket) {
                 System.out.println("Client Disconnected: " + socket.getInetAddress());
             }

             @Override
             public void clientConnected(Socket socket) {
                 System.out.println("Client Connected: " + socket.getInetAddress());
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
		return "Your data: " + data;
	}
}