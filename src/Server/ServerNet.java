package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerNet implements Runnable {
	private int ID;
	private boolean debug;
	
	private Thread thread;
	private ServerSocket ServerSocket;
	private Vector<Socket> ClientSockets;
	private boolean ReqTerminate;
	
	//private String ServerAddress;
	//private InetAddress ServerAddress;
	private int ServerPort;
	
	private void print(String dbg) {
		if (debug) {
			System.out.println("[Debug]: "+dbg);
		}
	}
	
	ServerNet(int threadid, int port, boolean dbg) {
		debug = dbg;
		ID = threadid;
		ServerPort = port;
		ReqTerminate = false;
		print("Thread initialized");
		
		try {
			ServerSocket = new ServerSocket(ServerPort);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receive() {
		return "";
	}
	
	public void send(String Data) {
		// Send string to all clients
		for (Socket client : ClientSockets) {
			try {
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				out.writeUTF(Data);
				print("Sent \""+Data+"\" to all clients.");
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void run() {
		while(ReqTerminate != true) {
			try {
				Socket NewClient = ServerSocket.accept();
				ClientSockets.add(NewClient);
				print("Added "+NewClient.getRemoteSocketAddress()+" to client list.");
			} catch(SocketTimeoutException s) {
				// Socket timed out
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void start() {
		
	}
	
	public void stop() {
		// Close all connections to all clients
		for (Socket client : ClientSockets) {
			try {
				client.close();
			} catch (IOException e) {
				print("Could not close client!");
				e.printStackTrace();
			}
		}
		try {
			ServerSocket.close();
		} catch (IOException e) {
			print("Could not close server!");
			e.printStackTrace();
		}
	}
}
