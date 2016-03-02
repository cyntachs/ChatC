package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
	private boolean debug;
	private boolean ReqTerminate;
	private boolean isConnected;
	private int ThreadID;
	
	private ServerSocket ServerSocket;
	private Socket ClientSocket;
	
	private void print(String dbg) {
		if (debug) {
			System.out.println("[ClientHandler"+ThreadID+"]: "+dbg);
		}
	}
	
	public ClientHandler (int id,ServerSocket serv, boolean dbg) {
		debug = dbg;
		ReqTerminate = false;
		isConnected = false;
		ThreadID = id;
		
		ServerSocket = serv;
	}
	
	public boolean getConnectionStatus() {
		return isConnected;
	}
	
	public Socket getClientSocket() {
		return ClientSocket;
	}
	
	private void ConAuth() {
		// authentication
		try {
			DataInputStream ClientIn = new DataInputStream(ClientSocket.getInputStream());
			byte msgType = ClientIn.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void MessageHandler() {
		
	}
	
	public void run() {
		print("Client handler thread running. ID "+ThreadID);
		while(ReqTerminate != true) {
			print("Waiting for a client...");
			try {
				Socket NewClient = ServerSocket.accept();
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				ClientSocket = NewClient;
				isConnected = true; // debug
				ConAuth();
			} catch(SocketTimeoutException s) {
				// Socket timed out
				print("Socket timed out!");
			} catch (IOException e) {
				print("Socket exception occured!");
				e.printStackTrace();
				break;
			}
		}
	}
}
