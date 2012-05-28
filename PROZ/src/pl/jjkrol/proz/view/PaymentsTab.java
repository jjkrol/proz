package pl.jjkrol.proz.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.CalculatedResultsNeededEvent;
import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.controller.LocumMeasurementsAndQuotationsNeededEvent;
import pl.jjkrol.proz.controller.LocumMockup;
import pl.jjkrol.proz.controller.LocumsDisplayer;
import pl.jjkrol.proz.controller.LocumsListNeededEvent;
import pl.jjkrol.proz.controller.MeasurementMockup;
import pl.jjkrol.proz.controller.QuotationMockup;
import pl.jjkrol.proz.model.BillableService;

/**
 * A class responsible for handling all user interactions connected with
 * operating on payments
 * 
 * @author jjkrol
 */
public class PaymentsTab implements SpecificTab, LocumsDisplayer {

	/**
	 * General class representing the tab state
	 */
	private class State {
		protected JPanel statePanel = new JPanel();

		/**
		 * Adds a state-specific panel to the main tab panel
		 */
		void addPanel() {
		}

		/**
		 * Changes tab state to the next step
		 */
		void next() {
		}

		/**
		 * Changes tab state to the previous step
		 */
		void prev() {
		}

		/**
		 * Removes state-specific panel from the main tab panel
		 */
		void clearPanel() {
			panel.remove(statePanel);
		}

		// move to the specific state
		void displayLocumsList(List<LocumMockup> locums) {

		}

		public void displayLocumMeasurementsAndQuotations(
				List<MeasurementMockup> measurements,
				Map<String, List<QuotationMockup>> quotations) {
		}
		
		public void displayCalculationResults(Map<BillableService, Float> results,
				Map<BillableService, Float> administrativeResults) {
		}
	}

	/**
	 * Class responsible for state in which the user is entering data concerning
	 * locum, date and quotations to calculate payment.
	 */
	private class DataChooseState extends State {
		private List<MeasurementMockup> measurements;
		private Map<String, List<QuotationMockup>> quotations;

		private DefaultComboBoxModel locumsComboModel =
				new DefaultComboBoxModel();
		private JComboBox locumsCombo = new JComboBox(locumsComboModel);
		private DefaultComboBoxModel measurementsFromComboModel =
				new DefaultComboBoxModel();
		private JComboBox measurementsFromCombo = new JComboBox(
				measurementsFromComboModel);
		private DefaultComboBoxModel measurementsToComboModel =
				new DefaultComboBoxModel();
		private JComboBox measurementsToCombo = new JComboBox(
				measurementsToComboModel);
		private DefaultComboBoxModel quotationsComboModel =
				new DefaultComboBoxModel();
		private JComboBox quotationsCombo = new JComboBox(quotationsComboModel);

		/**
		 * Constructor creates the state panel and all its components
		 */
		DataChooseState() {
			statePanel.setLayout(new MigLayout());
			statePanel.add(new JLabel("aa"));

			statePanel.add(locumsCombo);
			locumsCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO maybe replace it with data passed in locumMockup?
					// get name selected locum name
					LocumMockup moc = (LocumMockup) locumsCombo.getSelectedItem();
					measurementsToComboModel.removeAllElements();
					measurementsFromComboModel.removeAllElements();
					quotationsComboModel.removeAllElements();
					// create empty mockup
					core.putEvent(new LocumMeasurementsAndQuotationsNeededEvent(
							moc));
				}
			});

			// combo for beginning date
			statePanel.add(measurementsFromCombo);
			measurementsFromCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// assure that to is greater than from
					if (measurements != null) {
						MeasurementMockup selected =
								(MeasurementMockup) measurementsFromCombo
										.getSelectedItem();
						if (selected != null) {
							measurementsToComboModel.removeAllElements();
							for (MeasurementMockup moc : measurements) {
								if (moc.date.after(selected.date)) {
									measurementsToComboModel.addElement(moc);
								}
							}
						}
					}
				}
			});
			// combo for ending date
			statePanel.add(measurementsToCombo);

			// combo for quotation
			statePanel.add(quotationsCombo);

			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 4");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void next() {
			panel.remove(statePanel);
			LocumMockup loc = (LocumMockup) locumsCombo.getSelectedItem();
			MeasurementMockup meaFrom = (MeasurementMockup)measurementsFromCombo.getSelectedItem();
			MeasurementMockup meaTo = (MeasurementMockup)measurementsToCombo.getSelectedItem();
			String quot = (String)quotationsCombo.getSelectedItem();
			core.putEvent(new CalculatedResultsNeededEvent(loc, meaFrom, meaTo, quot));
			internalState = new AcceptResultsState(this);
			internalState.addPanel();
			panel.repaint();
		}

		/**
		 * Fills the locums combo box
		 */
		void displayLocumsList(List<LocumMockup> locums) {
			locumsComboModel.removeAllElements();
			for (LocumMockup loc : locums) {
				locumsComboModel.addElement(loc);
			}
		}

		/**
		 * Inserts data to three combo boxes: from, to and quotations
		 */
		public void displayLocumMeasurementsAndQuotations(
				List<MeasurementMockup> measurements,
				Map<String, List<QuotationMockup>> quotations) {

			// fill from combo
			this.measurements = measurements;
			this.quotations = quotations;
			//TODO sort this descending
			measurementsFromComboModel.removeAllElements();
			for (MeasurementMockup mock : measurements) {
				measurementsFromComboModel.addElement(mock);
			}

			// fill to combo
			measurementsToComboModel.removeAllElements();
			MeasurementMockup oldestMeasurement = measurements.get(0);
			for (MeasurementMockup mock : measurements) {
				if (mock.date.before(oldestMeasurement.date))
					oldestMeasurement = mock;
				measurementsToComboModel.addElement(mock);
			}

			// remove oldest element - shouldn't be chosen
			measurementsToComboModel.removeElement(oldestMeasurement);

			// fill quotations combo
			quotationsComboModel.removeAllElements();
			for (String name : quotations.keySet()) {
				quotationsComboModel.addElement(name);
			}
		}
	}

	/**
	 * A class responsible for a state in which calculation results are
	 * presented to the user and he can accept, dismiss or modify them
	 */
	private class AcceptResultsState extends State {
		/**
		 * storing previous state, so entered data could be preserved
		 */
		private State previousState;
		private Map<BillableService, JLabel> serviceLabels = new HashMap<BillableService, JLabel>();
		private Map<BillableService, JTextField> serviceFields = new HashMap<BillableService, JTextField>();
		private JTextField sumField = new JTextField("20");

		public AcceptResultsState() {
			this(null);
		}

		public AcceptResultsState(State previousState) {
			this.previousState = previousState;
			//statePanel.add(new JLabel("Wyniki obliczen:"), "wrap");
			statePanel.setLayout(new MigLayout());
			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
			
			//get service types for displaying
			BillableService[] services = BillableService.class.getEnumConstants();
		
			for(BillableService serv : services) {
				JLabel lab = new JLabel(serv.toString());
				statePanel.add(lab);
				serviceLabels.put(serv, lab);
				JTextField field = new JTextField(10);
				statePanel.add(field, "wrap");
				serviceFields.put(serv, field);
			}
			statePanel.add(new JLabel("Suma: "));
			statePanel.add(sumField);
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 4");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void prev() {
			panel.remove(statePanel);

			if (previousState == null)
				internalState = new DataChooseState();
			else
				internalState = previousState;

			internalState.addPanel();
			panel.repaint();

		}
		
		public void displayCalculationResults(Map<BillableService, Float> results,
				Map<BillableService, Float> administrativeResults) {
				Float sum = 0f;
				for(BillableService serv : results.keySet()) {
					JTextField input = serviceFields.get(serv);
					Float resultValue = results.get(serv);
					sum += resultValue;
					input.setText(resultValue.toString());
				}
				sumField.setText(sum.toString());
				//TODO disabled services (or in model)
				for(BillableService serv : administrativeResults.keySet()) {
					
				}
		}
	}

	private String name = "P³atnoœci";
	private final JPanel panel = new JPanel();
	private final Controller core = Controller.getInstance();
	private State internalState = new DataChooseState();
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	/*
	 * Panel components
	 */
	JLabel firstLab = new JLabel("Krok 1");
	JLabel secondLab = new JLabel("Krok 2");

	public void displayLocums(List<LocumMockup> locums) {
	}

	public PaymentsTab() {
	}

	@Override
	public JPanel getJPanel() {
		panel.setLayout(new MigLayout());
		panel.add(firstLab, "");
		panel.add(secondLab, "wrap");
		JPanel buttonsPanel = new JPanel();
		JButton prevButton = new JButton("Wstecz");
		prevButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				internalState.prev();
				internalState.addPanel();
			}
		});
		buttonsPanel.add(prevButton);
		JButton nextButton = new JButton("Dalej");
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				internalState.next();
				internalState.addPanel();
			}
		});
		buttonsPanel.add(nextButton);
		buttonsPanel.setPreferredSize(new Dimension(750, 100));
		buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(buttonsPanel, "south");
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		return panel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	public void getReady() {
		logger.debug(name + " got ready");
		internalState.clearPanel();
		internalState.addPanel();
		core.putEvent(new LocumsListNeededEvent(this));
	}

	@Override
	public void displayLocumsList(List<LocumMockup> locums) {
		internalState.displayLocumsList(locums);
	}

	public void displayLocumMeasurementsAndQuotations(
			List<MeasurementMockup> measurements,
			Map<String, List<QuotationMockup>> quotations) {
		internalState.displayLocumMeasurementsAndQuotations(measurements,
				quotations);

	}

	public void displayCalculationResults(Map<BillableService, Float> results,
			Map<BillableService, Float> administrativeResults) {
		internalState.displayCalculationResults(results, administrativeResults);
		
	}
}
