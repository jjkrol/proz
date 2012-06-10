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

/**
 * The Class LocumsTab.
 *
 * @author   jjkrol
 */
public class LocumsTab extends SpecificTab implements LocumsDisplayer{
	
	/** The blocking queue. */
	private BlockingQueue<PROZEvent> blockingQueue;
	
	/**
	 * Instantiates a new locums tab.
	 *
	 * @param blockingQueue the blocking queue
	 */
	public LocumsTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
	}
	
	/**
	 * The Class ListItem.
	 *
	 * @author   jjkrol
	 */
	private class ListItem {
		
		/** The name. */
		private String name;
		
		/** The id. */
		private int id;

		/**
		 * Instantiates a new list item.
		 *
		 * @param name the name
		 * @param id the id
		 */
		ListItem(String name, int id) {
			this.name = name;
			this.id = id;
		}

		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return name;
		}
	}

	/**
	 * The Class ValueReporter.
	 */
	private class ValueReporter implements ListSelectionListener {
		
		/* (non-Javadoc)
		 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
		 */
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

	/**
	 * The Class State.
	 */
	private class State {
		
		/**
		 * Creates the new item.
		 */
		void createNewItem() { };

		/**
		 * Save occupant.
		 *
		 * @param occupantId the occupant id
		 */
		void saveOccupant(int occupantId) { };
		
		/**
		 * Delete occupant.
		 *
		 * @param occupantId the occupant id
		 */
		void deleteOccupant(int occupantId){};
		
		/**
		 * Value changed.
		 *
		 * @param id the id
		 */
		void valueChanged(int id) {
			OccupantMockup moc = new OccupantMockup(id, null, null, null, null,
					null);
			try {
				blockingQueue.put(new OccupantChosenForViewingEvent(moc));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 * The Class NormalState.
	 */
	private class NormalState extends State {
		
		/* (non-Javadoc)
		 * @see pl.jjkrol.proz.view.LocumsTab.State#createNewItem()
		 */
		void createNewItem() {
			internalState = new EditingNewState();
			clearFields();
			addEmptyNewItem();
		}

		/* (non-Javadoc)
		 * @see pl.jjkrol.proz.view.LocumsTab.State#saveOccupant(int)
		 */
		void saveOccupant(int occupantId) {
		}
		
		/* (non-Javadoc)
		 * @see pl.jjkrol.proz.view.LocumsTab.State#deleteOccupant(int)
		 */
		void deleteOccupant(int occupantId){
			clearFields();
		}
	}

	/**
	 * The Class EditingNewState.
	 */
	private class EditingNewState extends State {
		
		/* (non-Javadoc)
		 * @see pl.jjkrol.proz.view.LocumsTab.State#saveOccupant(int)
		 */
		void saveOccupant(int occupantId) {
			internalState = new NormalState();
		}

		/* (non-Javadoc)
		 * @see pl.jjkrol.proz.view.LocumsTab.State#createNewItem()
		 */
		void createNewItem() {
			logger.warn("You cannot create new object while editing one");
			return;
		}
		
		/* (non-Javadoc)
		 * @see pl.jjkrol.proz.view.LocumsTab.State#valueChanged(int)
		 */
		void valueChanged(int id) {
			if (id != -1) {
				internalState = new NormalState();
				removeEmptyNewItem();
				super.valueChanged(id);
			}
		}

	}

	/** The internal state. */
	private State internalState = new NormalState();
	
	/** The name. */
	private String name = "Lokale";
	
	/** The core. */
	private final Controller core = Controller.getInstance();

	/** The locums list. */
	private JList locumsList;
	
	/** The locums list model. */
	private DefaultListModel locumsListModel = new DefaultListModel();
	
	/** The name input. */
	private final JTextField nameInput = new JTextField(30);
	
	/** The area input. */
	private final JTextField areaInput = new JTextField(30);
	
	/** The participation factor input. */
	private final JTextField participationFactorInput = new JTextField(30);
	
	/** The billing types. */
	Occupant.Billing[] billingTypes = { Occupant.Billing.BILL,
			Occupant.Billing.INVOICE };
	
	/** The billing person input. */
	private final JComboBox billingPersonInput = new JComboBox(billingTypes);
	
	/** The enabled services list. */
	private JList enabledServicesList;
	
	/** The enabled services list model. */
	private DefaultListModel enabledServicesListModel = new DefaultListModel();
	
	/** The counters list. */
	private JList countersList;
	
	/** The counters list model. */
	private DefaultListModel countersListModel = new DefaultListModel();
	
	/** The quotations list. */
	private JList quotationsList;
	
	/** The quotations list model. */
	private DefaultListModel quotationsListModel = new DefaultListModel();
	
	/** The occupants list. */
	private JList occupantsList;
	
	/** The occupants list model. */
	private DefaultListModel occupantsListModel = new DefaultListModel();
	
	/** The list listener. */
	private final ValueReporter listListener = new ValueReporter();
	
	/** The logger. */
	static Logger logger = Logger.getLogger(OccupantsTab.class);

	/** The delete listener. */
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

	/** The save listener. */
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

	/* (non-Javadoc)
	 * @see pl.jjkrol.proz.controller.LocumsDisplayer#displayLocumsList(java.util.List)
	 */
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

	/**
	 * Display occupants data.
	 *
	 * @param moc the moc
	 */
	public void displayOccupantsData(OccupantMockup moc) {
		nameInput.setText(moc.getName());
	}

	/* (non-Javadoc)
	 * @see pl.jjkrol.proz.view.SpecificTab#getJPanel()
	 */
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

	/* (non-Javadoc)
	 * @see pl.jjkrol.proz.view.SpecificTab#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see pl.jjkrol.proz.view.SpecificTab#getReady()
	 */
	@Override
	public void getReady() {
		logger.debug(name + " got ready");
		internalState = new NormalState();
		try {
			blockingQueue.put(new LocumsListNeededEvent(this));
		} catch (InterruptedException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Adds the empty new item.
	 */
	private void addEmptyNewItem() {
		locumsListModel.addElement(new ListItem("<nowy>", -1));
		int index = locumsList.getModel().getSize();
		locumsList.setSelectedIndex(index - 1);
	}

	/**
	 * Clear fields.
	 */
	private void clearFields() {
		nameInput.setText("");
	}

	/**
	 * Gets the selected locum id.
	 *
	 * @return the selected locum id
	 * @throws NoNodeSelectedException the no node selected exception
	 */
	private int getSelectedLocumId() throws NoNodeSelectedException {
		try {
			return ((ListItem) locumsList.getSelectedValue()).getId();
		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	/**
	 * Removes the empty new item.
	 */
	private void removeEmptyNewItem() {
		int index = locumsList.getModel().getSize();
		locumsListModel.remove(index - 1);
	}


}
