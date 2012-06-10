package pl.jjkrol.proz.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import pl.jjkrol.proz.events.measurements.AddMeasurementEvent;
import pl.jjkrol.proz.events.measurements.DeleteMeasurementEvent;
import pl.jjkrol.proz.events.measurements.LocumChosenForViewingEvent;
import pl.jjkrol.proz.events.measurements.SaveMeasurementEvent;
import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.controller.LocumsDisplayer;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.model.MeasurableService;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurementsTab.
 * 
 * @author jjkrol
 */
public class MeasurementsTab extends SpecificTab implements LocumsDisplayer {

	/**
	 * The Class LocumsValueReporter.
	 */
	private class LocumsValueReporter implements ListSelectionListener {

		/**
		 * {@inheritDoc}
		 */
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

	/**
	 * The Class MeasurementsValueReporter.
	 */
	private class MeasurementsValueReporter implements ListSelectionListener {

		/**
		 * {@inheritDoc}
		 */
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
	/**
	 * The Class State.
	 */
	private class State {

		/**
		 * Creates the new item.
		 */
		void create() {
		};

		/**
		 * Save measurement.
		 *
		 */
		void save() {
		};

		/**
		 * Delete measurement.
		 *
		 */
		void delete() {
		};

		/**
		 * Locums value changed.
		 * 
		 * @param moc
		 *            the moc
		 */
		void locumsValueChanged(LocumMockup moc) {
			try {
				blockingQueue.put(new LocumChosenForViewingEvent(moc));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
		}

		/**
		 * Measurements value changed.
		 * 
		 * @param moc
		 *            the moc
		 */
		void measurementsValueChanged(MeasurementMockup moc) {
			displayMeasurementData(moc);
		}

		/**
		 * Toggle buttons.
		 */
		void toggleButtons() {
		}
	}

	/**
	 * The Class NormalState.
	 */
	private class NormalState extends State {

		/**
		 * Instantiates a new normal state.
		 */
		NormalState() {
			// clear fields
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void create() {
			if (locumsList.hasFocus()) {
				LocumMockup selected =
						(LocumMockup) locumsList.getSelectedValue();
				internalState = new EditingNewState();
				internalState.toggleButtons();
				addEmptyElement();
			}
			// clearFields();
			// addEmptyNewItem();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void save() {
			try {
				LocumMockup locumMockup = getSelectedLocum();
				MeasurementMockup measurementMockup =
						getMeasurementMockupFromFields();
				blockingQueue.put(new SaveMeasurementEvent(locumMockup,
						measurementMockup));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
			} catch (NoNodeSelectedException e) {
				logger.warn(e.getMessage());
			} catch (BadDateValue e) {
				logger.warn(e.getMessage());
			}
			internalState = new NormalState();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void delete() {
			try {
				LocumMockup loc = getSelectedLocum();
				MeasurementMockup mea = getSelectedMeasurement();
				blockingQueue.put(new DeleteMeasurementEvent(loc, mea));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
			} catch (NoNodeSelectedException e) {
				logger.warn(e.getMessage());
			}
			internalState = new NormalState();
			internalState.toggleButtons();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void toggleButtons() {
			createButton.setEnabled(true);
			deleteButton.setEnabled(true);
			saveButton.setEnabled(true);
		}
	}

	/**
	 * The Class EditingNewState.
	 * 
	 * @author jjkrol
	 */
	private class EditingNewState extends State {

		/**
		 * Instantiates a new editing new state.
		 *
		 */
		EditingNewState() {
				clearFields();
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			String dateString = df.format(new Date());
			dateInput.setText(dateString);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void save() {
			try {
				LocumMockup locumMockup = getSelectedLocum();
				MeasurementMockup measurementMockup =
						getMeasurementMockupFromFields();
				blockingQueue.put(new AddMeasurementEvent(locumMockup,
						measurementMockup));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
			} catch (NoNodeSelectedException e) {
				logger.warn(e.getMessage());
			} catch (BadDateValue e) {
				logger.warn(e.getMessage());
			}
			internalState = new NormalState();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void create() {
			logger.warn("You cannot create new object while editing one");
			return;
		}

		/**
		 * {@inheritDoc}
		 */
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		void measurementsValueChanged(MeasurementMockup moc) {
			if (moc != emptyElement) {
				internalState = new NormalState();
				internalState.toggleButtons();
				removeEmptyElement();

				// call the method again,
				// so the application behaves normally
				internalState.measurementsValueChanged(moc);
			}
		}

		/**
		 * {@inheritDoc}
		 */
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
	/** The name. */
	private String name = "Odczyty";

	/** main panel of the tab. */
	private final JPanel panel = new JPanel();

	/** The core. */
	private final Controller core = Controller.getInstance();

	/** The internal state. */
	private State internalState = new NormalState();

	/** The logger. */
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	/*
	 * JPanel components
	 */
	/** button for creating a new measurement. */
	private JButton createButton;
	/** button for saving changes. */
	private JButton saveButton;
	/** button for deleting a measurement. */
	private JButton deleteButton;
	/** List for showing locums. */
	private JList locumsList;
	/** List model for locums list. */
	private DefaultListModel locumsListModel = new DefaultListModel();
	/** List for showing measurement sets for the locum. */
	private JList measurementsList;
	/** List model for the measurements list. */
	private DefaultListModel measurementsListModel = new DefaultListModel();
	/**
	 * An empty measurement mockup for showing as a new unsaved item.
	 * 
	 * @uml.property name="emptyElement" @uml.associationEnd
	 */
	private MeasurementMockup emptyElement = new MeasurementMockup(null, null);
	/** Input for date of the measurement. */
	private final JTextField dateInput = new JTextField(30);
	/** Listener for changes on the locums list. */
	private final ListSelectionListener locumsListListener =
			new LocumsValueReporter();
	/** Listener for changes on the measurements list. */
	private final ListSelectionListener measurementsListListener =
			new MeasurementsValueReporter();
	/** Map of labels for each measurable service. */
	private final Map<MeasurableService, JLabel> serviceLabels =
			new HashMap<MeasurableService, JLabel>();
	/** Map of text fields for each measurable service. */
	private final Map<MeasurableService, JTextField> serviceFields =
			new HashMap<MeasurableService, JTextField>();

	/** The delete listener. */
	ActionListener deleteListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			internalState.delete();
		}
	};

	/** The save listener. */
	ActionListener saveListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			internalState.save();
		}
	};

	/** The create listener. */
	ActionListener createListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			internalState.create();
		}
	};

	/**
	 * {@inheritDoc}
	 */
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
	 * Displays list of all measurements.
	 * 
	 * @param measurements
	 *            the measurements
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

	/**
	 * Display measurement data.
	 * 
	 * @param moc
	 *            the moc
	 */
	private void displayMeasurementData(MeasurementMockup moc) {
		Calendar date = moc.getDate();
		Map<MeasurableService, Float> values = moc.getValues();
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String dateString = df.format(date.getTime());
		dateInput.setText(dateString);
		for (MeasurableService serv : values.keySet()) {
			JTextField field = serviceFields.get(serv);
			Float value = values.get(serv);
			field.setText(value.toString());
		}
	}

	/** The blocking queue. */
	private BlockingQueue<PROZEvent> blockingQueue;

	/**
	 * Instantiates a new measurements tab.
	 * 
	 * @param blockingQueue
	 *            the blocking queue
	 */
	public MeasurementsTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		panel.setLayout(new MigLayout());
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 * @uml.property name="name"
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * Gets the selected locum.
	 * 
	 * @return the selected locum
	 * @throws NoNodeSelectedException
	 *             the no node selected exception
	 */
	private LocumMockup getSelectedLocum() throws NoNodeSelectedException {
		try {
			return (LocumMockup) locumsList.getSelectedValue();
		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	/**
	 * Gets the selected measurement.
	 * 
	 * @return the selected measurement
	 * @throws NoNodeSelectedException
	 *             the no node selected exception
	 */
	public MeasurementMockup getSelectedMeasurement()
			throws NoNodeSelectedException {
		try {
			Object ob = measurementsList.getSelectedValue();
			if (ob == null)
				throw new NoNodeSelectedException();
			else
				return (MeasurementMockup) ob;

		} catch (NullPointerException e) {
			throw new NoNodeSelectedException();
		}
	}

	/**
	 * Clear fields.
	 */
	private void clearFields() {
		dateInput.setText("");
		for (MeasurableService serv : serviceFields.keySet()) {
			JTextField field = serviceFields.get(serv);
			field.setText("");
		}
	}

	/**
	 * Adds the empty element.
	 */
	private void addEmptyElement() {
		measurementsListModel.addElement(emptyElement);
		measurementsList.setSelectedValue(emptyElement, true);

	}

	/**
	 * Removes the empty element.
	 */
	private void removeEmptyElement() {
		measurementsListModel.removeElement(emptyElement);

	}

	/**
	 * Gets the measurement mockup from fields.
	 *
	 * @return the measurement mockup from fields
	 * @throws BadDateValue the bad date value
	 */
	private MeasurementMockup getMeasurementMockupFromFields()
			throws BadDateValue {
		try {
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Date d;
			d = df.parse(dateInput.getText());
			Calendar date = new GregorianCalendar();
			date.setTime(d);
			Map<MeasurableService, Float> values =
					new HashMap<MeasurableService, Float>();
			for (MeasurableService serv : serviceFields.keySet()) {
				String value = serviceFields.get(serv).getText();
				values.put(serv, new Float(value));
			}
			MeasurementMockup measurementMockup =
					new MeasurementMockup(date, values);
			return measurementMockup;
		} catch (Exception e) {
			throw new BadDateValue();
		}
	}
}
