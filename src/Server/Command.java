package Server;

import java.io.IOException;

import Global.Packet;
import Global.Packet.PacketData;

public enum Command {
	DEFAULT(-1) {
		public void run(Object[] args) {
			// Unknown command
		}
	},
	PING(0) {
		public void run(Object[] args) {
			// Ping packet. send travel time back.
		}
	},
	REQ_STAT(1) {
		public void run(Object[] args) {// handle client request for server stat
			String data = ((PacketData) args[1]).Data();
			String token = ((ClientHandler) args[0]).getToken();
			PacketData pdata = (PacketData) args[1];
			ClientHandler client = (ClientHandler) args[0];
			
			// check if client is authenticated
			if (!client.isAuthenticated) client.Error_UnauthorizedClientExec();
			
			// check packet headers for security
			if ((pdata.DataType() != 0) && (pdata.Command() != 1)) {
				// error
				return;
			}
			// extract AuthToken
			int aulen = (int)data.charAt(0);
			String autoken = data.substring(1, aulen+1);

			// check authtoken
			if (!autoken.equals(token)) {
				((ClientHandler) args[0]).Error_InvalidAuthToken(autoken+" != "+token);
				return;
			}
			// check request
			switch(data.substring(aulen+1)) {
			case "GetServerRooms": { // client asks for list of chatrooms
				// serialize ChatRooms
				String serval = "GetServerRooms;";
				for (int i = 0; i < Server.ChatRooms.size(); i++) {
					String element = "" + i + "," + (String) Server.ChatRooms.get(i).get("Room Name") + ";";
					serval += element;
				}
				// send return status packet containing the data
				client.SendCommand(15,serval);
				break;
			}
			default: {
				break;
			}
			}
		}
	},
	REQ_CON(2) {
		public void run(Object[] args) {
			// handle client request for connection
			// TODO authenticate user && send DEC_CON if declined
			// TODO send ACC_CON w/ AuthToken
			ClientHandler client = ((ClientHandler) args[0]);
			String data = ((PacketData) args[1]).Data();
			String token = client.getToken();
			
			// extract username and password
			String[] unpdat = data.split(":",2);
			String uname = unpdat[0];
			String passwd = unpdat[1];
			
			// search database
			boolean found = true;
			
			// if auth then send ACC_CON
			// else send DEC_CON & terminate thread
			if (found) {
				client.SendCommand(4,token);
				client.isAuthenticated = true;
			} else {
				client.SendCommand(3,"");
				client.isAuthenticated = false;
				client.Stop();
			}
			//Server.AddMember(0, token); // debug add to chatroom 0 
		}
	},
	// 3,4 sent by server never received
	ERR_CON(5) {
		public void run(Object[] args) {
			
		}
	},
	ACK(6) {
		public void run(Object[] args) {
			
		}
	},
	TERM_CON(9) {
		public void run(Object[] args) {
			// handle client request to terminate connection
			ClientHandler client = ((ClientHandler) args[0]);
			// close the socket
			client.Stop();
		}
	},
	DATA(14) {
		public void run(Object[] args) { // handle data received from client
			ClientHandler client = ((ClientHandler) args[0]);
			String data = ((PacketData) args[1]).Data();
			String token = client.getToken();
			
			// check if client is authenticated
			if (!client.isAuthenticated) client.Error_UnauthorizedClientExec();
			
			// extract AuthToken
			int aulen = (int)data.charAt(0);
			String autoken = data.substring(1, aulen+1);
			
			// check authtoken
			if (!autoken.equals(token)) {
				client.Error_InvalidAuthToken(autoken+" != "+token);
				return;
			}
			
			// extract room number from data
			int rindex = (int)data.charAt(aulen+1); // range 0-255 encoded in ascii
			
			// broadcast
			Server.Broadcast(rindex, data.substring(aulen+2), token);
		}
	},
	RET_STAT(15) {
		public void run(Object[] args) {
			// handle received status
		}
	},
	RM_CMD(18) {
		public void run(Object[] args) {
			// perform room commands
			ClientHandler client = ((ClientHandler) args[0]);
			String data = ((PacketData) args[1]).Data();
			String token = client.getToken();
			
			// check if client is authenticated
			if (!client.isAuthenticated) client.Error_UnauthorizedClientExec();
			
			// extract AuthToken
			int aulen = (int)data.charAt(0);
			String autoken = data.substring(1, aulen+1);
			
			// check authtoken
			if (!autoken.equals(token)) {
				client.Error_InvalidAuthToken(autoken+" != "+token);
				return;
			}
			
			// extract command
			String rmcmd = data.substring(aulen+1);
			String[] rmcmdp = data.split(":",2);
			String cmdpart = rmcmdp[0];
			char cmdparam = rmcmdp[1].charAt(0);
			
			// execute command
			switch(cmdpart){
			case "Join": {
				Server.AddMember((int)cmdparam, token);
				break;
			}
			case "leave": {
				Server.RemoveMember((int)cmdparam, token);
				break;
			}
			default: {
				break;
			}
			}
		}
	}
	;
	
	public abstract void run(Object[] args);
	
	private int num;
	Command(int n) {this.num = n;}
	
	public static Command get(int n) {
		for (Command c : values()) {
			if (n == c.num) {
				return c;
			}
		}
		return DEFAULT;
	}
}
