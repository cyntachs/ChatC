package Client;

public class LoginThread extends Thread {
	ClientGUI thisClient;
	public LoginThread(ClientGUI client){
		thisClient = client;
	}
	@Override
	public void run() {
		while(true){
			//System.out.println("Running");
			if(thisClient.loginPane.getisLogin() == true){
				thisClient.cl.show(thisClient.panelCont, "2");
				break;
			}
		}
		this.interrupt();
	}
}
