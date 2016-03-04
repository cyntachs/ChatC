package Client;

import java.io.*;
import java.net.*;

public class ClientNet {
	private boolean debug;
	
	private int ServerPort;
	private InetAddress ServerAddress;
	private Socket ClientSocket;
	
	private ServerHandler ServerHandler;
	
	private void print(String dbg) {
		if (debug) {
			System.out.println("[ClientNet]: "+dbg);
		}
	}
	
	public ClientNet(InetAddress addr) {
		debug = true;
		
		ServerPort = 3680;
		ServerAddress = addr;
		
		try {
			ClientSocket = new Socket(ServerAddress,ServerPort);
			print("client connecting to server");
		} catch (UnknownHostException e) {
			print("Unknown host");
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		ServerHandler = new ServerHandler(ClientSocket,debug);
		ServerHandler.start();
	}
	
	public void send(String data) {
		
	}
	
	public void receive() {
		
	}
}
