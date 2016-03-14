package Global;

import java.net.*;
import java.io.*;

public class Packet {
	private static BufferedReader In;
	private static BufferedWriter Out;
	
	public Packet(Socket Socket) {
		try {
			In = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
			Out = new BufferedWriter(new OutputStreamWriter(Socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public synchronized static boolean readAvailable() {
		boolean retval = false;
		try {
			 retval = In.ready();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	public synchronized static void flushSocket() {
		try {
			In.skip(9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void writePacket(int type, int cmd, int size, boolean isfrag, int fragp,String D) {
		try {
			// check parameters
			
			// create packet and send
			String out = "" + 
					(char) type +
					(char) cmd +
					(char) size +
					(char) ((isfrag)? 1:0) +
					(char) fragp + 
					D;
			Out.write(out);
			Out.newLine();
			Out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static String[] readPacket() {
		if (!readAvailable()) return null;
		String[] retval = new String[6];
		try {
			/*int type = In.read();
			int cmd = In.read();
			int size = In.read();
			int isfrag = In.read();
			int fragp = In.read();*/
			
			//char data[] = new char[size];
			//In.read(data,0,size);
			
			/*retval[0] = Integer.toString(type);
			retval[1] = Integer.toString(cmd);
			retval[2] = Integer.toString(size);
			retval[3] = Integer.toString(isfrag);
			retval[4] = Integer.toString(fragp);
			retval[5] = new String(data);*/
			
			String raw = In.readLine();
			
			retval[0] = ""+ (int)raw.charAt(0);
			retval[1] = ""+ (int)raw.charAt(1);
			retval[2] = ""+ (int)raw.charAt(2);
			retval[3] = ""+ (int)raw.charAt(3);
			retval[4] = ""+ (int)raw.charAt(4);
			retval[5] = ""+ raw.substring(5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
}
