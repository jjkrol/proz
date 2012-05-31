package pl.jjkrol.proz.view;

import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;

import pl.jjkrol.proz.events.PROZEvent;

public class ReportsTab implements SpecificTab {
	private String name = "Raporty";
	private BlockingQueue<PROZEvent> blockingQueue;

	public ReportsTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
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
