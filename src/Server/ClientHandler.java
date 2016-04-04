package Server;

import Global.Packet;
import Global.Packet.PacketData;

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
	
	protected void send(String data, int command) {
		// send to client
		synchronized(ClientSocket) {
			P.writePacket(0, command, data.length(), false, 1, data);
		}
	}
	
	// AuthToken Generator
	private String GenerateAuthToken() {
		return "";
	}
	
	// Message Handler
	private void MessageHandler() {
		PacketData data = null;
		synchronized(ClientSocket) {
			if (!ClientSocket.isConnected()){
				print("Socket unexpectedly closed!");
				return;
			}
			if (!P.readAvailable()) return;
			print("Read Available");
			
			data = P.readPacket();
			
			if (data == null) {
				print("MessageHandler received invalid data");
				if (data == null) print("Data is null");
				//if (data[0] == null) print("Data is empty");
				return;
			}
		}
		print("\nPacket Dump: ["+data.DataType()+"-"+data.Command()+"-"+data.Size()+"-"+data.isFragmented()+"-"+data.FragmentPart()+"-"+data.Data()+"]\n"+
				"Message Type:    "+data.DataType()+"\n"+
				"Command:         "+data.Command()+"\n"+
				"Data Size:       "+data.Size()+"\n"+
				"Is fragmented:   "+data.isFragmented()+"\n"+
				"Fragment part #: "+data.FragmentPart()+"\n"+
				"[Data]\n"+data.Data());
		
		send(data.Data()); // debug
		
		switch(data.DataType()) {
		case 0:
			// message is a command
			Command.get(data.Command()).run(new Object[]{this,data});
			break;
		case 1:
			// check for valid packet tags
			if (data.Command() != 14) return;
			// broadcast to everyone else
			Command.get(14).run(new Object[]{this,data});
			break;
		default:
			// error
			print("Received Unknown Command");
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
