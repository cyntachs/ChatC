package Server;

public enum Command {
	PING(0) {
		public void run() {
			// Ping packet. send travel time back.
		}
	},
	REQ_STAT(1) {
		public void run() {
			// send server stat
		}
	};
	
	public abstract void run();
	
	private int num;
	Command(int n) {this.num = n;}
	
	public static Command get(int n) {
		for (Command c : values()) {
			if (n == c.num) {
				return c;
			}
		}
		return null;
	}
}
