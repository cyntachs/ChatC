package Client;
import java.io.*;
import java.net.*;

// Sockets thread class

public class ClientNet implements Runnable {
	private int ID;
	
	private Thread thread;
	private Socket cSocket;
	
	//private String ServerAddress;
	private InetAddress ServerAddress;
	private int ServerPort;
	
	ClientNet(int threadid) {
		ID = threadid;
		try {
			cSocket = new Socket(ServerAddress,ServerPort);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		
	}
	
	public void start() {
		
	}
	
	public void stop() {
		
	}
}
