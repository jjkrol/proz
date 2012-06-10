package pl.jjkrol.proz.view;

import javax.swing.JPanel;

/**
 * The Class SpecificTab.
 */
public abstract class SpecificTab {
	
	/** The name. */
	protected String name;
	
	/**
	 * Gets the JPanel of the tab
	 *
	 * @return the j panel
	 */
	abstract JPanel getJPanel();
	
	/**
	 * Gets the name of the tab.
	 *
	 * @return the name
	 */
	abstract String getName();
	
	/**
	 * Gets the tab ready - invoked when the tab is clicked.
	 *
	 * @return the ready
	 */
	abstract void getReady();
}
