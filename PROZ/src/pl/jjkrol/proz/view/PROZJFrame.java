package pl.jjkrol.proz.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

/**
 * A subclassed JFrame, responsible for creating
 * the main windows of the application
 * @author jjkrol
 *
 */
public class PROZJFrame extends JFrame {
	private Controller core = Controller.getInstance();
	private JFrame frame;
	private final JTabbedPane tabbedPane = new JTabbedPane();
	private final List<SpecificTab> views = new ArrayList<SpecificTab>();
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	public PROZJFrame() {
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
				System.exit(0);
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
				for(SpecificTab v : views){
					if(v.getName().equals(tabbedPane.getTitleAt(sel))){
						v.getReady();
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

	public void addView(SpecificTab view) {
		logger.debug("Added view "+view.getName());
		tabbedPane.add(view.getName(), view.getJPanel());
		views.add(view);
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

}
