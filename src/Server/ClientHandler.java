package Server;

import Global.Packet;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
	// Thread
	private boolean DEBUG;
	private boolean ReqTerminate;
	private int ThreadID;
	
	// Packet read/writer
	private Packet P;
	
	// Sockets
	private Socket ClientSocket;
	private boolean isConnected;
	
	// User info
	private String Username;
	private String AuthToken;
	
	// debug
	private synchronized void print(String dbg) {
		if (DEBUG) {
			System.out.println("[CliHandl"+ThreadID+"]: "+dbg);
		}
	}
	
	// constructor
	public ClientHandler (int id,Socket Client, boolean dbg) {
		this.DEBUG = dbg;
		this.ReqTerminate = false;
		this.isConnected = false;
		this.ThreadID = id;
		
		this.ClientSocket = Client;
		
		this.P = new Packet(this.ClientSocket);
	}
	
	// access functions
	protected int getID() {return ThreadID;}
	public boolean isConnected() {return isConnected;}
	public String getToken() {return AuthToken;}
	public String getUsername() {return Username;}
	
	// communication
	protected void send(String data) { 
		// send to client
		synchronized(ClientSocket) {
			P.writePacket(1, 14, data.length(), false, 1, data);
		}
	}
	
//	private boolean ConAuth() {
//		// authentication
//		print("Client authorized");
//		isConnected = true;
//		return true;
//	}
	
	// AuthToken Generator
	private String GenerateAuthToken() {
		return "";
	}
	
	// Message Handler
	private void MessageHandler() {
		String[] data = null;
		synchronized(ClientSocket) {
			if (!ClientSocket.isConnected()){
				print("Socket unexpectedly closed!");
				return;
			}
			if (!P.readAvailable()) return;
			print("Read Available");
			
			data = P.readPacket();
			
			if (data == null || data[0] == null) {
				print("MessageHandler received invalid data");
				if (data == null) print("Data is null"); else
				if (data[0] == null) print("Data is empty");
				return;
			}
		}
		print("\nPacket Dump: ["+data[0]+"-"+data[1]+"-"+data[2]+"-"+data[3]+"-"+data[4]+"-"+data[5]+"]\n"+
				"Message Type:    "+data[0]+"\n"+
				"Command:         "+data[1]+"\n"+
				"Data Size:       "+data[2]+"\n"+
				"Is fragmented:   "+data[3]+"\n"+
				"Fragment part #: "+data[4]+"\n"+
				"[Data]\n"+data[5]);
		
		send(data[5]); // debug
		
		switch(data[0]) {
		case "0":
			// message is a command
			Command.get(Integer.parseInt(data[1])).run(null);
			break;
		case "1":
			// check for valid packet tags
			if (data[1] != "14") return;
			// broadcast to everyone else
			Command.get(Integer.parseInt(data[1])).run(new Object[]{data[5],AuthToken});
			break;
		default:
			// error
			break;
		}
	}
	
	// Main Routine
	public void run() {
		print("Client handler thread running. ID "+ThreadID);
		while (!ReqTerminate) {
			// debug
			synchronized(ClientSocket) {
				if (ClientSocket.isClosed()) {
					print("Socket closed");
					return;
				}
			}
			MessageHandler();
		}
	}
	
	// term handler
	protected void finalize() {
		print("Thread closing");
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
