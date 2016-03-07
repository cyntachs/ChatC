package Server;

import java.io.*;

public class Packet {
	
	public static boolean readAvailable() {
		DataInputStream ClientIn;
		boolean retval = false;
		try {
			ClientIn = new DataInputStream(ClientHandler.getClientSocket().getInputStream());
			 retval = (ClientIn.available() > 0)? true:false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
	
	public static void flushSocket() {
		try {
			DataInputStream ClientIn = new DataInputStream(ClientHandler.getClientSocket().getInputStream());
			ClientIn.skip(9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writePacket(int type, int cmd, int size, boolean isfrag, int fragp,String D) {
		try {
			DataOutputStream ClientOut = new DataOutputStream(ClientHandler.getClientSocket().getOutputStream());
			
			ClientOut.writeByte(type); // message type
			ClientOut.writeByte(cmd); // command
			ClientOut.writeShort(size); // data size
			ClientOut.writeBoolean(isfrag); // is fragmented
			ClientOut.writeByte(fragp); // fragment part #
			ClientOut.writeUTF(D); // data
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String[] readPacket() {
		if (!readAvailable()) return null;
		String[] retval = new String[6];
		try {
			DataInputStream ClientIn = new DataInputStream(ClientHandler.getClientSocket().getInputStream());
			
			byte type = ClientIn.readByte();
			byte cmd = ClientIn.readByte();
			int size = ClientIn.readShort();
			boolean isfrag = ClientIn.readBoolean();
			byte fragp = ClientIn.readByte();
			String data = ClientIn.readUTF();
			
			retval[0] = Byte.toString(type);
			retval[1] = Byte.toString(cmd);
			retval[2] = String.valueOf(size);
			retval[3] = (isfrag)? "1":"0";
			retval[4] = Byte.toString(fragp);
			retval[5] = data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
}
