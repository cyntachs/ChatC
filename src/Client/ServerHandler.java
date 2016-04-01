package Client;

import Global.Packet;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerHandler extends Thread {
	private boolean debug;
	private boolean ReqTerminate;
	protected static boolean isConnected;
	
	private static Socket ClientSocket;
	
	private static Packet Packet;
	
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
		
		Packet = new Packet(cli);
	}
	
	protected static Socket getClientSocket() {
		return ClientSocket;
	}
	
	public void ConAuth() {
		String logindata = "";
		Packet.writePacket(0,2,logindata.length(),false,1,logindata);
	}
	
	public void Send(String d) {
		Packet.writePacket(1, 14, d.length(), false, 0, d);
	}
	
	public void run() {
		
	}
	
	protected void finalize() {
		
	}
}
