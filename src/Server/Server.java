package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	// -------------------- Variables -------------------- //
	
	// Const
	private static final boolean DEBUG = true;
	private static final int ServerPort = 36801;
	
	// vars
	private static boolean Term;
	private static int NextClientID;
	
	// Chat room system
	protected static int NextRoomIndex;
	protected static HashMap<Integer,HashMap<String,Object>> ChatRooms; // {"Room Index",{room info, eg: room name, room owner}}
	protected static HashMap<Integer,Vector<String>> ChatRooms_Members; // {"Room Index",{Auth Tokens}}
	protected static Vector<ClientHandler> ClientHandlerThreads; // threads
	
	// Sockets
	private static ServerSocket ServerSocket;
	
	// -------------------- Functions -------------------- //
	
	// debug print function
	private static void print (String msg) {
		if (DEBUG) {
			System.out.println("[Server]: "+msg);
		}
	}
	
	// Get Username from AuthToken
	private static String GetUsername(String au) {
		for (ClientHandler c : ClientHandlerThreads) {
			if (c.getToken() == au) 
				return c.getUsername();
		}
		return "";
	}
	
	// Check for AuthToken Conflict
	protected synchronized static boolean CheckAuthTokenUsed(String au) {
		for (ClientHandler c : ClientHandlerThreads) {
			if (c.getToken() == au)
				return true;
		}
		return false;
	}
	
	// Room management
	public synchronized static void CreateRoom(String name, String own) {
		HashMap<String,Object> Room = new HashMap<String,Object>();
		Room.put("Room Name", name);
		Room.put("Owner", own); // AuthToken of owner
		
		ChatRooms.put(NextRoomIndex, Room);
		ChatRooms_Members.put(NextRoomIndex, new Vector<String>());
		
		NextRoomIndex++;
	}
	
	public synchronized static void DestroyRoom(int index) {
		// TODO
	}
	
	public synchronized static void AddMember(int index, String au) {
		ChatRooms_Members.get(index).add(au);
	}
	
	public synchronized static void RemoveMember(int index, String au) {
		if (ChatRooms_Members.get(index).contains(au)) {
			int i = ChatRooms_Members.get(index).indexOf(au);
			ChatRooms_Members.get(index).remove(i);
		}
	}
	
	// room broadcast
	// TODO handle html formatted text
	public synchronized static void Broadcast(int index, String msg, String from) { // from is an AuthToken
		print("[Broadcast] Room: "+index+"  Message: "+msg+"  From: "+from);
		for (ClientHandler c : ClientHandlerThreads) {
			if (ChatRooms_Members.get(index).contains(c.getToken())) {
				if (c.getToken() == from) continue;
				String uname = GetUsername(from);
				c.Send(msg);
			}
		}
	}
	
	// global broadcast
	// TODO handle html formatted text
	public synchronized static void GlobalBroadcast(int index, String msg, String from) { // from is an AuthToken
		print("[Global Broadcast] Room: "+index+"  Message: "+msg+"  From: "+from);
		for (ClientHandler c : ClientHandlerThreads) {
			String uname = GetUsername(from);
			c.Send(msg);
		}
	}
	
	// Main Routine
	public static void main(String[] args) {
		// Initializations
		Term = false;
		NextClientID = 0;
		NextRoomIndex = 0;
		
		Runtime.getRuntime().addShutdownHook(new onTerminate());
		
		// init client handler thread vector
		ClientHandlerThreads = new Vector<ClientHandler>();
		
		// init chatrooms vector
		ChatRooms = new HashMap<Integer,HashMap<String,Object>>();
		
		// init chatrooms_member vector
		ChatRooms_Members = new HashMap<Integer,Vector<String>>();
		
		// init server socket
		try {
			ServerSocket = new ServerSocket(ServerPort);
			//ServerSocket.setSoTimeout(10000);
			print("Server listening in port "+ServerPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		print("Server initialized");
		
		// debug init
		CreateRoom("Debug Room","DEBUGAUTHTOKEN");
		
		// start server routine
		print("Server routine start");
		while (!Term) {
			try {
				// accept connections
				Socket NewClient = null;
				NewClient = ServerSocket.accept();
				NewClient.setKeepAlive(true);
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				
				// create client handler thread
				ClientHandler NewClientHandler = new ClientHandler(NextClientID,NewClient,DEBUG);
				NewClientHandler.setDaemon(true);
				NewClientHandler.start();
				
				// add to thread pool
				ClientHandlerThreads.add(NewClientHandler);
				print("Added new client");
				
				// increment thread ids
				NextClientID++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// remove dead threads (prevent memory leak)
			synchronized(ClientHandlerThreads) {
				for (ClientHandler c : ClientHandlerThreads) {
					if (!c.isAlive()) {
						// remove from chat rooms
						for (int key : ChatRooms_Members.keySet()) {
							RemoveMember(key, c.getToken());
						}
						// remove from pool
						print("Removing thread "+c.getID()+"");
						ClientHandlerThreads.remove(c);
					}
				}
			}
		}
	}
	
	static class onTerminate extends Thread {
		public void run() {
			print("Server terminating...");
			Term = true;
			// Stop all threads and disconnect
			synchronized(ClientHandlerThreads) {
				for (ClientHandler c : ClientHandlerThreads) {
					c.SendTerminate();
					c.Terminate();
					while (c.isAlive()) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			// wait
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Close ServerSocket
			try {
				ServerSocket.close();
			} catch (IOException e) {
				print("Can't close ServerSocket!");
				e.printStackTrace();
			}
			print("Done");
		}
	}
	
}
