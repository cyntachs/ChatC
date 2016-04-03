package Server;

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
			//if (){
				// authenticate client
				// isConnected = true
				// return
			//}
		}
	},
	DATA(14) {
		public void run(Object[] args) {
			// handle data received from client
			Main.GlobalBroadcast(0, (String) args[0], (String) args[1]);
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
