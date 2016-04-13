package Client;

import Global.Packet;
import Global.Packet.PacketData;
import Server.Command;

import java.io.*;
import java.net.*;

public class ServerHandler extends Thread {
	// -------------------- Variables -------------------- //
	
	// Vars
	private boolean DEBUG;
	private boolean ReqTerminate;
	protected boolean isAuthenticated;
	
	// Socket
	private Socket ClientSocket;
	
	// packet read/writer
	private Packet P;
	
	// Data
	protected String Data;
	
	// Authentication
	protected String AuthToken;
	
	// -------------------- Functions -------------------- //
	
	// debug
	public void print(String msg) {
		if (DEBUG) {
			System.out.println("[ServerHandler]: "+msg);
		}
	}
	
	// constructor
	ServerHandler(Socket cli, boolean dbg) {
		this.DEBUG = dbg;
		this.ReqTerminate = false;
		this.isAuthenticated = false;
		this.ClientSocket = cli;
		
		P = new Packet(ClientSocket);
	}
	
	// access functions
	protected Socket getClientSocket() {return ClientSocket;}
	protected boolean readReady() {return P.readAvailable();}
	protected String getData() {return Data;}
	
	// authenticate user
	public void Authenticate(String uname, String passwd) {
		String logindata = uname +"."+ passwd;
		// send login data
		synchronized(ClientSocket) {
			P.writePacket(0,2,logindata.length(),false,1,logindata);
		}
	}
	
	// send to server
	public void Send(String d, int rindex) {
		String dataf = ((char)AuthToken.length()) + AuthToken + ((char) rindex) + d;
		synchronized(ClientSocket) {
			P.writePacket(1, 14, dataf.length(), false, 1, dataf);
		}
	}
	
	// commands
	public void SendCommand(int cmd, String d) {
		String dataf = ((char)AuthToken.length()) + AuthToken + d;
		synchronized(ClientSocket) {
			P.writePacket(0, cmd, dataf.length(), false, 1, dataf);
		}
	}
	
	// Message Handler
	private void MessageHandler() {
		PacketData data = null;
		synchronized(ClientSocket) {
			if (!ClientSocket.isConnected()){
				print("Socket unexpectedly closed!");
				return;
			}
			if (!P.readAvailable()) return;
			print("Read Available");
			
			data = P.readPacket();
			
			if (data == null) {
				print("MessageHandler received invalid data");
				if (data == null) print("Data is null");
				//if (data[0] == null) print("Data is empty");
				return;
			}
		}
		print("\nPacket Dump: ["+data.DataType()+"-"+data.Command()+"-"+data.Size()+"-"+data.isFragmented()+"-"+data.FragmentPart()+"-"+data.Data()+"]\n"+
				"Message Type:    "+data.DataType()+"\n"+
				"Command:         "+data.Command()+"\n"+
				"Data Size:       "+data.Size()+"\n"+
				"Is fragmented:   "+data.isFragmented()+"\n"+
				"Fragment part #: "+data.FragmentPart()+"\n"+
				"[Data]\n"+data.Data());
		// TODO do stuff with message
		Command.get(data.Command()).run(new Object[]{this,data});
	}
	
	public void Stop() {ReqTerminate = true;}
	
	// main routine
	public void run() {
		// listen for communications
		while (!ReqTerminate){
			MessageHandler();
		}
	}
	
	// term handler
	protected void finalize() {
		if (DEBUG)
			print("Thread closing");
		// send terminate connection to server
		SendCommand(9,"");
		//wait before closing socket
		try {
			this.wait(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// close socket
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
