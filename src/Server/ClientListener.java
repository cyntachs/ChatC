package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientListener extends Thread {
	private boolean ReqTerminate;
	private boolean Debug;
	
	private ServerSocket ServerSocket;
	private int ServerPort;
	private int nextClient;
	
	private void print(String dbg) {
		if (Debug) {
			System.out.println("[CliListener]: "+dbg);
		}
	}
	
	public ClientListener(int Port, boolean dbg) {
		Debug = dbg;
		ServerPort = Port;
		try {
			ServerSocket = new ServerSocket(ServerPort);
		} catch (IOException e) {
			e.printStackTrace();
		};
		print("Server listening on port "+ServerPort);
		nextClient = 1;
	}
	
	public void run() {
		while(!ReqTerminate) {
			try {
				Socket NewClient = null;
				NewClient = ServerSocket.accept();
				NewClient.setKeepAlive(true);
				NewClient.setSoTimeout(1000);
				print("Received connection from "+NewClient.getRemoteSocketAddress()+"");
				
				ClientHandler NewClientHandler = new ClientHandler(nextClient,NewClient,Debug);
				NewClientHandler.setDaemon(true);
				NewClientHandler.start();
				synchronized(Main.ClientHandlerThreads) {
					Main.ClientHandlerThreads.add(NewClientHandler);
				}
				print("Added new client");
				
				nextClient++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
