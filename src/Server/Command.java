package Server;

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
			PacketData data = (PacketData) args[1];
			ClientHandler client = (ClientHandler) args[0];
			// check packet headers for security
			if ((data.DataType() != 0) && (data.Command() != 1)) {
				// error
				return;
			}
			// check request
			switch(data.Data()) {
			case "GetServerRooms": { // client asks for list of chatrooms
				// serialize ChatRooms
				String serval = "GetServerRooms;";
				for (int i = 0; i < Main.ChatRooms.size(); i++) {
					String element = "" + i + "," + (String) Main.ChatRooms.get(i).get("Room Name") + ";";
					serval += element;
				}
				// send return status packet containing the data
				client.send(serval,15);
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
			// TODO generate AuthToken
			// TODO send ACC_CON
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
			// handle request to terminate connection
		}
	},
	DATA(14) {
		public void run(Object[] args) { // handle data received from client
			// for now broadcast on room 0
			Main.Broadcast(0, ((PacketData) args[1]).Data(), ((ClientHandler) args[0]).getToken());
		}
	},
	RET_STAT(15) {
		public void run(Object[] args) {
			// handle received status
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
