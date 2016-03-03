package Client;

import java.io.*;
import java.net.*;

public class ServerHandler extends Thread {
	private boolean debug;
	private boolean ReqTerminate;
	protected static boolean isConnected;
	
	private static Socket ClientSocket;
	
	private String Data;
	
	public void print(String msg) {
		if (debug) {
			System.out.println("[ServerHandler]: "+msg);
		}
	}
	
	ServerHandler(Socket cli, boolean dbg) {
		debug = dbg;
		ReqTerminate = false;
		ClientSocket = cli;
	}
	
	protected static Socket getClientSocket() {
		return ClientSocket;
	}
	
	public void ConAuth() {
		String logindata = "";
		Packet.writePacket(0,2,logindata.length(),false,1,logindata);
	}
	
	public void run() {
		// Authenticate
		while(!ReqTerminate) {
			//ConAuth();
		}
	}
	
	protected void finalize() {
		
	}
}
