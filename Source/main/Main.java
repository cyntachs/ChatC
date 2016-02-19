// package
package main;

//imports
import java.awt.*;
import javax.swing.*;

// main program
public class Main extends JFrame {
	/*
	 *  Code is subject to change. This is only a demo.
	 */
	public static final String AppName = "ChatC";
	
	// frame initialize
	public Main() {
		super(AppName);
		Container mContainer = getContentPane(); // get main container
		mContainer.setLayout(new BorderLayout());
		
		Container tContainer = new Container(); // top container
		tContainer.setLayout(new BorderLayout());
		
		mContainer.add(tContainer, BorderLayout.CENTER); // add top container to center of main container
		
		JTextArea txtAreaLeft = new JTextArea("LEFT (Chat history area)");
		JScrollPane LeftPane = new JScrollPane(txtAreaLeft); 
		txtAreaLeft.setEditable(false);
		
		JTextArea txtAreaRight = new JTextArea("RIGHT (Users in session)");
		JScrollPane RightPane = new JScrollPane(txtAreaRight); 
		txtAreaRight.setEditable(false);
		
		JTextArea txtAreaInput = new JTextArea("INPUT (Text input)");
		JScrollPane InputPane = new JScrollPane(txtAreaInput); 
		txtAreaInput.setEditable(false);
		
		tContainer.add(LeftPane, BorderLayout.CENTER); // add to top container
		tContainer.add(RightPane, BorderLayout.LINE_END); // add to top container
		mContainer.add(InputPane, BorderLayout.PAGE_END); // add to bottom of main container
		
		// frame properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make app close properly
		setSize(500,450);
		setVisible(true); // display frame
	};
	
	// main function
	public static void main(String[] args) {
		// create frame
		new Main();
	}
}
