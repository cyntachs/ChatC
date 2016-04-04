package Client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientNet {
	// debug
	private boolean DEBUG;
	
	// Socket
	private int ServerPort;
	private InetAddress ServerAddress;
	private Socket ClientSocket;
	
	// Thread
	private ServerHandler ServerHandler;
	
	// User info
	private String AuthToken;
	private String Username;
	private String Password;
	
	// Data
	protected String Data;
	
	// Debug
	private void print(String dbg) {
		if (DEBUG) {
			System.out.println("[ClientNet]: "+dbg);
		}
	}
	
	// constructor
	public ClientNet(InetAddress addr) {
		DEBUG = true;
		
		ServerPort = 36801;
		ServerAddress = addr;
	}
	
	public void Connect(String uname, String passwd) {
		// connect to server
		try {
			ClientSocket = new Socket(ServerAddress,ServerPort);
			ClientSocket.setKeepAlive(true);
			print("client connecting to server");
		} catch (UnknownHostException e) {
			print("Unknown host");
		} catch (IOException e) {
			e.printStackTrace();
		}
		print("Client connected");
		ServerHandler = new ServerHandler(ClientSocket,DEBUG);
		ServerHandler.start();
	}
	
	// communication
	public void Send(String data) {
		ServerHandler.Send(data);
	}
	
	public boolean Ready() {
		return ServerHandler.readReady();
	}
	
	public String Receive() {
		if (Ready())
			return ServerHandler.getData();
		else
			return "";
	}
	
	// Server communications
	public HashMap<Integer,String> GetChatRooms() {
		return null;
	}
	
	public void JoinChatRoom() {
		
	}
	
	public void LeaveChatRoom(int index) {
		
	}
	
	public int GetID(String roomnumber){ // Deprecated
		return 0;
	}
}
