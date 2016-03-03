package Server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
	private boolean debug;
	private boolean ReqTerminate;
	private boolean isConnected;
	private int ThreadID;
	
	private ServerSocket ServerSocket;
	private Socket ClientSocket;
	
	private void print(String dbg) {
		if (debug) {
			System.out.println("[ClientHandler"+ThreadID+"]: "+dbg);
		}
	}
	
	public ClientHandler (int id,ServerSocket serv, boolean dbg) {
		debug = dbg;
		ReqTerminate = false;
		isConnected = false;
		ThreadID = id;
		
		ServerSocket = serv;
	}
	
	public boolean getConnectionStatus() {
		return isConnected;
	}
	
	public Socket getClientSocket() {
		return ClientSocket;
	}
	
	public void writePacket(int type, int cmd, int size, boolean isfrag, int fragp,String D) {
		try {
			DataOutputStream ClientOut = new DataOutputStream(ClientSocket.getOutputStream());
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
	
	public String[] readPacket() {
		String[] retval = new String[7];
		try {
			DataInputStream ClientIn = new DataInputStream(ClientSocket.getInputStream());
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
	
	private boolean ConAuth() {
		// authentication
		String[] data = readPacket();
		if (data[0]+data[1] == "02") {
			
		}
		return true;
	}
	
	private void MessageHandler() {
		while (ReqTerminate != true) {
			String[] data = readPacket();
			if (data[0] == "0") {
				// message is a command
				switch(data[1]) {
				case "0": // ping
				case "1": // REQ_STAT
				//case "2": // REQ_CON
				//case "3": // DEC_CON
				//case "4": // ACC_CON
				//case "5": // ERR_CON
				case "6": // ACK
				case "7": // ECHO
				case "8": // KEEP_ALV
				case "9": // TERM_CON
				case "10": // UPD_ULIST
				case "11": // UPD_HIST
				case "12": // RES_DATA
				case "13": // FRAG_PART
				case "14":
				default:
					break;
				}
			} else if(data[0] == "1") {
				// regular data
			}
		}
	}
	
	public void run() {
		print("Client handler thread running. ID "+ThreadID);
		while(ReqTerminate != true) {
			print("Waiting for a client...");
			try {
				Socket NewClient = ServerSocket.accept();
				print("Connection from "+NewClient.getRemoteSocketAddress()+"");
				ClientSocket = NewClient;
				isConnected = true; // debug
				if (ConAuth()) break; else return;
			} catch(SocketTimeoutException s) {
				// Socket timed out
				print("Socket timed out!");
			} catch (IOException e) {
				print("Socket exception occured!");
				e.printStackTrace();
				break;
			}
		}
		MessageHandler();
	}
	
	protected void finalize() {
		try {
			ClientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
