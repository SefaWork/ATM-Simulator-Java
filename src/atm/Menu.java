package atm;

import javax.swing.JPanel;

/**
 * The menu class is an abstract class that allows sub-classes to define a sub-process
 * for transactions and similar business.
 */
@SuppressWarnings("serial")
public abstract class Menu extends JPanel {
	protected ATM atm;
	protected boolean isProcessing;
	
	public Menu(ATM atm) {
		super();
		this.atm = atm;
	}
	
	public void run() {
		this.isProcessing = true;
		this.process();
	}
	
	/**
	 * Sub-process for menu.
	 */
	protected abstract void process();
	
	/**
	 * Ran after process is completed, allows you to clean up menu elements if needed.
	 */
	protected void clean() {
		this.atm.remove(this);
		this.atm.revalidate();
		this.atm.repaint();
		this.atm.getRootPane().setDefaultButton(null);
		
		this.isProcessing = false;
	};
	
	/**
	 * Sets UI to be visible and recalculates and repaints.
	 */
	protected void render() {
		this.setVisible(true);
		this.revalidate();
		this.repaint();
		
		this.atm.add(this);
		this.atm.setLocationRelativeTo(null);
		
		this.atm.revalidate();
		this.atm.repaint();
	}
}
