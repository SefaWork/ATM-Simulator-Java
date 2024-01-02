package atm;

import java.util.HashMap;
import java.util.Locale;

/**
 * Database for the ATM.
 */
public class ATMDatabase {
	private HashMap<String, Account> accounts;
	
	public ATMDatabase() {
		accounts = new HashMap<String, Account>();
		
		this.addAccount(new Account("Sefa Toksoy", "1234", 325d));
		this.addAccount(new Account("Test", "12345", 95d, 100d));
		this.addAccount(new Account("Hello", "44224422", 942.5d));
	}
	
	private void addAccount(Account account) {
		this.accounts.put(account.getUsername().toLowerCase(Locale.ENGLISH), account);
	}
	
	/**
	 * Fetches account only with username.
	 * @param username
	 * @return
	 */
	public Account fetchAccount(String username) {
		return accounts.get(username.toLowerCase(Locale.ENGLISH));
	}
	
	/**
	 * Fetches account with password.
	 * @param username
	 * @param password
	 * @return
	 */
	public Account fetchAccount(String username, String password) {
		Account account = fetchAccount(username);
		if(account == null || !account.checkPassword(password)) return null;
		return account;
	}
}
