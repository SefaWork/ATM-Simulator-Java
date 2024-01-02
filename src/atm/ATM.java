package atm;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import menus.CreditMenu;
import menus.DebtMenu;
import menus.DepositMenu;
import menus.Login;
import menus.MainMenu;
import menus.SendCash;
import menus.WithdrawMenu;

@SuppressWarnings("serial")
public class ATM extends JFrame {
	
	private boolean isStarted = false;
	private ATMDatabase database;
	
	private Account currentAccount = null;
	private String currentPassword = null;
	
	private HashMap<String, Menu> menus = new HashMap<String, Menu>();
	
	public ATM() {
		this.database = new ATMDatabase();
		
		this.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(800,640));
		this.setLocationRelativeTo(null);
		
		menus.put("login", new Login(this));
		menus.put("mainMenu", new MainMenu(this));
		menus.put("withdraw", new WithdrawMenu(this));
		menus.put("deposit", new DepositMenu(this));
		menus.put("credit", new CreditMenu(this));
		menus.put("debt", new DebtMenu(this));
		menus.put("sendCash", new SendCash(this));
	}
	
	/**
	 * When called, starts the ATM.
	 */
	public void start() {
		if(isStarted) return;
		isStarted = true;
		
		this.setVisible(true);
		this.logoff();
	}
	
	public Account attemptLogin(String username, String password) {
		Account account = this.database.fetchAccount(username, password);
		if(account != null) {
			this.currentPassword = password;
			this.currentAccount = account;
			return account;
		} else {
			return null;
		}
	}
	
	public void logoff() {
		this.currentAccount = null;
		this.currentPassword = null;
		this.menus.get("login").run();
	}
	
	public void transition(String newMenu) {
		Menu menu = this.menus.get(newMenu);
		if(menu != null) {
			menu.run();
		}
	}
	
	public String getCurrentUsername() {
		return this.currentAccount.getUsername();
	}
	
	public double getCurrentBalance() {
		return this.currentAccount.getBalance(this.currentPassword);
	}
	
	public double getCurrentDebt() {
		return this.currentAccount.getDebt(this.currentPassword);
	}
	
	public boolean withdrawCash(double amount) {
		return this.currentAccount.withdraw(this.currentPassword, amount);
	}
	
	public boolean depositCash(double amount) {
		return this.currentAccount.deposit(this.currentPassword, amount);
	}
	
	public boolean takeCredit(double amount) {
		return this.currentAccount.credit(this.currentPassword, amount);
	}
	
	public boolean payDebt(double amount) {
		return this.currentAccount.payDebt(this.currentPassword, amount);
	}
	
	public boolean sendCash(double amount, String to) {
		Account target = database.fetchAccount(to);
		if(target == null) {
			return false;
		}
		
		return this.currentAccount.sendCash(this.currentPassword, amount, target);
	}
	
	public ArrayList<String> getHistory() {
		return this.currentAccount.getHistory(currentPassword);
	}
	
	public String accountExists(String accountName) {
		Account account = this.database.fetchAccount(accountName);
		if(account == null) {
			return null;
		} else {
			return account.getUsername();
		}
	}
}
