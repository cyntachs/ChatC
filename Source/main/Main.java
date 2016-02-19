// package
package main;

//imports
import java.awt.*;

import javax.swing.*;

// main program
public class Main extends JFrame {
	public static final String AppName = "ChatC";
	
	// frame initialize
	public Main() {
		super(AppName);
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Container mContainer = getContentPane();
		mContainer.setLayout(new BorderLayout());
		
		Container tContainer = new Container();
		tContainer.setLayout(new BorderLayout());
		
		mContainer.add(tContainer, BorderLayout.CENTER);
		
		JTextArea txtAreaLeft = new JTextArea("LEFT (Chat history area)"); 
		JScrollPane scrollPane = new JScrollPane(txtAreaLeft); 
		txtAreaLeft.setEditable(false);
		
		JTextArea txtAreaRight = new JTextArea("RIGHT (Users in session)"); 
		JScrollPane scrollPane2 = new JScrollPane(txtAreaRight); 
		txtAreaRight.setEditable(false);
		
		JTextArea txtAreaInput = new JTextArea("INPUT (Text input)"); 
		JScrollPane scrollPane3 = new JScrollPane(txtAreaInput); 
		txtAreaInput.setEditable(false);
		
		tContainer.add(scrollPane, BorderLayout.CENTER);
		tContainer.add(scrollPane2, BorderLayout.LINE_END);
		mContainer.add(scrollPane3, BorderLayout.PAGE_END);
		setSize(500,450);
		setVisible(true);
	};
	
	// main function
	public static void main(String[] args) {
		// create frame
		new Main();
	}
}
