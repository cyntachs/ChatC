package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
	// Const
	private static final boolean debug = true;
	private static final int ServerPort = 3680;
	
	// vars
	private static boolean Term;
	private static int nextClientID;
	
	// sockets
	private static Vector<Vector<ClientHandler>> ChatRooms;
	private static Vector<ClientHandler> ClientHandlerThreads;
	private static ServerSocket ServerSocket;
	
	// debug print function
	private static void print (String msg) {
		if (debug) {
			System.out.println("[Server]: "+msg);
		}
	}
	
	public synchronized static void Broadcast(String data, ClientHandler except) {
		for (ClientHandler client : ClientHandlerThreads) {
			if (client != except) {
				print("Broadcastng to "+client.getID());
				client.send(data);
			}
		}
	}
	
	public static void main(String[] args) {
		Term = false;
		nextClientID = 1;
		
		// init client handler thread vector
		ClientHandlerThreads = new Vector<ClientHandler>();
		
		// init server socket
		try {
			ServerSocket = new ServerSocket(ServerPort);
			//ServerSocket.setSoTimeout(10000);
			print("Server listening in port "+ServerPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		print("Server initialized");
		
		// Server routine
		ClientHandler newClient = new ClientHandler(nextClientID,ServerSocket,true);
		newClient.setDaemon(true);
		newClient.start();
		print("Server routine start");
		while (!Term) {
			// Add new client
			if (newClient.getConnectionStatus()) {
				// add new connected client
				ClientHandlerThreads.add(newClient);
				print("Added new client");
				
				// create new client handler
				nextClientID++;
				newClient = new ClientHandler(nextClientID,ServerSocket,true);
				newClient.setDaemon(true);
				newClient.start();
			}
			
			// remove dead threads (prevent memory leak)
			for (ClientHandler c : ClientHandlerThreads) {
				if (!c.isAlive()) {
					print("Removing thread "+c.getID()+"");
					ClientHandlerThreads.remove(c);
				}
			}
		}
	}
}
