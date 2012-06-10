package pl.jjkrol.proz.view;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Occupant;

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
import org.apache.log4j.Logger;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import pl.jjkrol.proz.controller.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

import pl.jjkrol.proz.events.*;
import pl.jjkrol.proz.events.occupants.AddOccupantEvent;
import pl.jjkrol.proz.events.occupants.DeleteOccupantEvent;
import pl.jjkrol.proz.events.occupants.OccupantChosenForViewingEvent;
import pl.jjkrol.proz.events.occupants.OccupantsListNeededEvent;
import pl.jjkrol.proz.events.occupants.SaveOccupantEvent;

// TODO: Auto-generated Javadoc
/*
 * TODO disable create button
 */
/**
 * The Class OccupantsTab.
 *
 * @author   jjkrol
 */
public class OccupantsTab extends SpecificTab {
	
	/** The blocking queue. */
	BlockingQueue<PROZEvent> blockingQueue;
	
	/**
	 * Instantiates a new occupants tab.
	 *
	 * @param blockingQueue the blocking queue
	 */
	public OccupantsTab(BlockingQueue<PROZEvent> blockingQueue) {
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

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			return name;
		}
	}

	/**
	 * The Class ValueReporter.
	 */
	private class ValueReporter implements ListSelectionListener {
		
		/**
		 * {@inheritDoc}
		 */
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				int id;
				try {
					id = getSelectedOccupantId();
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
		void createNewItem() {
		};

		/**
		 * Save occupant.
		 *
		 * @param occupantId the occupant id
		 */
		void saveOccupant(int occupantId) {
		};

		/**
		 * Delete occupant.
		 *
		 * @param occupantId the occupant id
		 */
		void deleteOccupant(int occupantId) {
		};

		/**
		 * Value changed.
		 *
		 * @param id the id
		 */
		void valueChanged(int id) {
			OccupantMockup moc =
					new OccupantMockup(id, null, null, null, null, null);
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
		
		/**
		 * {@inheritDoc}
		 */
		void createNewItem() {
			internalState = new EditingNewState();
			clearFields();
			addEmptyNewItem();
		}

		/**
		 * {@inheritDoc}
		 */
		void saveOccupant(int occupantId) {
			OccupantMockup moc = createMockupFromFieldData(occupantId);
			try {
				blockingQueue.put(new SaveOccupantEvent(moc));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		void deleteOccupant(int occupantId) {
			OccupantMockup moc = createMockupFromFieldData(occupantId);
			try {
				blockingQueue.put(new DeleteOccupantEvent(moc));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			clearFields();
		}
	}

	/**
	 * The Class EditingNewState.
	 */
	private class EditingNewState extends State {
		
		/**
		 * {@inheritDoc}
		 */
		void saveOccupant(int occupantId) {
			OccupantMockup moc = createMockupFromFieldData(occupantId);
			try {
				blockingQueue.put(new AddOccupantEvent(moc));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			internalState = new NormalState();
		}

		/**
		 * {@inheritDoc}
		 */
		void createNewItem() {
			logger.warn("You cannot create new object while editing one");
			return;
		}

		/**
		 * {@inheritDoc}
		 */
		void valueChanged(int id) {
			if (id != -1) {
				internalState = new NormalState();
				removeEmptyNewItem();
				super.valueChanged(id);
			}
		}

	}
	
	/** The name. */
	private String name = "Najemcy";
	
	/** The internal state. */
	private State internalState = new NormalState();
	
	/** The core. */
	private final Controller core = Controller.getInstance();
	
	/** The logger. */
	static Logger logger = Logger.getLogger(OccupantsTab.class);

	/*
	 * panel components
	 */
	/** The occupants list. */
	private JList occupantsList;
	
	/** The list model. */
	private DefaultListModel listModel = new DefaultListModel();
	
	/** The name input. */
	private final JTextField nameInput = new JTextField(30);
	
	/** The address input. */
	private final JTextField addressInput = new JTextField(30);
	
	/** The telephone input. */
	private final JTextField telephoneInput = new JTextField(30);
	
	/** The billing types. */
	Occupant.Billing[] billingTypes = { Occupant.Billing.BILL,
			Occupant.Billing.INVOICE };
	
	/** The billing input. */
	private final JComboBox billingInput = new JComboBox(billingTypes);
	
	/** The nip input. */
	private final JTextField nipInput = new JTextField(30);

	/** The list listener. */
	private final ValueReporter listListener = new ValueReporter();

	/** The create listener. */
	ActionListener createListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			internalState.createNewItem();
		}

	};

	/** The delete listener. */
	ActionListener deleteListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int occupantId = getSelectedOccupantId();
				internalState.deleteOccupant(occupantId);
			} catch (NoNodeSelectedException ex) {
			}

		}

	};

	/** The save listener. */
	ActionListener saveListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				int occupantId = getSelectedOccupantId();
				internalState.saveOccupant(occupantId);
			} catch (NoNodeSelectedException ex) {
			}
		}
	};

	/**
	 * Display occupants list.
	 *
	 * @param occupants the occupants
	 */
	public void displayOccupantsList(final List<OccupantMockup> occupants) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				occupantsList.removeListSelectionListener(listListener);
				listModel.removeAllElements();
				for (OccupantMockup occ : occupants) {
					listModel.addElement(new ListItem(occ.getName(), occ.getId()));
				}
				occupantsList.invalidate();
				occupantsList.validate();
				occupantsList.addListSelectionListener(listListener);
			}
		});
	}

	/**
	 * Display occupants data.
	 *
	 * @param moc the moc
	 */
	public void displayOccupantsData(final OccupantMockup moc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				nameInput.setText(moc.getName());
				addressInput.setText(moc.getAddress());
				telephoneInput.setText(moc.getTelephone());
				nipInput.setText(moc.getNip());
				if (moc.getBillingType().equals(Occupant.Billing.BILL))
					billingInput.setSelectedIndex(0);
				else
					billingInput.setSelectedIndex(1);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JPanel getJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());

		occupantsList = new JList(listModel);
		JScrollPane scrollPane = new JScrollPane(occupantsList);
		scrollPane.setPreferredSize(new Dimension(150, 400));
		panel.add(scrollPane, "dock west");

		JLabel nameLabel = new JLabel("Imiê");
		panel.add(nameLabel, "");
		panel.add(nameInput, "wrap");

		JLabel addressLabel = new JLabel("Adres");
		panel.add(addressLabel, "");
		panel.add(addressInput, "wrap");

		JLabel telephoneLabel = new JLabel("Telefon");
		panel.add(telephoneLabel, "");
		panel.add(telephoneInput, "wrap");

		JLabel nipLabel = new JLabel("Nip");
		panel.add(nipLabel, "gap unrelated");
		panel.add(nipInput, "grow, wrap");

		JLabel billingLabel = new JLabel("Typ rozliczenia");
		billingInput.setPreferredSize(new Dimension(350, 10));
		panel.add(billingLabel, "");
		panel.add(billingInput, "wrap");
		JButton createButton = new JButton("Nowy najemca");
		JButton saveButton = new JButton("Zapisz");
		JButton deleteButton = new JButton("Usuñ");
		panel.add(createButton);
		panel.add(deleteButton, "split 2, gapleft 30");
		panel.add(saveButton, "gapleft 30");

		createButton.addActionListener(createListener);
		saveButton.addActionListener(saveListener);
		deleteButton.addActionListener(deleteListener);

		return panel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getReady() {
		internalState = new NormalState();
		try {
			blockingQueue.put(new OccupantsListNeededEvent());
		} catch (InterruptedException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Adds the empty new item.
	 */
	private void addEmptyNewItem() {
		listModel.addElement(new ListItem("<nowy>", -1));
		int index = occupantsList.getModel().getSize();
		occupantsList.setSelectedIndex(index - 1);
	}

	/**
	 * Clear fields.
	 */
	private void clearFields() {
		nameInput.setText("");
		addressInput.setText("");
		telephoneInput.setText("");
		nipInput.setText("");
	}

	/**
	 * Creates the mockup from field data.
	 *
	 * @param occupantId the occupant id
	 * @return the occupant mockup
	 */
	private OccupantMockup createMockupFromFieldData(int occupantId) {
		OccupantMockup moc =
				new OccupantMockup(occupantId, nameInput.getText(),
						addressInput.getText(), telephoneInput.getText(),
						nipInput.getText(), (Occupant.Billing) billingInput
								.getSelectedItem());
		return moc;
	}

	/**
	 * Gets the selected occupant id.
	 *
	 * @return the selected occupant id
	 * @throws NoNodeSelectedException the no node selected exception
	 */
	private int getSelectedOccupantId() throws NoNodeSelectedException {
		try {
			return ((ListItem) occupantsList.getSelectedValue()).getId();
		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	/**
	 * Removes the empty new item.
	 */
	private void removeEmptyNewItem() {
		int index = occupantsList.getModel().getSize();
		listModel.remove(index - 1);
	}

}
