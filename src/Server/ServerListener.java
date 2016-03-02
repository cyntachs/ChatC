package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerListener implements Runnable{
	private boolean debug;
	private boolean ReqTerminate;
	
	private ServerSocket ServerSocket;
	
	private void print(String dbg) {
		if (debug) {
			System.out.println("[Debug]: "+dbg);
		}
	}
	
	public ServerListener (ServerSocket serv, boolean dbg) {
		debug = dbg;
		ServerSocket = serv;
		ReqTerminate = false;
	}

	public void run() {
		print("Socket handler thread is running");
		while(ReqTerminate != true) {
			print("Waiting for connections...");
			try {
				Socket NewClient = ServerSocket.accept();
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				//ClientSockets.add(NewClient);
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
}
