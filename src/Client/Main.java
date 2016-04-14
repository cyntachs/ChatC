package Client;

import java.io.*;
import java.net.*;
import java.util.*;

// main program
public class Main {
	/*
	 *  Code is subject to change. This is only a demo.
	 */

	// main function
	public static void print(String m) {
		System.out.println("[Client]: "+m);
	}
	
	public static void main(String[] args) {
		print("Starting connection");
		try {
			ClientNet cn = new ClientNet(InetAddress.getLocalHost());
			boolean ret = cn.Connect("user","pass");
			print(ret? "T":"F");
			cn.Send("Testing 1 2 3.", 0);
			print("sent test message");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
