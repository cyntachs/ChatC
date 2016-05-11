package Global;

import java.net.*;
import java.io.*;

public class Packet {
	private Socket Socket;
	private DataInputStream In;
	private DataOutputStream Out;
	
	public class PacketData {
		private int id;
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
				this.Out = new DataOutputStream(new BufferedOutputStream(Socket.getOutputStream()));
				this.In = new DataInputStream(new BufferedInputStream(Socket.getInputStream()));
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
			retval = (In.available()>0)? true:false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	public boolean clearSocket() {
		try {
			synchronized(Socket) {
			In.skip(9999);
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public PacketData writePacket(int type, int cmd, int size, boolean isfrag, int fragp, String D) {
		synchronized(Socket){
			// check parameters
			// limit size to 65025
			
			// write out
			try {
				Out.writeInt(type);
				Out.writeInt(cmd);
				Out.writeInt(size);
				Out.writeBoolean(isfrag);
				Out.writeInt(fragp);
				Out.writeUTF(D);
			} catch (IOException e) {
				return new PacketData("[Packet] Write exception!\n"+e.toString());
			}
			// flush
			try {
				Out.flush();
			} catch (IOException e) {
				return new PacketData("[Packet] Could not flush buffer!\n"+e.toString());
			}
		}
		String[] retval = new String[6];
		retval[0] = ""+ type;
		retval[1] = ""+ cmd;
		retval[2] = ""+ size;
		retval[3] = ""+ isfrag;
		retval[4] = ""+ fragp;
		retval[5] = ""+ D;
		return new PacketData(retval);
	}
	
	public PacketData writePacket(PacketData Data) {
		synchronized(Socket){
			// check parameters
			// limit size to 65025
			
			// write out
			try {
				Out.writeInt(Data.DataType());
				Out.writeInt(Data.Command());
				Out.writeInt(Data.Size());
				Out.writeBoolean(Data.isFragmented());
				Out.writeInt(Data.FragmentPart());
				Out.writeUTF(Data.Data());
			} catch (IOException e) {
				return new PacketData("[Packet] Write exception!\n"+e.toString());
			}
			// flush
			try {
				Out.flush();
			} catch (IOException e) {
				return new PacketData("[Packet] Could not flush buffer!\n"+e.toString());
			}
		}
		return Data;
	}
	
	public PacketData readPacket() {
		if (!readAvailable()) return null;
		String[] retval = new String[6];
		String raw = null;
		byte[] header = new byte[5];
		String headerstr = null;
		int type = -1; int cmd = -1; int size = -1; boolean isfrag = false; int fragp = -1;
		synchronized(Socket) {
			try {
				type = In.readInt();
				cmd = In.readInt();
				size = In.readInt();
				isfrag = In.readBoolean();
				fragp = In.readInt();
				raw = "" + In.readUTF();
			} catch (IOException e) {
				return new PacketData("[Packet] Read exception!\n"+e.toString());
			}
		}
		// check if header is not malformed
		if ((type == -1) || (cmd == -1) || (size == -1) || (!isfrag && fragp == -1))
			return new PacketData("Malformed Packet");
		// check if size parameter is correct
		if (size != raw.length())
			return new PacketData("Incorrect Data Size: Expected "+size+", Got "+raw.length());
		// extract data
		retval[0] = ""+ type;
		retval[1] = ""+ cmd;
		retval[2] = ""+ size;
		retval[3] = ""+ isfrag;
		retval[4] = ""+ fragp;
		retval[5] = ""+ raw;
		return new PacketData(retval);
	}
}
