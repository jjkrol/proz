package pl.jjkrol.proz.view;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.events.PROZEvent;

/**
 * Main class of the application view.
 * 
 * @author jjkrol
 */
public class View {

	/** The frame. */
	private PROZJFrame frame;

	/** The views. */
	public List<SpecificTab> views = new LinkedList<SpecificTab>();

	/** The blocking queue. */
	private BlockingQueue<PROZEvent> blockingQueue;

	/** The logger. */
	static Logger logger = org.apache.log4j.Logger.getLogger(View.class);

	/**
	 * View constructor sets the look and feel of the application.
	 * 
	 * @param blockingQueue
	 *            the blocking queue
	 */
	public View(final BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;

		try {
			UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager
					.getLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			logger.warn(e.getMessage());
		}

	}

	/**
	 * creates and shows the main frame.
	 */
	public void startGUI() {
		views.add(new MeasurementsTab(blockingQueue));
		views.add(new BuildingMeasurementsTab(blockingQueue));
		views.add(new PaymentsTab(blockingQueue));
		views.add(new OccupantsTab(blockingQueue));
		views.add(new LocumsTab(blockingQueue));
		views.add(new InvoicesTab(blockingQueue));
		views.add(new ReportsTab(blockingQueue));

		frame = new PROZJFrame(blockingQueue);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setTitle("PROZ");
				frame.setDefaultCloseOperation(PROZJFrame.DO_NOTHING_ON_CLOSE);
				frame.setSize(850, 650);
				for (SpecificTab v : views) {
					frame.addTab(v);
				}
				try {
					getSpecificView(MeasurementsTab.class).getReady();
				} catch (NoSuchTabException e) {
					e.printStackTrace();
				}
				frame.setVisible(true);
			}
		});
	}

	/**
	 * displays a message box.
	 * 
	 * @param message
	 *            the message
	 */
	public void displayUserMessage(final String message) {
		JOptionPane.showMessageDialog(frame, message);
	}

	/**
	 * Returns the specific tab instantiated in the view. Used by controller for
	 * invoking methods of the specific tab directly, because View as a facade
	 * would be to big.
	 * 
	 * @param wantedClass
	 *            the wanted class
	 * @return specificTab
	 * @throws NoSuchTabException
	 *             the no such tab exception
	 */
	public SpecificTab getSpecificView(final Class wantedClass)
			throws NoSuchTabException {
		for (SpecificTab v : views) {
			if (v.getClass() == wantedClass)
				return v;
		}
		throw new NoSuchTabException();
	}
}
