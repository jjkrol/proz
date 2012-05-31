package pl.jjkrol.proz.view;
import pl.jjkrol.proz.events.*;
import pl.jjkrol.proz.events.occupants.OccupantChosenForViewingEvent;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.controller.LocumsDisplayer;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Occupant;

public class LocumsTab implements SpecificTab, LocumsDisplayer{
	private BlockingQueue<PROZEvent> blockingQueue;
	
	public LocumsTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
	
	private class ListItem {
		private String name;
		private int id;

		ListItem(String name, int id) {
			this.name = name;
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public String toString() {
			return name;
		}
	}

	private class ValueReporter implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				int id;
				try {
					id = getSelectedLocumId();
				} catch (NoNodeSelectedException e) {
					return;
				}
				internalState.valueChanged(id);
			}
		}

	}

	private class State {
		void createNewItem() { };

		void saveOccupant(int occupantId) { };
		void deleteOccupant(int occupantId){};
		void valueChanged(int id) {
			OccupantMockup moc = new OccupantMockup(id, null, null, null, null,
					null);
			try {
				blockingQueue.put(new OccupantChosenForViewingEvent(moc));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private class NormalState extends State {
		void createNewItem() {
			internalState = new EditingNewState();
			clearFields();
			addEmptyNewItem();
		}

		void saveOccupant(int occupantId) {
//			OccupantMockup moc = createMockupFromFieldData(occupantId);
//			core.putEvent(new SaveOccupantEvent(moc));
		}
		void deleteOccupant(int occupantId){
//			OccupantMockup moc = createMockupFromFieldData(occupantId);
//			core.putEvent(new DeleteOccupantEvent(moc));
			clearFields();
		}
	}

	private class EditingNewState extends State {
		void saveOccupant(int occupantId) {
//			OccupantMockup moc = createMockupFromFieldData(occupantId);
//			core.putEvent(new AddOccupantEvent(moc));
			internalState = new NormalState();
		}

		void createNewItem() {
			logger.warn("You cannot create new object while editing one");
			return;
		}
		void valueChanged(int id) {
			if (id != -1) {
				internalState = new NormalState();
				removeEmptyNewItem();
				super.valueChanged(id);
			}
		}

	}

	private State internalState = new NormalState();
	private String name = "Lokale";
	private final Controller core = Controller.getInstance();

	private JList locumsList;
	private DefaultListModel locumsListModel = new DefaultListModel();
	private final JTextField nameInput = new JTextField(30);
	private final JTextField areaInput = new JTextField(30);
	private final JTextField participationFactorInput = new JTextField(30);
	Occupant.Billing[] billingTypes = { Occupant.Billing.BILL,
			Occupant.Billing.INVOICE };
	private final JComboBox billingPersonInput = new JComboBox(billingTypes);
	private JList enabledServicesList;
	private DefaultListModel enabledServicesListModel = new DefaultListModel();
	private JList countersList;
	private DefaultListModel countersListModel = new DefaultListModel();
	private JList quotationsList;
	private DefaultListModel quotationsListModel = new DefaultListModel();
	private JList occupantsList;
	private DefaultListModel occupantsListModel = new DefaultListModel();
	
	private final ValueReporter listListener = new ValueReporter();
	static Logger logger = Logger.getLogger(OccupantsTab.class);

	ActionListener deleteListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int occupantId;
			try {
				occupantId = getSelectedLocumId();
			} catch (NoNodeSelectedException ex) {
				return;
			}
			internalState.deleteOccupant(occupantId);
			
		}

	};

	ActionListener saveListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int occupantId;
			try {
				occupantId = getSelectedLocumId();
			} catch (NoNodeSelectedException ex) {
				return;
			}
			internalState.saveOccupant(occupantId);

		}
	};

	public void displayLocumsList(final List<LocumMockup> locums) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
		locumsList.removeListSelectionListener(listListener);
		locumsListModel.removeAllElements();
		for (LocumMockup loc : locums) {
			locumsListModel.addElement(loc.getName());
		}
		locumsList.invalidate();
		locumsList.validate();
		locumsList.addListSelectionListener(listListener);
				}});
	}

	public void displayOccupantsData(OccupantMockup moc) {
		nameInput.setText(moc.name);
	}

	@Override
	public JPanel getJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());

		locumsList = new JList(locumsListModel);
		JScrollPane scrollPane = new JScrollPane(locumsList);
		scrollPane.setPreferredSize(new Dimension(150, 400));
		panel.add(scrollPane, "dock west, w 150:150:150");
//	private final JComboBox billingPersonInput = new JComboBox(billingTypes);
//	private DefaultListModel enabledListModel = new DefaultListModel();
//	private DefaultListModel countersListModel = new DefaultListModel();
//	private DefaultListModel quotationsListModel = new DefaultListModel();
//	private DefaultListModel occupantsListModel = new DefaultListModel();

		JLabel nameLabel = new JLabel("Nazwa");
		panel.add(nameLabel, "");
		panel.add(nameInput, "wrap");

		//TODO disable
		JLabel partFacLabel = new JLabel("Part fac");
		panel.add(partFacLabel, "");
		panel.add(participationFactorInput, "wrap");

		//TODO disable
		JLabel areaLabel = new JLabel("Powierzchnia");
		panel.add(areaLabel, "");
		panel.add(areaInput, "wrap");

		JLabel billingPersonLabel = new JLabel("Osoba rozliczana");
		billingPersonInput.setPreferredSize(new Dimension(350, 10));
		panel.add(billingPersonLabel, "");
		panel.add(billingPersonInput, "wrap");
		
		enabledServicesList = new JList(enabledServicesListModel);
		JScrollPane enabledServicesScrollPane = new JScrollPane(enabledServicesList);
		enabledServicesScrollPane.setSize(new Dimension(150, 300));
		panel.add(enabledServicesScrollPane, "w 150:150:150");

		countersList = new JList(countersListModel);
		JScrollPane countersScrollPane = new JScrollPane(countersList);
		countersScrollPane.setSize(new Dimension(150, 300));
		panel.add(countersScrollPane, "w 150:150:150");
		
		quotationsList = new JList(quotationsListModel);
		JScrollPane quotationsScrollPane = new JScrollPane(quotationsList);
		quotationsScrollPane.setSize(new Dimension(150, 300));
		panel.add(quotationsScrollPane, "w 150:150:150");
		
		occupantsList = new JList(occupantsListModel);
		JScrollPane occupantsScrollPane = new JScrollPane(occupantsList);
		occupantsScrollPane.setSize(new Dimension(150, 300));
		panel.add(occupantsScrollPane, "w 150:150:150");
		
		JButton createButton = new JButton("Nowy najemca");
		JButton saveButton = new JButton("Zapisz");
		JButton deleteButton = new JButton("Usuñ");
//		panel.add(createButton);
//		panel.add(deleteButton, "split 2, gapleft 30");
//		panel.add(saveButton, "gapleft 30");

		createButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			internalState.createNewItem();
		}

	});
		saveButton.addActionListener(saveListener);
		deleteButton.addActionListener(deleteListener);

		return panel;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void getReady() {
		logger.debug(name + " got ready");
		internalState = new NormalState();
		try {
			blockingQueue.put(new LocumsListNeededEvent(this));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addEmptyNewItem() {
		locumsListModel.addElement(new ListItem("<nowy>", -1));
		int index = locumsList.getModel().getSize();
		locumsList.setSelectedIndex(index - 1);
	}

	private void clearFields() {
		nameInput.setText("");
	}

	private int getSelectedLocumId() throws NoNodeSelectedException {
		try {
			return ((ListItem) locumsList.getSelectedValue()).getId();
		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	private void removeEmptyNewItem() {
		int index = locumsList.getModel().getSize();
		locumsListModel.remove(index - 1);
	}


}
