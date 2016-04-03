package Server;

import Global.Packet;

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
			// handle client request for server stat
		}
	},
	REQ_CON(2) {
		public void run(Object[] args) {
			// handle client request for connection
			// TODO authenticate user
			// TODO generate AuthToken
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
		public void run(Object[] args) {
			// handle data received from client
			Main.Broadcast(0, ((Packet.PacketData) args[1]).Data(), ((ClientHandler) args[0]).getToken());
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
