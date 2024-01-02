package atm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Account {
	
	private static DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	private double balance = 0;
	private double debt = 0;
	private String password;
	private String username;
	
	private ArrayList<String> transactionHistory = new ArrayList<String>();
	
	/**
	 * Adds a time footer to the given string and returns it.
	 * @param base
	 * @return
	 */
	private static String addCurrentTime(String base) {
		LocalDateTime now = LocalDateTime.now();
		return "[" + Account.TIME_PATTERN.format(now) + "]: " + base;
	}
	
	/**
	 * Default constructor for account.
	 * @param username
	 * @param password
	 */
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Takes in a starting balance.
	 * @param username
	 * @param password
	 * @param balance
	 */
	public Account(String username, String password, double balance) {
		this(username, password);
		this.balance = balance;
	}
	
	/**
	 * Takes in a starting balance and debt.
	 * @param username
	 * @param password
	 * @param balance
	 * @param debt
	 */
	public Account(String username, String password, double balance, double debt) {
		this(username, password, balance);
		this.debt = debt;
	}
	
	/**
	 * Checks if the password is correct. Records unsuccessful login attempts.
	 * @param password
	 * @return
	 */
	public boolean checkPassword(String password) {
		if(this.password.equals(password)) {
			return true;
		} else {
			this.transactionHistory.add(addCurrentTime("Unsuccessful login attempt."));
			return false;
		}
	}
	
	/**
	 * Gets account's name.
	 * @return
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * Gets current balance of account. Incorrect password leads to 0 balance being reported.
	 * @param password
	 * @return
	 */
	public double getBalance(String password) {
		if(this.checkPassword(password)) {			
			return this.balance;
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets current debt of account. Incorrect password leads to 0 debt being reported.
	 * @param password
	 * @return
	 */
	public double getDebt(String password) {
		if(this.checkPassword(password)) {			
			return this.debt;
		} else {
			return 0;
		}
	}

	/**
	 * Performs a withdraw operation.
	 * @param currentPassword
	 * @param amount
	 * @return Whether the operation failed or not.
	 */
	public boolean withdraw(String currentPassword, double amount) {
		if(this.checkPassword(password)) {			
			if(this.balance < amount) {
				return false;
			} else {
				this.balance -= amount;
				
				this.transactionHistory.add(addCurrentTime("Withdrew $" + amount + "."));
				
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Performs a deposit operation.
	 * @param currentPassword
	 * @param amount
	 * @return Whether the operation failed or not.
	 */
	public boolean deposit(String currentPassword, double amount) {
		if(this.checkPassword(password)) {			
			this.balance += amount;
			
			this.transactionHistory.add(addCurrentTime("Deposited $" + amount + "."));
			
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Performs a credit operation.
	 * @param currentPassword
	 * @param amount
	 * @return Whether the operation failed or not.
	 */
	public boolean credit(String currentPassword, double amount) {
		if(this.checkPassword(password) && (amount + this.debt) <= 1000d) {			
			this.balance += amount;
			this.debt += amount;
			
			this.transactionHistory.add(addCurrentTime("Credited $" + amount + "."));
			
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Performs a debt payment operation.
	 * @param currentPassword
	 * @param amount
	 * @return Whether the operation failed or not.
	 */
	public boolean payDebt(String currentPassword, double amount) {
		if(this.checkPassword(password) && amount <= this.debt) {			
			if(this.balance < amount) {
				return false;
			} else {
				this.balance -= amount;
				this.debt -= amount;
				
				this.transactionHistory.add(addCurrentTime("Paid $" + amount + " towards debt."));
				
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Performs a cash sending operation.
	 * @param currentPassword
	 * @param amount
	 * @param target
	 * @return Whether the operation failed or not.
	 */
	public boolean sendCash(String currentPassword, double amount, Account target) {
		if(this.checkPassword(password)) {			
			if(this.balance < amount) {
				return false;
			} else {
				this.balance -= amount;
				target.receiveCash(this.getUsername(), amount);
				this.transactionHistory.add(addCurrentTime("Sent $" + amount + " to " + target.getUsername() + "."));
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Retrives money sent by another account.
	 * @param sender
	 * @param amount
	 */
	public void receiveCash(String sender, double amount) {
		this.transactionHistory.add(addCurrentTime("Received $" + amount + " from " + sender + "."));
		this.balance += amount;
	}
	
	/**
	 * Gets transaction history. Returns null if password is incorrect.
	 * @param currentPassword
	 * @return
	 */
	public ArrayList<String> getHistory(String currentPassword) {
		if(this.checkPassword(currentPassword)) {
			return this.transactionHistory;
		} else {
			return null;
		}
	}
	
}
