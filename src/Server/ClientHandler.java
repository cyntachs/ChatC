package Server;

import Global.Packet;
import Global.Packet.PacketData;

import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.util.*;

public class ClientHandler extends Thread {
	// -------------------- Variables -------------------- //
	// Thread
	private boolean DEBUG;
	private boolean ReqTerminate;
	private int ThreadID;
	
	// Packet read/writer
	private Packet P;
	
	// Sockets
	private Socket ClientSocket;
	private boolean isConnected;
	
	// User info
	protected boolean isAuthenticated;
	private String Username;
	private String AuthToken;
	
	// idle timer
	private Timer timer;
	private static final int IdleTimerLimit = 10; // 10 minutes
	private int IdleTime;
	
	// -------------------- Functions -------------------- //
	
	// debug
	private synchronized void print(String dbg) {
		if (DEBUG) {
			System.out.println("[CliHandl"+ThreadID+"]: "+dbg);
		}
	}
	
	// constructor
	public ClientHandler (int id,Socket Client, boolean dbg) {
		this.DEBUG = dbg;
		this.ReqTerminate = false;
		this.isConnected = false;
		this.ThreadID = id;
		
		this.isAuthenticated = false;
		this.Username = "";
		this.AuthToken = "";
		
		this.ClientSocket = Client;
		
		this.P = new Packet(this.ClientSocket);
		
		this.AssignToken();
		
		this.IdleTime = 0;
		timer = new Timer();
		timer.schedule(new IdleTimer(), 0, 60 * 1000);
		
		print("ClienHandler thread initialized");
	}
	
	// access functions
	protected int getID() {return ThreadID;}
	public boolean isConnected() {return isConnected;}
	public String getToken() {return AuthToken;}
	public String getUsername() {return Username;}
	
	// idle timer
	protected void ResetIdle() {
		IdleTime = 0;
	}
	
	protected void CheckIdle() {
		print("Idle time: "+IdleTime+" minutes.");
		if (IdleTime >= IdleTimerLimit) {
			print("Client been idle for too long! Closing connection.");
			// close connection && terminate thread
			SendTerminate();
			Terminate();
		}
	}
	
	class IdleTimer extends TimerTask {
		public void run() {
			IdleTime++;
			CheckIdle();
		}
	}
	
	// send message to client
	protected void Send(String data) { 
		synchronized(ClientSocket) {
			P.writePacket(1, 14, data.length(), false, 1, data);
		}
	}
	
	// Send command to client
	protected void SendCommand(int cmd, String data) {
		synchronized(ClientSocket) {
			P.writePacket(0,cmd,data.length(),false,1,data);
		}
	}
	
	// convert long int to base64
	public String toBase64(long l) {
		char[] table = {
				'0','1','2','3','4','5','6','7','8','9',
				'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
				'!','?',
		};
		String retval = "";
		long q = l;
		do {
			double d = q/64.0;
			long i = (long) d;
			double f = d - (double)i;
			retval = table[(int)(64.0*f)] + retval;
			q = i;
		} while (q > 64);
		retval = table[(int) q] + retval;
		return retval;
	}
	
	// AuthToken Generator
	private String GenerateAuthToken() {
		SecureRandom rinit = new SecureRandom();
		byte[] rn = new byte[20];
		rinit.nextBytes(rn);
		SecureRandom rand = new SecureRandom(rn);
		for (int i = 0; i < 10; i++) {
			rand.nextLong();
		}
		return toBase64(Math.abs(rand.nextLong()));
	}
	
	// Assigns thread with an Authentication Token
	protected void AssignToken() {
		String token = GenerateAuthToken();
		while (Server.CheckAuthTokenUsed(token))
			token = GenerateAuthToken();
		AuthToken = token;
	}
	
	// Error
	protected void Error_InvalidAuthToken(String err) {
		print("Client sent invalid Authentication Token! \n"+err);
		SendTerminate();
		Terminate();
	}
	
	protected void Error_UnauthorizedClientExec(String err) {
		print("Unauthorized client is trying to perform actions! \n"+err);
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
		ResetIdle();
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
		
		//Send(data.Data()); // debug
		
		switch(data.DataType()) {
		case 0:
			// message is a command
			Command.get(data.Command()).run(new Object[]{this,data});
			break;
		case 1:
			// check for valid packet tags
			if (data.Command() != 14) return;
			// broadcast to everyone else
			Command.get(14).run(new Object[]{this,data});
			break;
		default:
			// error
			print("Received Unknown Command");
			break;
		}
	}
	
	public void Terminate(){
		// Stop handling messages
		ReqTerminate = true;
	}
	
	protected void SendTerminate() {
		// send a terminate connection to client
		SendCommand(9,"");
		print("Sent TERM_CON to client");
		// wait before closing socket
		try {
			Thread.sleep(100);
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
	
	// Main Routine
	public void run() {
		print("Client handler thread running. ID "+ThreadID);
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
		print("ClientHandler Thread Ended");
		return;
	}
	
	// term handler
	protected void finalize() {
		if (DEBUG)
			print("Thread closing");
	}
}
