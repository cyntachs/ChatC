package Global;

import java.net.*;
import java.io.*;

public class Packet {
	private Socket Socket;
	private BufferedReader In;
	private BufferedWriter Out;
	
	public class PacketData {
		private int type;
		private int command;
		private int size;
		private boolean isfragmented;
		private int fragpart;
		private String data;
		private String Error;
		
		public PacketData(String[] a) {
			type = Integer.parseInt(a[0]);
			command = Integer.parseInt(a[1]);
			size = Integer.parseInt(a[2]);
			isfragmented = (a[3] != "0");
			fragpart = Integer.parseInt(a[4]);
			data = a[5];
			Error = null;
		}
		public PacketData(String e) {Error = e;}
		
		public int DataType() {return type;}
		public int Command() {return command;}
		public int Size() {return size;}
		public boolean isFragmented() {return isfragmented;}
		public int FragmentPart() {return fragpart;}
		public String Data() {return data;}
		public String GetError() {return Error;}
	}
	
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
	
	public boolean readAvailable() {
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
	
	public void flushSocket() {
		try {
			synchronized(Socket) {
			In.skip(9999);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writePacket(int type, int cmd, int size, boolean isfrag, int fragp, String D) {
		try {
			// check parameters
			// limit size to 65025
			
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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Out.flush();
		} catch (IOException e) {
			System.out.println("[Packet] Could not flush buffer!");
			e.printStackTrace();
		}
	}
	
	public PacketData readPacket() {
		if (!readAvailable()) return null;
		String[] retval = new String[6];
		String raw = null;
		try {
			// read socket
			synchronized(Socket) {
			raw = In.readLine();
			}
		} catch (IOException e) {e.printStackTrace();}
		// check if header is not malformed
		if (raw.length() < 5) return new PacketData("Malformed Packet");
		// check if size parameter is correct
		if (((int)raw.charAt(2)) != raw.substring(5).length())
			return new PacketData("Incorrect Data Size");
		// extract data
		retval[0] = ""+ (int)raw.charAt(0);
		retval[1] = ""+ (int)raw.charAt(1);
		retval[2] = ""+ (int)raw.charAt(2);
		retval[3] = ""+ (int)raw.charAt(3);
		retval[4] = ""+ (int)raw.charAt(4);
		retval[5] = ""+ raw.substring(5);
		return new PacketData(retval);
	}
}
