/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import javax.swing.JOptionPane;

/**
 *
 * @author Chochu
 */
public class LoginGUI extends javax.swing.JFrame {

	private String Username;
	private String Password;
	/**
	 * Creates new form LoginGUI
	 */
	public LoginGUI() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() {

		Login_Button = new javax.swing.JButton();
		Username_Label = new javax.swing.JLabel();
		Username_Textfield = new javax.swing.JTextField();
		Password_textfield = new javax.swing.JPasswordField();
		Password_Label = new javax.swing.JLabel();
		Sendup_Button = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		Login_Button.setText("Login");
		Login_Button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				Login_ButtonActionPerformed(evt);
			}
		});

		Username_Label.setText("Username");

		Password_Label.setText("Password");

		Sendup_Button.setText("Sign Up");
        Sendup_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Sendup_ButtonActionPerformed(evt);
            }
        });
        
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(Username_Label)
								.addComponent(Password_Label))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addGroup(layout.createSequentialGroup()
												.addComponent(Login_Button)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(Sendup_Button))
												.addComponent(Username_Textfield, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
												.addComponent(Password_textfield))
												.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
						.addGap(33, 33, 33)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(Username_Label)
												.addComponent(Username_Textfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(Password_textfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(Password_Label))
												.addGap(20, 20, 20)
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(Login_Button)
														.addComponent(Sendup_Button))
														.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);

		pack();
	}// </editor-fold>                        


	private void Sendup_ButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
		if(Username_Textfield.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Please Enter a username", "No Username inserted", JOptionPane.INFORMATION_MESSAGE);

		}
		else if(new String(Password_textfield.getPassword()).equals("")){
			JOptionPane.showMessageDialog(null, "Please enter a password", "No Password inserted", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			Username = Username_Textfield.getText();   //Get the user name
			Password = new String(Password_textfield.getPassword()); //Get the password
			if(CreatedAccount()){
				JOptionPane.showMessageDialog(null, "Account is created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
			else{
				JOptionPane.showMessageDialog(null, "Unable to created account", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}                                             

	private void Login_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//Store password to Username and Password variable
		if(Username_Textfield.getText().equals("")){
			JOptionPane.showMessageDialog(null, "Please Enter a username", "No Username inserted", JOptionPane.INFORMATION_MESSAGE);

		}
		else if(new String(Password_textfield.getPassword()).equals("")){
			JOptionPane.showMessageDialog(null, "Please enter a password", "No Password inserted", JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			Username = Username_Textfield.getText();   //Get the user name
			Password = new String(Password_textfield.getPassword()); //Get the password
			if(AccountExist()){
				ClientNet clientnet = new ClientNet();
				this.setVisible(false);
				ClientGUI Client1 = new ClientGUI(clientnet);
				Client1.setVisible(true);
				Client1.setSize(800,600);
			}
		}
	}                                            
	private boolean CreatedAccount(){
		try{
			byte[] encryPass = new TripleDES().encrypt(getPassword());
			if(new CheckDatabase().createAccount(getUsername(), encryPass))
				return true;						
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,e, "Error", JOptionPane.INFORMATION_MESSAGE);
		}
		return false;
	}
	private boolean AccountExist(){
		try {
			byte[] encryPass = new TripleDES().encrypt(getPassword());
			if (new CheckDatabase().isInSystem(getUsername(), encryPass))
				return true;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,e, "Error", JOptionPane.INFORMATION_MESSAGE);

		}
		return false;
	}

	public String getUsername(){
		return Username;
	}

	public String getPassword(){
		return Password;
	}
	/**^
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LoginGUI().setVisible(true);

			}
		});
	}

	// Variables declaration - do not modify                     
	private javax.swing.JButton Login_Button;
	private javax.swing.JLabel Password_Label;
	private javax.swing.JPasswordField Password_textfield;
	private javax.swing.JButton Sendup_Button;
	private javax.swing.JLabel Username_Label;
	private javax.swing.JTextField Username_Textfield;
	// End of variables declaration                   
}
