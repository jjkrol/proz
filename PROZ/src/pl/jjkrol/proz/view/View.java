package pl.jjkrol.proz.view;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class of the application view
 * 
 * @author jjkrol
 */
public class View {
	private PROZJFrame frame;
	public List<SpecificTab> views;
	
	/**
	 * View constructor sets the look and feel of the application
	 */
	public View() {
  		try {
			UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager
					.getLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {

		}

	}

	/**
	 * creates and shows the main frame
	 */
	public void startGUI(final List<SpecificTab> views) {
		frame = new PROZJFrame();
		this.views = views;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setTitle("PROZ");
				frame.setDefaultCloseOperation(PROZJFrame.EXIT_ON_CLOSE);
				frame.setSize(800, 600);
				for (SpecificTab v : views) {
					frame.addView(v);
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
	 * @param wantedClass
	 * @return specificTab
	 */
	public SpecificTab getSpecificView(Class wantedClass) {
		for (SpecificTab v : views) {
			if (v.getClass() == wantedClass)
				return v;
		}
		return new OccupantsTab();
	}
}
