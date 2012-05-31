package pl.jjkrol.proz.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.jar.JarInputStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pl.jjkrol.proz.events.*;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.controller.LocumsDisplayer;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.model.MeasurableService;

public class MeasurementsTab implements SpecificTab, LocumsDisplayer {

	private class LocumsValueReporter implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				try {
					LocumMockup moc = getSelectedLocum();
					internalState.locumsValueChanged(moc);
				} catch (NoNodeSelectedException e) {
					return;
				}
			}
		}
	}

	private class MeasurementsValueReporter implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				try {
					MeasurementMockup moc = getSelectedMeasurement();
					internalState.measurementsValueChanged(moc);
				} catch (NoNodeSelectedException e) {
					return;
				}
			}
		}
	}

	/*
	 * Internal states of the tab
	 */
	private class State {
		void createNewItem() {
		};

		void saveMeasurement(int occupantId) {
		};

		void deleteMeasurement(int occupantId) {
		};

		void locumsValueChanged(LocumMockup moc) {
			try {
				blockingQueue.put(new LocumChosenForViewingEvent(moc));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		void measurementsValueChanged(MeasurementMockup moc) {
			displayMeasurementData(moc);
		}

		void toggleButtons() {
		}
	}

	private class NormalState extends State {
		NormalState() {
			// clear fields
		}

		@Override
		void createNewItem() {
			if (locumsList.hasFocus()) {
				LocumMockup selected =
						(LocumMockup) locumsList.getSelectedValue();
				internalState = new EditingNewState(selected);
				internalState.toggleButtons();
				addEmptyElement();
				clearFields();
			}
			// clearFields();
			// addEmptyNewItem();
		}

		@Override
		void saveMeasurement(int occupantId) {
			// MeasurementMockup moc = createMockupFromFieldData()
			// OccupantMockup moc = createMockupFromFieldData(occupantId);
			// core.putEvent(new SaveOccupantEvent(moc));
		}

		@Override
		void deleteMeasurement(int occupantId) {
			internalState = new NormalState();
			internalState.toggleButtons();
		}

		@Override
		void toggleButtons() {
			createButton.setEnabled(true);
			deleteButton.setEnabled(true);
			saveButton.setEnabled(true);
		}
	}

	private class ShowingLocum extends State {

	}

	private class EditingNewState extends State {
		private LocumMockup selectedLocum;

		EditingNewState(LocumMockup selectedLocum) {
			this.selectedLocum = selectedLocum;
		}

		@Override
		void saveMeasurement(int occupantId) {
			// OccupantMockup moc = createMockupFromFieldData(occupantId);
			// core.putEvent(new AddOccupantEvent(moc));
			internalState = new NormalState();
		}

		@Override
		void createNewItem() {
			logger.warn("You cannot create new object while editing one");
			return;
		}

		@Override
		void locumsValueChanged(LocumMockup moc) {
			// TODO drop the changes, and revert to the normal state
			// revert to normal state
			internalState = new NormalState();
			internalState.toggleButtons();
			// call the method again,
			// so the application behaves normally
			internalState.locumsValueChanged(moc);
			// removeEmptyNewItem();
		}

		@Override
		void measurementsValueChanged(MeasurementMockup moc) {
			logger.debug("tutaj");
			if (moc != emptyElement) {
				internalState = new NormalState();
				internalState.toggleButtons();
				removeEmptyElement();

				// call the method again,
				// so the application behaves normally
				internalState.measurementsValueChanged(moc);
			}
		}

		@Override
		void toggleButtons() {
			createButton.setEnabled(false);
			deleteButton.setEnabled(true);
			saveButton.setEnabled(true);
		}

	}

	/*
	 * Main variables
	 */
	private String name = "Odczyty";
	/**
	 * main panel of the tab
	 */
	private final JPanel panel = new JPanel();
	private final Controller core = Controller.getInstance();
	private State internalState = new NormalState();
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	/*
	 * JPanel components
	 */
	/**
	 * button for creating a new measurement
	 */
	private JButton createButton;
	/**
	 * button for saving changes
	 */
	private JButton saveButton;
	/**
	 * button for deleting a measurement
	 */
	private JButton deleteButton;

	/**
	 * List for showing locums
	 */
	private JList locumsList;
	/**
	 * List model for locums list
	 */
	private DefaultListModel locumsListModel = new DefaultListModel();
	/**
	 * List for showing measurement sets for the locum
	 */
	private JList measurementsList;
	/**
	 * List model for the measurements list
	 */
	private DefaultListModel measurementsListModel = new DefaultListModel();
	/**
	 * An empty measurement mockup for showing as a new unsaved item
	 */
	private MeasurementMockup emptyElement = new MeasurementMockup(null, null);
	/**
	 * Input for date of the measurement
	 */
	private final JTextField dateInput = new JTextField(30);
	/**
	 * Listener for changes on the locums list
	 */
	private final ListSelectionListener locumsListListener =
			new LocumsValueReporter();
	/**
	 * Listener for changes on the measurements list
	 */
	private final ListSelectionListener measurementsListListener =
			new MeasurementsValueReporter();
	/**
	 * Map of labels for each measurable service
	 */
	private final Map<MeasurableService, JLabel> serviceLabels =
			new HashMap<MeasurableService, JLabel>();
	/**
	 * Map of text fields for each measurable service
	 */
	private final Map<MeasurableService, JTextField> serviceFields =
			new HashMap<MeasurableService, JTextField>();

	ActionListener deleteListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// try {
			// int measurementId = getSelectedMeasurementId();
			// internalState.deleteMeasurement(occupantId);
			// }
			// catch (NoNodeSelectedException ex) {
			//
			// }
		}
	};

	ActionListener saveListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// try {
			// int occupantId = getSelectedOccupantId();
			// internalState.saveOccupant(occupantId);
			// }
			// catch (NoNodeSelectedException ex) {
			// }
		}
	};

	ActionListener createListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			internalState.createNewItem();
		}
	};

	public void displayLocumsList(List<LocumMockup> locums) {
		locumsList.removeListSelectionListener(locumsListListener);
		locumsListModel.removeAllElements();
		for (LocumMockup loc : locums) {
			locumsListModel.addElement(loc);
		}
		locumsList.invalidate();
		locumsList.validate();
		locumsList.addListSelectionListener(locumsListListener);
	}

	/**
	 * Displays list of all measurements
	 * 
	 * @param measurements
	 */
	public void displayMeasurements(final List<MeasurementMockup> measurements) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				measurementsList
						.removeListSelectionListener(measurementsListListener);
				measurementsListModel.removeAllElements();
				for (MeasurementMockup mea : measurements) {
					measurementsListModel.addElement(mea);
				}
				measurementsList.invalidate();
				measurementsList.validate();
				measurementsList
						.addListSelectionListener(measurementsListListener);
			}
		});
	}

	private void displayMeasurementData(MeasurementMockup moc) {
		Calendar date = moc.date;
		Map<MeasurableService, Float> values = moc.values;
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String dateString = df.format(date.getTime());
		dateInput.setText(dateString);
		for (MeasurableService serv : values.keySet()) {
			JTextField field = serviceFields.get(serv);
			Float value = values.get(serv);
			field.setText(value.toString());
		}
	}
	private BlockingQueue<PROZEvent> blockingQueue;
	public MeasurementsTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		panel.setLayout(new MigLayout());
	}

	@Override
	public JPanel getJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());

		locumsList = new JList(locumsListModel);
		JScrollPane locumsScrollPane = new JScrollPane(locumsList);
		locumsScrollPane.setPreferredSize(new Dimension(150, 400));
		panel.add(locumsScrollPane, "dock west");

		measurementsList = new JList(measurementsListModel);
		JScrollPane measurementsScrollPane = new JScrollPane(measurementsList);
		measurementsScrollPane.setPreferredSize(new Dimension(150, 400));
		panel.add(measurementsScrollPane, "dock west");

		JLabel dateLabel = new JLabel("Data");
		panel.add(dateLabel, "");
		panel.add(dateInput, "wrap");

		MeasurableService[] services =
				new MeasurableService[] { MeasurableService.CW,
						MeasurableService.CCW, MeasurableService.ZW,
						MeasurableService.CO, MeasurableService.EE,
						MeasurableService.GAZ };

		for (MeasurableService serv : services) {
			JLabel lab = new JLabel(serv.toString());
			panel.add(lab);
			serviceLabels.put(serv, lab);
			JTextField field = new JTextField(10);
			panel.add(field, "wrap");
			serviceFields.put(serv, field);
		}

		/*
		 * JLabel addressLabel = new JLabel("Adres"); panel.add(addressLabel,
		 * ""); panel.add(addressInput, "wrap");
		 * 
		 * JLabel telephoneLabel = new JLabel("Telefon");
		 * panel.add(telephoneLabel, ""); panel.add(telephoneInput, "wrap");
		 * 
		 * JLabel nipLabel = new JLabel("Nip"); panel.add(nipLabel,
		 * "gap unrelated"); panel.add(nipInput, "grow, wrap");
		 * 
		 * JLabel billingLabel = new JLabel("Typ rozliczenia");
		 * billingInput.setPreferredSize(new Dimension(350, 10));
		 * panel.add(billingLabel, ""); panel.add(billingInput, "wrap");
		 */
		createButton = new JButton("Nowy odczyt");
		saveButton = new JButton("Zapisz");
		deleteButton = new JButton("Usuñ");
		panel.add(createButton);
		panel.add(deleteButton, "split 2, gapleft 30");
		panel.add(saveButton, "gapleft 30");

		saveButton.addActionListener(saveListener);
		deleteButton.addActionListener(deleteListener);
		createButton.addActionListener(createListener);

		return panel;
	}

	@Override
	public String getName() {
		return name;
	}

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

	private LocumMockup getSelectedLocum() throws NoNodeSelectedException {
		try {
			return (LocumMockup) locumsList.getSelectedValue();
		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	public MeasurementMockup getSelectedMeasurement()
			throws NoNodeSelectedException {
		try {
			return (MeasurementMockup) measurementsList.getSelectedValue();

		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	private void clearFields() {
		dateInput.setText("");
		for (MeasurableService serv : serviceFields.keySet()) {
			JTextField field = serviceFields.get(serv);
			field.setText("");
		}
	}

	private void addEmptyElement() {
		measurementsListModel.addElement(emptyElement);
		measurementsList.setSelectedValue(emptyElement, true);

	}

	private void removeEmptyElement() {
		measurementsListModel.removeElement(emptyElement);

	}
}
