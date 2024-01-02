package menus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import atm.ATM;
import atm.Menu;

@SuppressWarnings("serial")
public class MainMenu extends Menu implements ActionListener {

	// Main
	
	private JLabel welcomeLabel;
	private JLabel welcomeSub;
	private JPanel transactions;
	
	// Balance and Debt
	
	private JPanel accountBalances;
	private JLabel balance;
	private JLabel debt;
	
	// Options
	
	private JButton deposit;
	private JButton withdraw;
	private JButton credit;
	private JButton payCredit;
	private JButton sendCash;
	private JButton quit;
	
	// Menu stuffs.
	
	private JMenuBar menuBar;
	private JMenu creditsMenu;
	private JMenuItem credits;
	
	private JMenu systemMenu;
	private JMenuItem logoff;
	private JMenuItem exit;
	
	// Event control
	
	private volatile boolean isActive = false;
	
	// History
	
	private JTextArea display;
	private JScrollPane scrollPane;
	
	public MainMenu(ATM atm) {
		super(atm);

	    display = new JTextArea(100, 108);
	    display.setEditable(false);
	    scrollPane = new JScrollPane(display);
	    scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
	    
	    scrollPane.setBounds(150, 300, 500, 200);
		
		welcomeLabel = new JLabel("Welcome, User!");
		welcomeLabel.setBounds(50, 50, 500, 50);
		welcomeLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		
		welcomeSub = new JLabel("Please select an option below:");
		welcomeSub.setBounds(65,70,500,50);
		welcomeSub.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
		
		balance = new JLabel("Current Balance: $0");
		debt = new JLabel("Current Debt: $0");
		
		transactions = new JPanel();
		transactions.setLayout(new GridLayout(2, 3, 50, 50));
		transactions.setBounds(150, 170, 500, 100);
		
		accountBalances = new JPanel();
		accountBalances.setLayout(new GridLayout(2,1,0,0));
		accountBalances.setBounds(65,120,300,30);
		
		menuBar = new JMenuBar();
		creditsMenu = new JMenu("Credits");
		credits = new JMenuItem("Sources");
		
		systemMenu = new JMenu("System");
		logoff = new JMenuItem("Logoff");
		exit = new JMenuItem("Exit");
		
		credits.addActionListener(this);
		
		creditsMenu.add(credits);
		systemMenu.add(logoff);
		systemMenu.add(exit);
		
		menuBar.add(systemMenu);
		menuBar.add(creditsMenu);
		
		
		deposit = new JButton("Deposit Cash");
		withdraw = new JButton("Withdraw Cash");
		credit = new JButton("Take Credit");
		payCredit = new JButton("Pay Debt");
		sendCash = new JButton("Send Cash");
		quit = new JButton("Log Out");
		
		deposit.addActionListener(this);
		withdraw.addActionListener(this);
		credit.addActionListener(this);
		payCredit.addActionListener(this);
		sendCash.addActionListener(this);
		quit.addActionListener(this);
		logoff.addActionListener(this);
		exit.addActionListener(this);
		
		this.setLayout(null);
		this.setSize(800,600);
		
		transactions.add(deposit);
		transactions.add(withdraw);
		transactions.add(sendCash);
		transactions.add(credit);
		transactions.add(payCredit);
		transactions.add(quit);
		transactions.setVisible(true);
		
		accountBalances.add(balance);
		accountBalances.add(debt);
		
		this.add(welcomeLabel);
		this.add(welcomeSub);
		this.add(transactions);
		this.add(accountBalances);
		this.add(scrollPane);
	}

	protected void process() {
		atm.setSize(800,600);
		atm.setTitle("Main Menu");
		atm.setJMenuBar(menuBar);
		
		welcomeLabel.setText("Welcome, " + this.atm.getCurrentUsername() + "!");
		balance.setText("Current Balance: $" + this.atm.getCurrentBalance());
		debt.setText("Current Debt: $" + this.atm.getCurrentDebt());
		
		ArrayList<String> history = this.atm.getHistory();
		String concat = "";
		int length = history.size();
		
		for(int i = length - 1; i >= 0; i--) {
			concat = concat + history.get(i) + "\n";
		}
		
		this.display.setText(concat);
		scrollPane.getVerticalScrollBar().setValue(0);
		scrollPane.getHorizontalScrollBar().setValue(0);
		
		this.render();
		
		transactions.revalidate();
		transactions.repaint();
		
		accountBalances.revalidate();
		accountBalances.repaint();
		
		this.isActive = true;
	}
	
	@Override
	protected void clean() {
		super.clean();
		atm.setJMenuBar(null);
		welcomeLabel.setText("Welcome, User!");
		balance.setText("???");
		debt.setText("???");
		display.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.isProcessing) return;
		
		Object source = e.getSource();
		String toTransition = null;
		
		double balance = this.atm.getCurrentBalance();
		double debt = this.atm.getCurrentDebt();
		
		if(source == this.credits) {
			JOptionPane.showMessageDialog(this.atm, "https://www.geeksforgeeks.org/introduction-to-java-swing/\nhttps://www.javatpoint.com/java-swing", "Sources", JOptionPane.INFORMATION_MESSAGE);
		} else if(source == this.deposit) {
			toTransition = "deposit";
		} else if(source == this.withdraw) {
			toTransition = "withdraw";
			if(balance <= 0d) {
				JOptionPane.showMessageDialog(this.atm, "Your current balance is $0. You cannot withdraw cash.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if(source == this.sendCash) {
			toTransition = "sendCash";
			if(balance <= 0d) {
				JOptionPane.showMessageDialog(this.atm, "Your current balance is $0. You cannot send cash to another account.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if(source == this.credit) {
			toTransition = "credit";
			if(debt >= 1000d) {
				JOptionPane.showMessageDialog(this.atm, "Your current debt exceeds $1000. You may not credit anymore cash.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if(source == this.payCredit) {
			toTransition = "debt";
			if(debt <= 0d) {
				JOptionPane.showMessageDialog(this.atm, "You have no debt to pay.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(balance <= 0d) {
				JOptionPane.showMessageDialog(this.atm, "Your current balance is $0. You cannot pay your debt.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else if(source == this.exit) {
			this.atm.dispose();
		} else if((source == this.quit || source == this.logoff) && this.isActive) {
			this.isActive = false;
			this.clean();
			this.atm.logoff();
		}
		
		if(toTransition != null && this.isActive) {
			this.isActive = false;
			this.clean();
			
			this.atm.transition(toTransition);
		}
	}
}
