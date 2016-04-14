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
	
	public class AuthPoll {
		public boolean Check() {
			return (ServerHandler.getAuthStatus() == 2)? true:false;
		}
	}
	
	public AuthPoll Connect(String uname, String passwd) {
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
		/*while (ServerHandler.getAuthStatus() >= 1) {
			//print("waiting");
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {e.printStackTrace();}
			if (ServerHandler.getAuthStatus() == 2)
				return true;
		}*/
		return new AuthPoll();
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
	
	// Server commands
	public HashMap<Integer,String> GetChatRooms() {
		String autokenhdr = ((char)ServerHandler.AuthToken.length()) + ServerHandler.AuthToken;
		ServerHandler.SendCommand(1,autokenhdr+"GetServerRooms");
		return null;
	}
	
	public void JoinChatRoom(int index) {
		
	}
	
	public void LeaveChatRoom(int index) {
		
	}
}
