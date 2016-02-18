// package
package main;

//imports
import java.awt.Button;
import java.awt.Container;
import javax.swing.*;

// main program
public class Main extends JFrame {
	public static final String AppName = "ChatC";
	
	// frame initialize
	public Main() {
		super(AppName);
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Container mContainer = getContentPane();
		mContainer.setLayout(new BoxLayout(mContainer, BoxLayout.Y_AXIS));
		add(new Button("1"));
		add(new Button("2"));
		setSize(500,450);
		setVisible(true);
	};
	
	// main function
	public static void main(String[] args) {
		// create frame
		Main Frame = new Main();
	}
}
