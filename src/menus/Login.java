package menus;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import atm.ATM;
import atm.Account;
import atm.Menu;

@SuppressWarnings("serial")
public class Login extends Menu implements ActionListener {
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JButton confirm;
	private JLabel warningLabel;
	
	// Volatile keyword allows property to always be synchronized.
	private volatile boolean isActive = false;

	public Login(ATM atm) {
		super(atm);
		
		usernameField = new JTextField();
		passwordField = new JPasswordField();
		
		usernameLabel = new JLabel();
		passwordLabel = new JLabel();
		warningLabel = new JLabel();
		
		confirm = new JButton();
		
		warningLabel.setForeground(Color.RED);
		usernameLabel.setText("Username");
		passwordLabel.setText("Password");
		
		confirm.setText("Confirm");
		confirm.addActionListener(this);
		
		usernameField.setHorizontalAlignment(JTextField.CENTER);
		passwordField.setHorizontalAlignment(JPasswordField.CENTER);
		
		this.add(usernameLabel);
		this.add(passwordLabel);
		this.add(usernameField);
		this.add(passwordField);
		this.add(confirm);
		this.add(warningLabel);
		
		this.setLayout(new GridLayout(3, 2, 100, 0));
		this.setBounds(15,15,450,75);
	}

	protected void process() {
		this.warningLabel.setText("");
		this.usernameField.setText("");
		this.passwordField.setText("");
		
		
		atm.setSize(500,150);
		atm.setTitle("Login");
		this.render();
		
		this.atm.getRootPane().setDefaultButton(this.confirm);
		this.isActive = true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.isProcessing) return;
		
		if(e.getSource() == this.confirm) {
			String username = this.usernameField.getText();
			String password = new String(this.passwordField.getPassword());
			
			Account account = atm.attemptLogin(username, password);
			if(account == null) {
				this.warningLabel.setText("Credentials incorrect.");
			} else if(this.isActive) {
				this.isActive = false;
				this.clean();
				
				// Transition to main menu.
				this.atm.transition("mainMenu");
			}
		}
	}
}
