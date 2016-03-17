package Global;

import java.net.*;
import java.io.*;

public class Packet {
	private static Socket Socket;
	private static BufferedReader In;
	private static BufferedWriter Out;
	
	public Packet(Socket Sock) {
		this.Socket = Sock;
		try {
			synchronized(Socket) {
				this.Out = new BufferedWriter(new OutputStreamWriter(Socket.getOutputStream()));
				this.In = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
				Out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean readAvailable() {
		boolean retval = false;
		try {
			synchronized(Socket) {
			retval = In.ready();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	public static void flushSocket() {
		try {
			synchronized(Socket) {
			In.skip(9999);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writePacket(int type, int cmd, int size, boolean isfrag, int fragp,String D) {
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
			
			synchronized(Socket){
			Out.write(out);
			Out.newLine();
			Out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] readPacket() {
		if (!readAvailable()) return null;
		String[] retval = new String[6];
		try {
			String raw = null;
			
			synchronized(Socket) {
			raw = In.readLine();
			}
			
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
