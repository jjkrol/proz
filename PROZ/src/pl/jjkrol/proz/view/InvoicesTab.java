package pl.jjkrol.proz.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;

import pl.jjkrol.proz.events.PROZEvent;

/**
 * @author   jjkrol
 */
public class InvoicesTab extends SpecificTab {
	/**
	 * @uml.property  name="name"
	 */
	private String name = "Faktury";

	private BlockingQueue<PROZEvent> blockingQueue;
	public InvoicesTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
	/**
	 * @return
	 * @uml.property  name="name"
	 */
	@Override
	public String getName() {
		return name;
	}

	@Override
	public JPanel getJPanel() {
		JPanel panel = new JPanel();
		return panel;

	}

	@Override
	public void getReady() {

	}
}
