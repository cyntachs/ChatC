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
	
	public void run() {
		// Authenticate
		//while(!ReqTerminate) {
			//ConAuth();
		//}
		int count = 1;
		Scanner keyin = new Scanner(System.in);
		while (!ReqTerminate) {
			keyin.nextLine();
			Packet.writePacket(1, 14, 5, false, 1, "Packet "+count);
			print("Test packet "+count+" sent");
			try {
				Thread.sleep(500); //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			try {
				synchronized(ClientSocket) {
					BufferedReader In = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
					if (In.ready()) {
						String data = In.readLine();
						print(data);
					} else {
						print("no read");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			count++;
		}
	}
	
	protected void finalize() {
		
	}
}
