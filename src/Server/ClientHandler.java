package Server;

import Global.Packet;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
	private boolean debug;
	private boolean ReqTerminate;
	protected static boolean isConnected;
	private int ThreadID;
	
	private static Packet Packet;
	
	private static Socket ClientSocket;
	
	private String RoomID;
	private String Username;
	
	private synchronized void print(String dbg) {
		if (debug) {
			System.out.println("[CliHandl"+ThreadID+"]: "+dbg);
		}
	}
	
	public ClientHandler (int id,Socket Client, boolean dbg) {
		this.debug = dbg;
		this.ReqTerminate = false;
		this.isConnected = false;
		this.ThreadID = id;
		
		this.ClientSocket = Client;
		
		this.Packet = new Packet(this.ClientSocket);
	}
	
	protected int getID() {return ThreadID;}
	protected boolean getConnectionStatus() {return isConnected;}
	
	protected static void send(String data) {
		Packet.writePacket(1, 14, data.length(), false, 1, data);
	}
	
	private boolean ConAuth() {
		// authentication
		print("Client authorized");
		isConnected = true;
		return true;
	}
	
	private void MessageHandler() {
		if (!ClientSocket.isConnected()){
			print("Socket unexpectedly closed!");
			return;
		}
		String[] data = null;
		synchronized(ClientSocket) {
			if (!Packet.readAvailable()) return;
			print("Read Available");
			
			data = Packet.readPacket();
			
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
		
		switch(data[0]) {
		case "0":
			// message is a command
			Command.get(Integer.parseInt(data[1])).run(null);
			break;
		case "1":
			// check for valid packet tags
			if (data[1] != "14") return;
			// broadcast to everyone else
			Main.Broadcast(data[6],this);
			break;
		default:
			// error
			break;
		}
	}
	
	public void run() {
		print("Client handler thread running. ID "+ThreadID);
		while (!ReqTerminate) {
			if (ClientSocket.isClosed()) {
				print("Socket closed");
				return;
			}
			//MessageHandler();
			try {
				synchronized(ClientSocket) {
					BufferedReader In = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
					if (In.ready()) {
						print(In.readLine());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void finalize() {
		print("Thread closing");
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
