package pl.jjkrol.proz.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;

import pl.jjkrol.proz.events.PROZEvent;

public class InvoicesTab implements SpecificTab {
	private String name = "Faktury";

	private BlockingQueue<PROZEvent> blockingQueue;
	public InvoicesTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		// TODO Auto-generated constructor stub
	}
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
		// TODO Auto-generated method stub

	}
}
