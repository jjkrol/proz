package pl.jjkrol.proz.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.WindowClosingEvent;

/**
 * A subclassed JFrame, responsible for creating the main windows of the application.
 *
 * @author   jjkrol
 */
public class PROZJFrame extends JFrame implements WindowListener{
	
	/** The core. */
	private Controller core = Controller.getInstance();
	
	/** The frame. */
	private JFrame frame;
	
	/** The tabbed pane. */
	private final JTabbedPane tabbedPane = new JTabbedPane();
	
	/** The views. */
	private final List<SpecificTab> views = new ArrayList<SpecificTab>();
	
	/** The blocking queue. */
	private final BlockingQueue<PROZEvent> blockingQueue;
	
	/** The logger. */
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	/**
	 * Instantiates a new pROZJ frame.
	 *
	 * @param blockingQueue the blocking queue
	 */
	public PROZJFrame(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		addWindowListener(this);
		createMenu();
		createTabbedPane();
	}

	/**
	 * Creates the menu.
	 */
	private void createMenu() {
		JMenuBar mb = new JMenuBar();

		mb.add(getFileMenu());
		mb.add(getEditMenu());
		mb.add(getHelpMenu());

		setJMenuBar(mb);
	}

	/**
	 * Gets the help menu.
	 *
	 * @return the help menu
	 */
	private JMenu getHelpMenu() {
		JMenu helpMenu = new JMenu("Pomoc");
		helpMenu.add("Samouczek");
		helpMenu.add("O programie");
		return helpMenu;
	}

	/**
	 * Gets the edits the menu.
	 *
	 * @return the edits the menu
	 */
	private JMenu getEditMenu() {
		JMenu editMenu = new JMenu("Edycja");
		editMenu.add("Cofnij");
		editMenu.add("Preferencje");
		return editMenu;
	}

	/**
	 * Gets the file menu.
	 *
	 * @return the file menu
	 */
	private JMenu getFileMenu() {
		JMenu fileMenu = new JMenu("Plik");
		fileMenu.add("Nowy");
		fileMenu.add("Zamknij").addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					blockingQueue.put(new WindowClosingEvent());
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		});
		return fileMenu;
	}

	/**
	 * Creates the tabbed pane.
	 */
	private void createTabbedPane() {
		tabbedPane.add("Home", getHomePanel());
		tabbedPane.addChangeListener(new ChangeListener() {
			// This method is called whenever the selected tab changes
			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane) evt.getSource();
				int sel = pane.getSelectedIndex();
				for(SpecificTab tab : views){
					
					
					if(tab.getName().equals(tabbedPane.getTitleAt(sel))){
						tab.getReady();
					}
				}
			}
		});
		add(tabbedPane);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean bool) {
		super.setVisible(bool);
	}

	/**
	 * Adds the tab.
	 *
	 * @param tab the tab
	 */
	public void addTab(SpecificTab tab) {
		logger.debug("Added view "+tab.getName());
		tabbedPane.add(tab.getName(), tab.getJPanel());
		views.add(tab);
		if(!(tab instanceof PaymentsTab || 
				tab instanceof MeasurementsTab ||
				tab instanceof OccupantsTab ||
				tab instanceof LocumsTab
				))
			tabbedPane.setEnabledAt(tabbedPane.getComponentCount()-2, false);
	}

	/**
	 * Gets the home panel.
	 *
	 * @return the home panel
	 */
	private JPanel getHomePanel() {
		JPanel homePanel = new JPanel();
		JButton b1 = new JButton("Przycisk 1"), b2 = new JButton("Przycisk 2");
		JTextField txt = new JTextField(10);



		homePanel.add(b1);
		homePanel.add(b2);
		homePanel.add(txt);
		return homePanel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			blockingQueue.put(new WindowClosingEvent());
		} catch (InterruptedException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

}
