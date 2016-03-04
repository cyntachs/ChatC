package Server;

public enum Command {
	DEFAULT(-1) {
		public void run(String[] args) {
			// Unknown command
		}
	},
	PING(0) {
		public void run(String[] args) {
			// Ping packet. send travel time back.
		}
	},
	REQ_STAT(1) {
		public void run(String[] args) {
			// handle client request for server stat
		}
	},
	REQ_CON(2) {
		public void run(String[] args) {
			// handle client request for connection
			if (!ClientHandler.isConnected) {
				// authenticate client
				// isConnected = true
				// return
			}
		}
	},
	DATA(14) {
		public void run(String[] args) {
			// handle data received from client
			Main.Broadcast("", null);
		}
	};
	
	public abstract void run(String[] args);
	
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
