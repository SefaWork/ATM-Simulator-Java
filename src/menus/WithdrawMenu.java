package menus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import atm.ATM;
import atm.Menu;

@SuppressWarnings("serial")
public class WithdrawMenu extends Menu implements ActionListener {
	
	private JButton confirm;
	private JButton cancel;
	private JSpinner amount;
	
	private JLabel currentBalance;
	private JLabel withdrawLabel;
	
	// Menu stuffs.
	
	private JMenuBar menuBar;
	private JMenu systemMenu;
	private JMenuItem logoff;
	private JMenuItem exit;
	
	// Event control.
	private volatile boolean isActive = false;

	public WithdrawMenu(ATM atm) {
		super(atm);
		
		this.confirm = new JButton("Confirm");
		this.cancel = new JButton("Cancel");
		this.amount = new JSpinner();
		this.withdrawLabel = new JLabel("Type in the amount to withdraw.");
		this.currentBalance = new JLabel("Current Balance: $0");
		
		this.withdrawLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		this.amount.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		
		this.amount.setBounds(275, 125, 250, 50);
		this.confirm.setBounds(400, 200, 240, 25);
		this.cancel.setBounds(150, 200, 240, 25);
		this.withdrawLabel.setBounds(100,50,600,50);
		this.currentBalance.setBounds(150, 75, 500, 50);
		
		this.cancel.setBackground(new Color(255, 125, 125));
		this.confirm.setBackground(new Color(125, 255, 125));
		
		this.currentBalance.setHorizontalAlignment(JLabel.CENTER);
		this.withdrawLabel.setHorizontalAlignment(JLabel.CENTER);
		
		this.menuBar = new JMenuBar();
		this.systemMenu = new JMenu("System");
		this.logoff = new JMenuItem("Logoff");
		this.exit = new JMenuItem("Exit");
		
		this.logoff.addActionListener(this);
		this.exit.addActionListener(this);
		this.confirm.addActionListener(this);
		this.cancel.addActionListener(this);
		
		this.systemMenu.add(logoff);
		this.systemMenu.add(exit);
		this.menuBar.add(systemMenu);
		
		this.setLayout(null);
		this.setSize(800,350);
		this.add(confirm);
		this.add(amount);
		this.add(withdrawLabel);
		this.add(currentBalance);
		this.add(cancel);
	}


	protected void process() {
		double balance = this.atm.getCurrentBalance();
		
		this.amount.setValue(0d);
		this.amount.setModel(new SpinnerNumberModel(0d, 0d, balance, 1d));
		
		this.currentBalance.setText("Current Balance: $" + balance);
		
		this.atm.setSize(800,350);
		this.atm.setTitle("Withdraw");
		this.atm.setJMenuBar(menuBar);
		this.atm.getRootPane().setDefaultButton(this.confirm);
		
		this.render();
		this.isActive = true;
	}
	
	@Override
	protected void clean() {
		super.clean();
		this.atm.setJMenuBar(null);
		this.currentBalance.setText("???");
		this.amount.setValue(0);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!this.isProcessing || !this.isActive) return;
		Object source = e.getSource();
		
		if(source == this.logoff) {
			this.isActive = false;
			this.clean();
			this.atm.logoff();
		} else if(source == this.exit) {
			this.isActive = false;
			this.atm.dispose();
		} else if(source == this.confirm) {
			this.isActive = false;
			
			double toWithdraw = (double) this.amount.getValue();
			
			if(toWithdraw <= 0) {
				JOptionPane.showMessageDialog(this.atm, "Please put a valid amount to withdraw.", "Invalid Amount",JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
				this.isActive = true;
			} else {				
				int result = JOptionPane.showConfirmDialog(this.atm, "Are you sure you want to withdraw $" + toWithdraw + "?", "Confirm Withdraw", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.YES_OPTION) {
					// Handle transaction here.
					boolean withdrawResult = this.atm.withdrawCash(toWithdraw);
					if(withdrawResult) {
						JOptionPane.showMessageDialog(this.atm, "Successfully withdrew $" + toWithdraw + ". You can receive your money from the cash dispenser.", "Success", JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
						this.clean();
						this.atm.transition("mainMenu");
					} else {
						JOptionPane.showMessageDialog(this.atm, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
						this.isActive = true;
					}
				} else {
					this.isActive = true;
				}
			}
		} else if(source == this.cancel) {
			this.isActive = false;
			this.clean();
			this.atm.transition("mainMenu");
		}
	}
}
