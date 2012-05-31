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

/*
 * TODO disable create button
 */
public class OccupantsTab implements SpecificTab {
	
	BlockingQueue<PROZEvent> blockingQueue;
	public OccupantsTab(BlockingQueue<PROZEvent> blockingQueue) {
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
					id = getSelectedOccupantId();
				} catch (NoNodeSelectedException e) {
					return;
				}
				internalState.valueChanged(id);
			}
		}

	}

	private class State {
		void createNewItem() {
		};

		void saveOccupant(int occupantId) {
		};

		void deleteOccupant(int occupantId) {
		};

		void valueChanged(int id) {
			OccupantMockup moc =
					new OccupantMockup(id, null, null, null, null, null);
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
			OccupantMockup moc = createMockupFromFieldData(occupantId);
			try {
				blockingQueue.put(new SaveOccupantEvent(moc));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		void deleteOccupant(int occupantId) {
			OccupantMockup moc = createMockupFromFieldData(occupantId);
			try {
				blockingQueue.put(new DeleteOccupantEvent(moc));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clearFields();
		}
	}

	private class EditingNewState extends State {
		void saveOccupant(int occupantId) {
			OccupantMockup moc = createMockupFromFieldData(occupantId);
			try {
				blockingQueue.put(new AddOccupantEvent(moc));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	private String name = "Najemcy";
	private State internalState = new NormalState();
	private final Controller core = Controller.getInstance();
	static Logger logger = Logger.getLogger(OccupantsTab.class);

	/*
	 * panel components
	 */
	private JList occupantsList;
	private DefaultListModel listModel = new DefaultListModel();
	private final JTextField nameInput = new JTextField(30);
	private final JTextField addressInput = new JTextField(30);
	private final JTextField telephoneInput = new JTextField(30);
	Occupant.Billing[] billingTypes = { Occupant.Billing.BILL,
			Occupant.Billing.INVOICE };
	private final JComboBox billingInput = new JComboBox(billingTypes);
	private final JTextField nipInput = new JTextField(30);

	private final ValueReporter listListener = new ValueReporter();

	ActionListener createListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			internalState.createNewItem();
		}

	};

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
	 * Displays occupants list
	 * 
	 * @param occupants
	 */
	public void displayOccupantsList(final List<OccupantMockup> occupants) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				occupantsList.removeListSelectionListener(listListener);
				listModel.removeAllElements();
				for (OccupantMockup occ : occupants) {
					listModel.addElement(new ListItem(occ.name, occ.id));
				}
				occupantsList.invalidate();
				occupantsList.validate();
				occupantsList.addListSelectionListener(listListener);
			}
		});
	}

	public void displayOccupantsData(final OccupantMockup moc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				nameInput.setText(moc.name);
				addressInput.setText(moc.address);
				telephoneInput.setText(moc.telephone);
				nipInput.setText(moc.nip);
				if (moc.billingType.equals(Occupant.Billing.BILL))
					billingInput.setSelectedIndex(0);
				else
					billingInput.setSelectedIndex(1);
			}
		});
	}

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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void getReady() {
		internalState = new NormalState();
		try {
			blockingQueue.put(new OccupantsListNeededEvent());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addEmptyNewItem() {
		listModel.addElement(new ListItem("<nowy>", -1));
		int index = occupantsList.getModel().getSize();
		occupantsList.setSelectedIndex(index - 1);
	}

	private void clearFields() {
		nameInput.setText("");
		addressInput.setText("");
		telephoneInput.setText("");
		nipInput.setText("");
	}

	private OccupantMockup createMockupFromFieldData(int occupantId) {
		OccupantMockup moc =
				new OccupantMockup(occupantId, nameInput.getText(),
						addressInput.getText(), telephoneInput.getText(),
						nipInput.getText(), (Occupant.Billing) billingInput
								.getSelectedItem());
		return moc;
	}

	private int getSelectedOccupantId() throws NoNodeSelectedException {
		try {
			return ((ListItem) occupantsList.getSelectedValue()).getId();
		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	private void removeEmptyNewItem() {
		int index = occupantsList.getModel().getSize();
		listModel.remove(index - 1);
	}

}
