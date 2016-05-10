package Client;
//import Client.ClientNet.AuthPoll;
public class ClientGuiListener extends Thread {
	ClientNet cn;
	//AuthPoll poll;
	public ClientGuiListener(ClientNet cnn){
		System.out.println("Thread Launch");
		cn = cnn;
	}
	@Override
	public void run() {
		System.out.println("running");
		while(true){ 
			String s1 = cn.Receive();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.print(s1);
			if(s1 != null){
				System.out.println(s1 + " Data Received");
				int getsocketstring = (int)s1.charAt(0);
				for(int x = 0; x < ClientGUI.CTPArr.size();x++){
					if( ClientGUI.CTPArr.get(x).chatRoomid == getsocketstring){
						ClientGUI.CTPArr.get(x).addStringtoChat(s1.substring(1));
					}
				}
			}
		}
		
	}
}
