package Client;

import Global.Packet;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerHandler extends Thread {
	// Vars
	private boolean DEBUG;
	private boolean ReqTerminate;
	protected static boolean isConnected;
	
	// Socket
	private static Socket ClientSocket;
	
	// packet read/writer
	private static Packet Packet;
	
	// Data
	private String Data;
	
	// debug
	public void print(String msg) {
		if (DEBUG) {
			System.out.println("[ServerHandler]: "+msg);
		}
	}
	
	// constructor
	ServerHandler(Socket cli, boolean dbg) {
		DEBUG = dbg;
		ReqTerminate = false;
		ClientSocket = cli;
		
		Packet = new Packet(cli);
	}
	
	// access functions
	protected static Socket getClientSocket() {
		return ClientSocket;
	}
	
	// authenticate user
	public void Authenticate() {
		String logindata = "";
		Packet.writePacket(0,2,logindata.length(),false,1,logindata);
	}
	
	// send to server
	public void Send(String d) {
		Packet.writePacket(1, 14, d.length(), false, 0, d);
	}
	
	// main routine
	public void run() {
		// listen for communications
	}
	
	// term handler
	protected void finalize() {
		
	}
}
