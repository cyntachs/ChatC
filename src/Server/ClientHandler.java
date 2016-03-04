package Server;

import java.io.*;
import java.net.*;
import java.util.*;

import javafx.scene.Parent;

public class ClientHandler extends Thread {
	private boolean debug;
	private boolean ReqTerminate;
	protected static boolean isConnected;
	private int ThreadID;
	
	private static ServerSocket ServerSocket;
	private static Socket ClientSocket;
	
	private String RoomID;
	private String Username;
	
	private void print(String dbg) {
		if (debug) {
			System.out.println("[CliHandl"+ThreadID+"]: "+dbg);
		}
	}
	
	public ClientHandler (int id,ServerSocket serv, boolean dbg) {
		debug = dbg;
		ReqTerminate = false;
		isConnected = false;
		ThreadID = id;
		
		ServerSocket = serv;
	}
	
	protected int getID() {return ThreadID;}
	protected boolean getConnectionStatus() {return isConnected;}
	protected static Socket getClientSocket() {return ClientSocket;}
	protected static ServerSocket getServerSocket() {return ServerSocket;}
	
	protected static void send(String data) {
		Packet.writePacket(1, 14, data.length(), false, 1, data);
	}
	
	private boolean ConAuth() {
		// authentication
		/*String[] data = Packet.readPacket();
		if (data[0]+data[1] == "02") {
			
		}*/
		print("Client authorized");
		isConnected = true;
		return true;
	}
	
	private void MessageHandler() {
		if (!Packet.readAvailable()) return;
		print("Received message");
		
		String[] data = Packet.readPacket();
		
		if (data == null || data[0] == null) {
			print("MessageHandler received invalid data");
			if (data == null) print("Data is null"); else
			if (data[0] == null) print("Data is empty");
			Packet.flushSocket();
			return;
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
		while(ReqTerminate != true) {
			print("Waiting for a client...");
			try {
				Socket NewClient = ServerSocket.accept();
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				ClientSocket = NewClient;
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
		print("Thread closing");
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
