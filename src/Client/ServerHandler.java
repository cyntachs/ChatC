package Client;

import Global.Packet;
import Global.Packet.PacketData;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServerHandler extends Thread {
	// -------------------- Variables -------------------- //
	
	// Vars
	private boolean DEBUG;
	private boolean ReqTerminate;
	private boolean Killed;
	protected int AuthStatus; // 0 - none, 1 - authenticating, 2 - authenticated, -1 - declined
	
	// Socket
	private Socket ClientSocket;
	
	// packet read/writer
	private Packet P;
	
	// Data
	protected String Data;
	protected boolean NewData;
	protected HashMap<String,String> Info;
	
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
		this.Killed = false;
		this.AuthStatus = 0;
		this.ClientSocket = cli;
		
		Info = new HashMap<String,String>();
		
		P = new Packet(ClientSocket);
	}
	
	// access functions
	protected Socket getClientSocket() {return ClientSocket;}
	protected boolean readReady() {return P.readAvailable();}
	protected String getData() {return Data;}
	protected int getAuthStatus() {return AuthStatus;}
	public boolean isKilled() {return Killed;}
	
	// Get users
	public int RequestDone(String req) {
		return (Info.containsKey(req))? 1:0;
	}
	
	// authenticate user
	public void Authenticate(String uname, String passwd) {
		if (AuthStatus ==  2) return;
		String logindata = uname +":"+ passwd;
		// send login data
		synchronized(ClientSocket) {
			P.writePacket(0,2,logindata.length(),false,1,logindata);
		}
		AuthStatus = 1;
	}
	
	public void DeAuthenticate() {
		if (AuthStatus != 2) return;
		
		AuthStatus = 0;
	}
	
	// send to server
	public void Send(String d, int rindex) {
		if (AuthStatus != 2) return;
		String dataf = ((char)AuthToken.length()) + AuthToken + ((char) rindex) + d;
		synchronized(ClientSocket) {
			P.writePacket(1, 14, dataf.length(), false, 1, dataf);
		}
		print("Packet sent: " + dataf);
	}
	
	// commands
	public void SendCommand(int cmd, String d) {
		if (AuthStatus != 2) return;
		String dataf = ((char)AuthToken.length()) + AuthToken + d;
		synchronized(ClientSocket) {
			P.writePacket(0, cmd, dataf.length(), false, 1, dataf);
		}
		print("Command sent: " + dataf);
	}
	
	// Acknowledge
	protected void AckRcv() {
		String dataf = "ACK_RCV";
		P.writePacket(0, 6, dataf.length(), false, 1, dataf);
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
		
		// check data
		if (data.GetError() != null){
			print(data.GetError());
			return;
		}
		// debug
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
	
	public void Terminate(){
		// Stop handling messages
		ReqTerminate = true;
	}
	
	protected void SendTerminate() {
		// send a terminate connection to client
		SendCommand(9,"");
		print("Sent TERM_CON to server");
		// wait before closing socket
		try {
			this.wait(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	protected void CloseSocket() {
		// close the socket
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		print("Socket Closed!");
	}
	
	// main routine
	public void run() {
		print("Server handler thread running.");
		while (!ReqTerminate) {
			// debug
			synchronized(ClientSocket) {
				if (ClientSocket.isClosed()) {
					print("Socket closed");
					return;
				}
			}
			MessageHandler();
		}
		CloseSocket();
		print("ServerHandler Thread Ended");
		this.Killed = true;
		return;
	}
	
	// term handler
	protected void finalize() {
		if (DEBUG)
			print("Thread closing");
	}
}
