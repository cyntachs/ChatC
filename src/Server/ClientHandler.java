package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
	private boolean debug;
	private boolean ReqTerminate;
	private boolean isConnected;
	private int ThreadID;
	
	private static ServerSocket ServerSocket;
	private static Socket ClientSocket;
	
	private String RoomID;
	private String Username;
	
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
	
	public static Socket getClientSocket() {
		return ClientSocket;
	}
	
	public static ServerSocket getServerSocket() {
		return ServerSocket;
	}
	
	public static void send(String data) {
		
	}
	
	private boolean ConAuth() {
		// authentication
		String[] data = Packet.readPacket();
		if (data[0]+data[1] == "02") {
			
		}
		return true;
	}
	
	private void MessageHandler() {
		while (ReqTerminate != true) {
			String[] data = Packet.readPacket();
			if (data[0] == "0") {
				// message is a command
				Command.get(Integer.parseInt(data[1])).run();
			} else if(data[0] == "1") { // regular data
				
			}
		}
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
				if (ConAuth()) break; else return;
			} catch(SocketTimeoutException s) {
				// Socket timed out
				print("Socket timed out!");
			} catch (IOException e) {
				print("Socket exception occured!");
				e.printStackTrace();
				break;
			}
		}
		while (!ReqTerminate) {
			MessageHandler();
		}
	}
	
	protected void finalize() {
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
