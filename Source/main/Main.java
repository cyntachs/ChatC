// package
package main;

import java.awt.Container;

// imports
import javax.swing.*;

// main program
public class Main extends JFrame {
	public static final String AppName = "ChatC";
	
	// frame initialize
	public Main() {
		super(AppName);
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(null);
		setSize(500,450);
		setVisible(true);
	};
	
	// main function
	public static void main(String[] args) {
		// create frame
		Main Frame = new Main();
	}
}
