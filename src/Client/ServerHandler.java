package Client;

import Global.Packet;
import Global.Packet.PacketData;
import Server.Command;

import java.io.*;
import java.net.*;

public class ServerHandler extends Thread {
	// Vars
	private boolean DEBUG;
	private boolean ReqTerminate;
	protected boolean isConnected;
	
	// Socket
	private Socket ClientSocket;
	
	// packet read/writer
	private Packet P;
	
	// Data
	protected String Data;
	
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
		this.ClientSocket = cli;
		
		P = new Packet(ClientSocket);
	}
	
	// access functions
	protected Socket getClientSocket() {return ClientSocket;}
	protected boolean readReady() {return P.readAvailable();}
	protected String getData() {return Data;}
	
	// authenticate user
	public void Authenticate() {
		String logindata = "";
		synchronized(ClientSocket) {
			P.writePacket(0,2,logindata.length(),false,1,logindata);
		}
	}
	
	// send to server
	public void Send(String d) {
		synchronized(ClientSocket) {
			P.writePacket(1, 14, d.length(), false, 0, d);
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
		if (DEBUG)
			print("\nPacket Dump: ["+data.DataType()+"-"+data.Command()+"-"+data.Size()+"-"+data.isFragmented()+"-"+data.FragmentPart()+"-"+data.Data()+"]\n"+
				"Message Type:    "+data.DataType()+"\n"+
				"Command:         "+data.Command()+"\n"+
				"Data Size:       "+data.Size()+"\n"+
				"Is fragmented:   "+data.isFragmented()+"\n"+
				"Fragment part #: "+data.FragmentPart()+"\n"+
				"[Data]\n"+data.Data());
		// TODO do stuff with message
		Command.get(data.Command()).run(new Object[]{this,data});
		Data = data.Data();
	}
	
	// main routine
	public void run() {
		// listen for communications
		MessageHandler();
	}
	
	// term handler
	protected void finalize() {
		print("Thread closing");
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
