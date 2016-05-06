package Client;
//import Client.ClientNet.AuthPoll;
public class ClientGuiListener extends Thread {
	ChatTabPane thisClient;
	ClientNet cn;
	//AuthPoll poll;
	public ClientGuiListener(ChatTabPane client,ClientNet cnn){
		thisClient = client;
		cn = cnn;
	}
	@Override
	public void run() {
		while(true){ 
			String s1 = cn.Receive();
			if(s1 != "")
				thisClient.addStringtoChat(s1);
		}
	}
}
