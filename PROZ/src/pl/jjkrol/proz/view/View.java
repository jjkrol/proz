package pl.jjkrol.proz.view;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Main class of the application view
 * 
 * @author jjkrol
 */
public class View {
	private PROZJFrame frame;
	public List<SpecificTab> views;

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

	public SpecificTab getSpecificView(Class wantedClass) {
		for (SpecificTab v : views) {
			if (v.getClass() == wantedClass)
				return v;
		}
		return new OccupantsTab();
	}
}
