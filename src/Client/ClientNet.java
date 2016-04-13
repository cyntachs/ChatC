package Client;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
		
		// Hash password
		try {
			MessageDigest Hash = MessageDigest.getInstance("SHA-256");
			Hash.update(passwd.getBytes("UTF-8"));
			passwd = String.format("%064x", new java.math.BigInteger(1, Hash.digest()));
		} catch (NoSuchAlgorithmException e) {e.printStackTrace();
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}
		
		// authenticate
		ServerHandler.Authenticate(uname, passwd);
	}
	
	// communication
	public void Send(String data, int rindex) {
		ServerHandler.Send(data, rindex);
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
		ServerHandler.SendCommand(1,"GetServerRooms");
		return null;
	}
	
	public void JoinChatRoom(int index) {
		
	}
	
	public void LeaveChatRoom(int index) {
		
	}
	
	public int GetID(String roomnumber){ // Deprecated
		return 0;
	}
}
