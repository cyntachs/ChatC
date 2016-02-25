import java.awt.*;
import javax.swing.*;


public class Frame extends JFrame {
	public static final String AppName = "ChatC";
	
	public JTextArea ChatHistory;
	public JTextArea UsersList;
	public JEditorPane Input;
	
	// frame initialize
	public Frame() {
		super(AppName);
		Container mContainer = getContentPane(); // get main container
		mContainer.setLayout(new BorderLayout());
		
		Container tContainer = new Container(); // top container
		tContainer.setLayout(new BorderLayout());
		
		mContainer.add(tContainer, BorderLayout.CENTER); // add top container to center of main container
		
		// initialize ChatHistory and prevent editing
		ChatHistory = new JTextArea("LEFT (Chat history area)");
		ChatHistory.setEditable(false);
		
		// add to scrollpane object
		JScrollPane LeftPane = new JScrollPane(ChatHistory); 
		LeftPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		
		// initialize UsersList and prevent editing
		UsersList = new JTextArea("RIGHT (Users in session)");
		UsersList.setEditable(false);
		
		// add to scrollpane object
		JScrollPane RightPane = new JScrollPane(UsersList); 
		RightPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); 
		
		// initialize Input
		Input = new JEditorPane();
		
		// add to scrollpane object and increase height
		JScrollPane InputPane = new JScrollPane(Input); 
		InputPane.setPreferredSize(new Dimension(0, 50));
		
		// add objects to frame
		tContainer.add(LeftPane, BorderLayout.CENTER); // add to top container
		tContainer.add(RightPane, BorderLayout.LINE_END); // add to top container
		mContainer.add(InputPane, BorderLayout.PAGE_END); // add to bottom of main container
		
		// frame properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make app close properly
		setSize(500,450);
		setVisible(true); // display frame
	};
}
