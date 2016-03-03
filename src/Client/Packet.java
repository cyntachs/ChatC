package Client;

import java.io.*;

public class Packet {
	public static void writePacket(int type, int cmd, int size, boolean isfrag, int fragp,String D) {
		try {
			DataOutputStream ClientOut = new DataOutputStream(ServerHandler.getClientSocket().getOutputStream());
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
		String[] retval = new String[7];
		try {
			DataInputStream ClientIn = new DataInputStream(ServerHandler.getClientSocket().getInputStream());
			byte type = ClientIn.readByte();
			byte cmd = ClientIn.readByte();
			short size = ClientIn.readShort();
			boolean isfrag = ClientIn.readBoolean();
			byte fragp = ClientIn.readByte();
			String data = ClientIn.readUTF();
			
			retval[0] = Byte.toString(type);
			retval[1] = Byte.toString(cmd);
			retval[3] = String.valueOf(size);
			retval[4] = (isfrag)? "1":"0";
			retval[5] = Byte.toString(fragp);
			retval[6] = data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retval;
	}
}
