package pl.jjkrol.proz.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;

import pl.jjkrol.proz.events.PROZEvent;

/**
 * The Class InvoicesTab.
 *
 * @author   jjkrol
 */
public class InvoicesTab extends SpecificTab {
	
	/** The name. */
	private String name = "Faktury";

	/** The blocking queue. */
	private BlockingQueue<PROZEvent> blockingQueue;
	
	/**
	 * Instantiates a new invoices tab.
	 *
	 * @param blockingQueue the blocking queue
	 */
	public InvoicesTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JPanel getJPanel() {
		JPanel panel = new JPanel();
		return panel;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getReady() {

	}
}
