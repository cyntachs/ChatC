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
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {e.printStackTrace();}
			return (ServerHandler.getAuthStatus() == 2)? true:false;
		}
		public boolean isDeclined() {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {e.printStackTrace();}
			return !ServerHandler.isAlive();
		}
	}
	
	public AuthPoll Connect(String uname, String passwd) {
		// connect to server if not yet connected
		if ( ((ClientSocket == null) || (ClientSocket.isClosed())) && 
				((ServerHandler == null) || (!ServerHandler.isAlive())) ) {
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
		
		// authenticate
		ServerHandler.Authenticate(uname, passwd);
		return new AuthPoll();
	}
	
	// communication
	public void Send(String data, int rindex) {
		// remove trailing spaces and newlines
		data = data.trim().replaceAll("\r?\n", "");
		ServerHandler.Send(data, rindex);
	}
	
	public boolean Ready() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {e.printStackTrace();}
		return ServerHandler.readReady();
	}
	
	public String Receive() {
		if (Ready())
			return ServerHandler.getData();
		else
			return null;
	}
	
	// Server commands
	public HashMap<Integer,String> GetChatRooms() {
		String autokenhdr = ((char)ServerHandler.AuthToken.length()) + ServerHandler.AuthToken;
		ServerHandler.SendCommand(1,autokenhdr+"GetServerRooms");
		return null;
	}
	
	public void JoinChatRoom(int index) {
		ServerHandler.SendCommand(18, "JOIN:"+index);
	}
	
	public void LeaveChatRoom(int index) {
		ServerHandler.SendCommand(18, "LEAVE:"+index);
	}
	
	// Close connection
	public void CloseConnection() {
		ServerHandler.SendTerminate();
		ServerHandler.Terminate();
	}
}
