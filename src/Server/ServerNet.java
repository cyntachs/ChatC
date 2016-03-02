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
	public boolean ReqTerminate;
	
	//private String ServerAddress;
	//private InetAddress ServerAddress;
	private int ServerPort;
	
	private void print(String dbg) {
		if (debug) {
			System.out.println("[Debug]: "+dbg);
		}
	}
	
	ServerNet(int threadid, int port, boolean dbg) {
		// init vars
		debug = dbg;
		ID = threadid;
		ServerPort = port;
		ReqTerminate = false;
		
		// init client sockets vector
		ClientSockets = new Vector<Socket>();
		
		// init server socket 
		try {
			ServerSocket = new ServerSocket(ServerPort);
			//ServerSocket.setSoTimeout(10000);
			print("Server listening on port "+ServerPort);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		// debug
		print("Server thread initialized");
	}
	
	/*public Vector<String> receive() {
		for (Socket client : ClientSockets) {
			try {
				DataInputStream ClientInput = new DataInputStream(client.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		return new Vector<String>();
	}*/
	
	/*public void send(String Data) {
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
	}*/
	
	public void run() {
		print("Socket handler thread is running");
		while(ReqTerminate != true) {
			print("Waiting for connections...");
			try {
				Socket NewClient = ServerSocket.accept();
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				ClientSockets.add(NewClient);
				print("Added "+NewClient.getRemoteSocketAddress()+" to clients list");
			} catch(SocketTimeoutException s) {
				// Socket timed out
				print("Socket timed out!");
			} catch (IOException e) {
				print("Exception!");
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void start() {
		print("Starting server thread");
		this.run();
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
