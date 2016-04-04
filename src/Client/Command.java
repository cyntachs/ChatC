package Client;

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
		public void run(Object[] args) {
			// handle server request for stat
		}
	},
	DEC_CON(3) {
		public void run(Object[] args) {
			// handler server decline connection
		}
	},
	ACC_CON(4) {
		public void run(Object[] args) {
			// handle server accept connection
		}
	},
	ERR_CON(5) {
		public void run(Object[] args) {
			// server sent an error connection
		}
	},
	ACK(6) {
		public void run(Object[] args) {
			// server sent ack
		}
	},
	TERM_CON(9) {
		public void run(Object[] args) {
			// handle server request to terminate connection
		}
	},
	DATA(14) {
		public void run(Object[] args) {
			// handle data received from server
			ServerHandler client = (ServerHandler) args[0];
			client.Data = (String) ((PacketData) args[1]).Data();
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
