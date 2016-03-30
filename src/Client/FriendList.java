package Client;

//Friend List GUI
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FriendList extends JFrame implements ActionListener, MouseListener{

	//card1
	JPanel JPanel1;
	JPanel JPanel2;
	JPanel JPanel3;
	JScrollPane UserOnline_ScrollPane;
	CardLayout cl;
	
	public static void main(String[] args) {
		FriendList friendList=new FriendList();

	}

	public FriendList(){

		//card1
		JPanel1 = new JPanel(new BorderLayout());   		    
		JPanel2 = new JPanel(new GridLayout(50,1,4,4));
		JLabel [] jbls1=new JLabel[5];
		for(int i=0;i<jbls1.length;i++){
			
			jbls1[i]=new JLabel( i+1+"", new ImageIcon(),JLabel.LEFT);
			jbls1[i].addMouseListener(this);
			JPanel2.add(jbls1[i]);
			
		}
		
		UserOnline_ScrollPane = new JScrollPane(JPanel2);		
		UserOnline_ScrollPane = new JScrollPane(JPanel2);	
		JPanel1.add(UserOnline_ScrollPane,"Center");
		
		
		
		cl=new CardLayout();
		this.setLayout(cl);
		this.add(JPanel1,"1");
		
		this.setSize(200,600);
		this.setVisible(true);
      
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		if(arg0.getClickCount()==2){
			String friendNo = ((JLabel)arg0.getSource()).getText();
			System.out.println("you want to talk with "+friendNo+"");
			//new QqChat (friendNo);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.red);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
		JLabel jl=(JLabel)arg0.getSource();
		jl.setForeground(Color.black);
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}

