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
 * A subclassed JFrame, responsible for creating the main windows of the application
 * @author   jjkrol
 */
public class PROZJFrame extends JFrame implements WindowListener{
	/**
	 * @uml.property  name="core"
	 * @uml.associationEnd  
	 */
	private Controller core = Controller.getInstance();
	private JFrame frame;
	private final JTabbedPane tabbedPane = new JTabbedPane();
	private final List<SpecificTab> views = new ArrayList<SpecificTab>();
	private final BlockingQueue<PROZEvent> blockingQueue;
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	public PROZJFrame(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		addWindowListener(this);
		createMenu();
		createTabbedPane();
	}

	private void createMenu() {
		JMenuBar mb = new JMenuBar();

		mb.add(getFileMenu());
		mb.add(getEditMenu());
		mb.add(getHelpMenu());

		setJMenuBar(mb);
	}

	private JMenu getHelpMenu() {
		JMenu helpMenu = new JMenu("Pomoc");
		helpMenu.add("Samouczek");
		helpMenu.add("O programie");
		return helpMenu;
	}

	private JMenu getEditMenu() {
		JMenu editMenu = new JMenu("Edycja");
		editMenu.add("Cofnij");
		editMenu.add("Preferencje");
		return editMenu;
	}

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

	@Override
	public void setVisible(boolean bool) {
		super.setVisible(bool);
	}

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

	private JPanel getHomePanel() {
		JPanel homePanel = new JPanel();
		JButton b1 = new JButton("Przycisk 1"), b2 = new JButton("Przycisk 2");
		JTextField txt = new JTextField(10);



		homePanel.add(b1);
		homePanel.add(b2);
		homePanel.add(txt);
		return homePanel;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			blockingQueue.put(new WindowClosingEvent());
		} catch (InterruptedException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

}
