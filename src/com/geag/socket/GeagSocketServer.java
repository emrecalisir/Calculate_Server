package com.geag.socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.geag.rmi.GeagRmiOperations;

public class GeagSocketServer extends Thread {
	private ServerSocket serverSocket;

	public GeagSocketServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(100000);
	}

	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client on port "
						+ serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());
				ObjectInputStream on = new ObjectInputStream(server.getInputStream());
				double[][] a= (double[][]) on.readObject();
				GeagRmiOperations geagRmiOperations = new GeagRmiOperations();
				String response = geagRmiOperations.getResponseOfMatriceMultiplicationWithJScience(a);
				//DataInputStream in = new DataInputStream(
					//	server.getInputStream());
				//System.out.println(in.readUTF());
				DataOutputStream out = new DataOutputStream(
						server.getOutputStream());
				System.out.println(response);
				out.writeUTF(response);
				server.close();
			} catch (SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			Thread t = new GeagSocketServer(15002);
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
