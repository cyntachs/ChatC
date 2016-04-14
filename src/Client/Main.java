package Client;

import java.io.*;
import java.net.*;
import java.util.*;

import Client.ClientNet.AuthPoll;

// main program
public class Main {
	/*
	 *  Code is subject to change. This is only a demo.
	 */

	// main function
	public static void print(String m) {
		System.out.println("[Client]: "+m);
	}
	
	public static void main(String[] args) throws InterruptedException {
		print("Starting connection");
		try {
			// Clientnet
			ClientNet cn = new ClientNet(InetAddress.getLocalHost());
			
			// Authenticate
			AuthPoll poll = cn.Connect("user","pass");
			while (!poll.Check()){
				Thread.sleep(1);
			}
			
			// Send
			cn.Send("Testing 1 2 3.", 0);
			print("sent test message");
			print("\nListening for mesages");
			while (true) {
				Thread.sleep(10);
				if (cn.Ready()) {
					print(cn.Receive());
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
