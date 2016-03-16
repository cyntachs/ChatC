package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientListener extends Thread {
	private boolean ReqTerminate;
	private boolean Debug;
	
	private ServerSocket ServerSocket;
	private int nextClient;
	
	private void print(String dbg) {
		if (Debug) {
			System.out.println("[CliListener]: "+dbg);
		}
	}
	
	public ClientListener(ServerSocket Sock, boolean dbg) {
		Debug = dbg;
		ServerSocket = Sock;
		nextClient = 1;
	}
	
	public void run() {
		while(!ReqTerminate) {
			try {
				Socket NewClient = null;
				NewClient = ServerSocket.accept();
				NewClient.setKeepAlive(true);
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				
				ClientHandler NewClientHandler = new ClientHandler(nextClient,NewClient,Debug);
				NewClientHandler.setDaemon(true);
				NewClientHandler.start();
				
				Main.ClientHandlerThreads.add(NewClientHandler);
				print("Added new client");
				
				nextClient++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
