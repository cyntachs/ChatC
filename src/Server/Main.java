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
	protected static Vector<Vector<ClientHandler>> ChatRooms;
	protected static Vector<ClientHandler> ClientHandlerThreads;
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
				print("Broadcasting to "+client.getID());
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
		//ClientHandler newClient = new ClientHandler(nextClientID,ServerSocket,debug);
		//newClient.setDaemon(true);
		//newClient.start();
		
		// client listener version
		//ClientListener Listener = new ClientListener(ServerSocket,debug);
		//Listener.setDaemon(true);
		//Listener.start();
		//print("Client listener started");
		
		print("Server routine start");
		while (!Term) {
			// Add new client
			/*if (newClient.getConnectionStatus()) {
				// add new connected client
				ClientHandlerThreads.add(newClient);
				print("Added new client");
				
				// create new client handler
				nextClientID++;
				newClient = new ClientHandler(nextClientID,ServerSocket,debug);
				newClient.setDaemon(true);
				newClient.start();
			}*/
			try {
				Socket NewClient = null;
				NewClient = ServerSocket.accept();
				NewClient.setKeepAlive(true);
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				
				ClientHandler NewClientHandler = new ClientHandler(nextClientID,NewClient,debug);
				NewClientHandler.setDaemon(true);
				NewClientHandler.start();
				
				ClientHandlerThreads.add(NewClientHandler);
				print("Added new client");
				
				nextClientID++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// remove dead threads (prevent memory leak)
			for (ClientHandler c : ClientHandlerThreads) {
				if (!c.isAlive()) {
					print("Removing thread "+c.getID()+"");
					//ClientHandlerThreads.remove(c);
				}
			}
		}
	}
}
