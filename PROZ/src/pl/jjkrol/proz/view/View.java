package pl.jjkrol.proz.view;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.events.PROZEvent;

/**
 * Main class of the application view
 * @author   jjkrol
 */
public class View {
	private PROZJFrame frame;
	public List<SpecificTab> views;
	private BlockingQueue<PROZEvent>blockingQueue;
	static Logger logger = org.apache.log4j.Logger.getLogger(View.class);
	
	
	/**
	 * View constructor sets the look and feel of the application
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
	 * creates and shows the main frame
	 */
	public void startGUI(final List<SpecificTab> views) {
		frame = new PROZJFrame(blockingQueue);
		this.views = views;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setTitle("PROZ");
				frame.setDefaultCloseOperation(PROZJFrame.DO_NOTHING_ON_CLOSE);
				frame.setSize(850, 650);
				for (SpecificTab v : views) {
					frame.addTab(v);
				}
				frame.setVisible(true);
			}
		});
	}

	/**
	 * displays a message box
	 * @param message
	 */
	public void displayUserMessage(String message) {
		JOptionPane.showMessageDialog(frame, message);
	}

	/**
	 * Returns the specific tab instantiated in the view.
	 * Used by controller for invoking methods of the specific tab
	 * directly, because View as a facade would be to big.
	 *
	 * @param wantedClass the wanted class
	 * @return specificTab
	 * @throws NoSuchTabException the no such tab exception
	 */
	public SpecificTab getSpecificView(Class wantedClass) throws NoSuchTabException {
		for (SpecificTab v : views) {
			if (v.getClass() == wantedClass)
				return v;
		}
		throw new NoSuchTabException();
	}
}
