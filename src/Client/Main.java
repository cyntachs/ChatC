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
			new ClientNet(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
