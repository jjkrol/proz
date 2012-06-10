package pl.jjkrol.proz.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javassist.compiler.ProceedHandler;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

import pl.jjkrol.proz.events.*;
import pl.jjkrol.proz.events.payments.CalculatedResultsNeededEvent;
import pl.jjkrol.proz.events.payments.GenerateInvoiceEvent;
import pl.jjkrol.proz.events.payments.GenerateUsageTableEvent;
import pl.jjkrol.proz.events.payments.LocumMeasurementsAndQuotationsNeededEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.controller.LocumsDisplayer;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.mockups.ResultMockup;

/**
 * A class responsible for handling all user interactions connected with operating on payments
 * @author   jjkrol
 */
public class PaymentsTab extends SpecificTab implements LocumsDisplayer {

	/**
	 * @author   jjkrol
	 */
	private class AcceptGeneratedUsageTableState extends State {
		private SwingController controller;
		/**
		 */
		private State previousState;

		public AcceptGeneratedUsageTableState(final State previousState) {
			this.previousState = previousState;
			statePanel.setLayout(new MigLayout());
			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
			controller = new SwingController();

			SwingViewBuilder factory = new SwingViewBuilder(controller);

			JPanel viewerComponentPanel = factory.buildViewerPanel();

			// TODO needed?
			// add copy keyboard command
			ComponentKeyBinding.install(controller, viewerComponentPanel);

			// add interactive mouse link annotation support via callback
			controller.getDocumentViewController().setAnnotationCallback(
					new org.icepdf.ri.common.MyAnnotationCallback(controller
							.getDocumentViewController()));

			statePanel.add(viewerComponentPanel);
		}

		public void displayPdf(String filename) {
			controller.openDocument(filename);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 6");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void prev() {
			panel.remove(statePanel);

			if (previousState == null)
				logger.warn("Akcja niedozwolona");
			else
				internalState = previousState;
			super.prev();
		}

		/**
		 * {@inheritDoc}
		 */
		void next() {
			panel.remove(statePanel);
			internalState = new AcceptInvoiceDataState(this);
			super.next();		}
		
		void toggleButtons() {
			unboldLabels();
			Font f = genTableLabel.getFont();
			genTableLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}

	}

	/**
	 * @author   jjkrol
	 */
	private class AcceptGeneratedInvoiceState extends State {
		private SwingController controller;
		/**
		 */
		private State previousState;

		AcceptGeneratedInvoiceState(final State previousState) {
			this.previousState = previousState;
			statePanel.setLayout(new MigLayout());
			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
			controller = new SwingController();

			SwingViewBuilder factory = new SwingViewBuilder(controller);

			JPanel viewerComponentPanel = factory.buildViewerPanel();

			// TODO needed?
			// add copy keyboard command
			ComponentKeyBinding.install(controller, viewerComponentPanel);

			// add interactive mouse link annotation support via callback
			controller.getDocumentViewController().setAnnotationCallback(
					new org.icepdf.ri.common.MyAnnotationCallback(controller
							.getDocumentViewController()));

			statePanel.add(viewerComponentPanel);
		}

		void displayPdf(String filename) {
			controller.openDocument(filename);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 6");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void prev() {
			panel.remove(statePanel);

			if (previousState == null)
				logger.warn("Akcja niedozwolona");
			else
				internalState = previousState;
			super.prev();
		}

		/**
		 * {@inheritDoc}
		 */
		void next() {
			panel.remove(statePanel);
			internalState = new Finish(this);
			super.next();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		void toggleButtons() {
			unboldLabels();
			Font f = genTableLabel.getFont();
			genInvoiceLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}

	}

	private class Finish extends State{
		private State previousState;
		Finish(State previousState) {
			this.previousState = previousState;
		}
		

		/**
		 * {@inheritDoc}
		 */
		@Override
		void prev() {
			panel.remove(statePanel);

			if (previousState == null)
				logger.warn("Akcja niedozwolona");
			else
				internalState = previousState;
			super.prev();
		}
		/**
		 * {@inheritDoc}
		 */
		void next() {
			panel.remove(statePanel);
			nextButton.setText("dalej");
			internalState = new DataChooseState();
			super.next();
		}	
		/**
		 * {@inheritDoc}
		 */
		@Override
		void toggleButtons() {
			unboldLabels();
			Font f = genTableLabel.getFont();
			genInvoiceLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			prevButton.setEnabled(true);
			nextButton.setText("Koniec");
			nextButton.setEnabled(true);
		}
		
	}
	
	/**
	 * @author   jjkrol
	 */
	private class AcceptInvoiceDataState extends State {
		private State previousState;

		public AcceptInvoiceDataState(State previousState) {

			this.previousState = previousState;
			// statePanel.add(new JLabel("Wyniki obliczen:"), "wrap");
			statePanel.setLayout(new MigLayout());
			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));

	

			statePanel.add(new JLabel("Suma: "));
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 6");
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

			super.prev();
		}

		/**
		 * {@inheritDoc}
		 */
		void next() {
			try {
				blockingQueue.put(new GenerateInvoiceEvent(result));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			panel.remove(statePanel);
			internalState = new AcceptGeneratedInvoiceState(this);
			super.next();			
		}
		
		void toggleButtons() {
			unboldLabels();
			Font f = invoiceDataLabel.getFont();
			invoiceDataLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}
	}

	/**
	 * A class responsible for a state in which calculation results are presented to the user and he can accept, dismiss or modify them
	 */
	private class AcceptResultsState extends State {

		private Map<BillableService, JTextField> administrativeServiceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, JLabel> administrativeServiceLabels =
				new HashMap<BillableService, JLabel>();
		private List<BillableService> administrativeServices =
				new ArrayList<BillableService>();
		/** storing previous state, so entered data could be preserved */
		private State previousState;
		private Map<BillableService, JTextField> serviceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, JLabel> serviceLabels =
				new HashMap<BillableService, JLabel>();
		private JTextField sumField = new JTextField("20");

		public AcceptResultsState(State previousState) {

			// FIXME that is a business rule!
			administrativeServices.add(BillableService.CO);
			administrativeServices.add(BillableService.WODA);
			administrativeServices.add(BillableService.EE);

			this.previousState = previousState;
			// statePanel.add(new JLabel("Wyniki obliczen:"), "wrap");
			statePanel.setLayout(new MigLayout());
			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));

			// get service types for displaying
			BillableService[] services =
					BillableService.class.getEnumConstants();

			for (BillableService serv : services) {
				JLabel lab = new JLabel(serv.toString());
				statePanel.add(lab);
				serviceLabels.put(serv, lab);
				JTextField field = new JTextField(10);
				if (administrativeServices.contains(serv)) {
					statePanel.add(field);
					serviceFields.put(serv, field);
					JLabel admLab = new JLabel(serv.toString());
					statePanel.add(admLab);
					administrativeServiceLabels.put(serv, admLab);
					JTextField admField = new JTextField(10);
					statePanel.add(admField, "wrap");
					administrativeServiceFields.put(serv, admField);
				} else {
					statePanel.add(field, "wrap");
					serviceFields.put(serv, field);
				}

			}

			statePanel.add(new JLabel("Suma: "));
			statePanel.add(sumField);
		}

		public void displayCalculationResults() {
			Map<BillableService, BigDecimal> results = result.getResults();
			Map<BillableService, BigDecimal> administrativeResults =
					result.getAdministrativeResults();
			BigDecimal sum = new BigDecimal(0);
			LocumMockup selectedLocum = result.getLocum();
			for (BillableService serv : results.keySet()) {
				if (!selectedLocum.getEnabledServices().contains(serv)) {
					results.put(serv, new BigDecimal(0));
				}
				JTextField input = serviceFields.get(serv);
				BigDecimal resultValue = results.get(serv);
				sum = sum.add(resultValue);
				input.setText(resultValue.toString());
				if (administrativeServices.contains(serv)) {
					resultValue = administrativeResults.get(serv);
					input = administrativeServiceFields.get(serv);
					input.setText(resultValue.toString());
				}
			}
			sumField.setText(sum.toString());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 6");
		}

		/**
		 * {@inheritDoc}
		 */
		void next() {
			panel.remove(statePanel);
			internalState = new AcceptUsageTableDataState(this, result);
			super.next();
			
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
			super.prev();
		}

		void toggleButtons() {
			unboldLabels();
			Font f = resultLabel.getFont();
			resultLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}
	}

	/**
	 * @author   jjkrol
	 */
	private class AcceptUsageTableDataState extends State {
		private Map<BillableService, Float> administrativeResults;
		private Map<BillableService, JTextField> administrativeServiceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, JLabel> administrativeServiceLabels =
				new HashMap<BillableService, JLabel>();
		private List<BillableService> administrativeServices =
				new ArrayList<BillableService>();
		/**
		 * @uml.property  name="previousState"
		 * @uml.associationEnd  
		 */
		private State previousState;
		/**
		 * storing previous state, so entered data could be preserved
		 * @uml.property  name="result"
		 * @uml.associationEnd  
		 */
		private Map<BillableService, Float> results;
		private Map<BillableService, JTextField> serviceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, JLabel> serviceLabels =
				new HashMap<BillableService, JLabel>();
		private JTextField sumField = new JTextField("20");

		public AcceptUsageTableDataState(final State previousState,
				final ResultMockup givenResult) {
			this.previousState = previousState;
			result = givenResult;
			statePanel.setLayout(new MigLayout());
			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));

			/*
			 * // FIXME that is a business rule!
			 * administrativeServices.add(BillableService.CO);
			 * administrativeServices.add(BillableService.WODA);
			 * administrativeServices.add(BillableService.EE);
			 * 
			 * 
			 * // get service types for displaying BillableService[] services =
			 * BillableService.class.getEnumConstants();
			 * 
			 * for (BillableService serv : services) { JLabel lab = new
			 * JLabel(serv.toString()); statePanel.add(lab);
			 * serviceLabels.put(serv, lab); JTextField field = new
			 * JTextField(10); if (administrativeServices.contains(serv)) {
			 * statePanel.add(field); serviceFields.put(serv, field); JLabel
			 * admLab = new JLabel(serv.toString()); statePanel.add(admLab);
			 * administrativeServiceLabels.put(serv, admLab); JTextField
			 * admField = new JTextField(10); statePanel.add(admField, "wrap");
			 * administrativeServiceFields.put(serv, admField); } else {
			 * statePanel.add(field, "wrap"); serviceFields.put(serv, field); }
			 * 
			 * } statePanel.add(new JLabel("Suma: ")); statePanel.add(sumField);
			 * generateTableButton.addActionListener(generateTable);
			 * statePanel.add(generateTableButton);
			 */
		}

		public void displayCalculationResults(
				Map<BillableService, Float> results,
				Map<BillableService, Float> administrativeResults) {
			this.results = results;
			this.administrativeResults = administrativeResults;
			Float sum = 0f;
			LocumMockup selectedLocum =
					((DataChooseState) previousState).getSelectedLocum();
			for (BillableService serv : results.keySet()) {
				if (!selectedLocum.getEnabledServices().contains(serv)) {
					results.put(serv, 0f);
				}
				JTextField input = serviceFields.get(serv);
				Float resultValue = results.get(serv);
				sum += resultValue;
				input.setText(resultValue.toString());
				if (administrativeServices.contains(serv)) {
					resultValue = administrativeResults.get(serv);
					input = administrativeServiceFields.get(serv);
					input.setText(resultValue.toString());
				}
			}
			sumField.setText(sum.toString());
			// TODO disabled services (or in model)
			for (BillableService serv : administrativeResults.keySet()) {

			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 6");
		}

		@Override
		void next() {
			try {
				blockingQueue.put(new GenerateUsageTableEvent(result));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			panel.remove(statePanel);
			internalState = new AcceptGeneratedUsageTableState(this);
			super.next();
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
			super.prev();
		}

		void toggleButtons() {
			unboldLabels();
			Font f = tableDataLabel.getFont();
			tableDataLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			prevButton.setEnabled(true);
			nextButton.setEnabled(true);
		}
	}

	/**
	 * Class responsible for state in which the user is entering data concerning locum, date and quotations to calculate payment.
	 */
	private class DataChooseState extends State {
		private DefaultComboBoxModel locumsComboModel =
				new DefaultComboBoxModel();
		private JComboBox locumsCombo = new JComboBox(locumsComboModel);

		private List<MeasurementMockup> measurements;
		private DefaultComboBoxModel measurementsFromComboModel =
				new DefaultComboBoxModel();
		private JComboBox measurementsFromCombo = new JComboBox(
				measurementsFromComboModel);
		private DefaultComboBoxModel measurementsToComboModel =
				new DefaultComboBoxModel();
		private JComboBox measurementsToCombo = new JComboBox(
				measurementsToComboModel);
		private Map<String, List<QuotationMockup>> quotations;
		private DefaultComboBoxModel quotationsComboModel =
				new DefaultComboBoxModel();
		private JComboBox quotationsCombo = new JComboBox(quotationsComboModel);
		/**
		 * @uml.property  name="selectedLocum"
		 * @uml.associationEnd  
		 */
		private LocumMockup selectedLocum = new LocumMockup(null, 0, 0, null,
				null, null,null,null);

		/**
		 * Constructor creates the state panel and all its components
		 */
		DataChooseState() {
			result = null;
			
			statePanel.setLayout(new MigLayout());
			statePanel.add(new JLabel("Lokal: "));
			statePanel.add(locumsCombo, "wrap");
			locumsCombo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO maybe replace it with data passed in locumMockup?
					// get name selected locum name
					LocumMockup moc =
							(LocumMockup) locumsCombo.getSelectedItem();
					if (moc != null) {
						measurementsToComboModel.removeAllElements();
						measurementsFromComboModel.removeAllElements();
						quotationsComboModel.removeAllElements();
						// create empty mockup
						try {
							blockingQueue
									.put(new LocumMeasurementsAndQuotationsNeededEvent(
											moc));
						} catch (InterruptedException e) {
							logger.warn(e.getMessage());
							e.printStackTrace();
						}
					}
				}
			});

			// combo for beginning date
			statePanel.add(new JLabel("Od: "));
			statePanel.add(measurementsFromCombo, "wrap");
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
								if (moc.getDate().after(selected.getDate())) {
									measurementsToComboModel.addElement(moc);
								}
							}
						}
					}
				}
			});
			// combo for ending date
			statePanel.add(new JLabel("Do"));
			statePanel.add(measurementsToCombo, "wrap");

			// combo for quotation
			statePanel.add(new JLabel("Stawki"));
			statePanel.add(quotationsCombo, "wrap");

			statePanel.setPreferredSize(new Dimension(700, 450));
			statePanel.setBorder(BorderFactory.createLineBorder(Color.black));
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
			// TODO sort this descending
			measurementsFromComboModel.removeAllElements();
			for (MeasurementMockup mock : measurements) {
				measurementsFromComboModel.addElement(mock);
			}

			// fill to combo
			measurementsToComboModel.removeAllElements();
			MeasurementMockup oldestMeasurement = measurements.get(0);
			for (MeasurementMockup mock : measurements) {
				if (mock.getDate().before(oldestMeasurement.getDate()))
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

		/**
		 * {@inheritDoc}
		 */
		@Override
		void addPanel() {
			panel.add(statePanel, "span 6");
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
		 * @return
		 * @uml.property  name="selectedLocum"
		 */
		LocumMockup getSelectedLocum() {
			return selectedLocum;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		void next() {
			panel.remove(statePanel);
			selectedLocum = (LocumMockup) locumsCombo.getSelectedItem();
			MeasurementMockup meaFrom =
					(MeasurementMockup) measurementsFromCombo.getSelectedItem();
			MeasurementMockup meaTo =
					(MeasurementMockup) measurementsToCombo.getSelectedItem();
			String quot = (String) quotationsCombo.getSelectedItem();
			try {
				blockingQueue.put(new CalculatedResultsNeededEvent(
						selectedLocum, meaFrom, meaTo, quot));
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
				e.printStackTrace();
			}
			internalState = new AcceptResultsState(this);
			super.next();
		}

		void toggleButtons() {
			unboldLabels();
			Font f = dataChoiceLabel.getFont();
			dataChoiceLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
			prevButton.setEnabled(false);
			nextButton.setEnabled(true);
		}
	}

	/**
	 * General class representing the tab state
	 */
	private class State {
		protected JPanel statePanel = new JPanel();

		void displayCalculationResults() {
		}

		void displayLocumMeasurementsAndQuotations(
				List<MeasurementMockup> measurements,
				Map<String, List<QuotationMockup>> quotations) {
		}

		/**
		 * Adds a state-specific panel to the main tab panel
		 */
		void addPanel() {
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

		/**
		 * Changes tab state to the next step
		 */
		void next() {
			internalState.addPanel();
			panel.repaint();
			internalState.toggleButtons();
		}

		/**
		 * Changes tab state to the previous step
		 */
		void prev() {
			internalState.addPanel();
			panel.repaint();
			internalState.toggleButtons();
		}

		/**
		 * sets states of buttons and labels
		 */
		void toggleButtons() {
		}

		void unboldLabels() {
			for (JLabel label : wizardLabels) {
				Font f = label.getFont();
				label.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
			}
		}

		/**
		 * displays a specified file in the tab
		 * @param filename
		 */
		void displayPdf(String filename) {
		}
	}

	static Logger logger = Logger.getLogger(PROZJFrame.class);
	private BlockingQueue<PROZEvent> blockingQueue;
	private State internalState = new DataChooseState();
	private String name = "P³atnoœci";

	private ResultMockup result;
	private JButton nextButton = new JButton("Dalej");
	private final JPanel panel = new JPanel();
	private JButton prevButton = new JButton("Wstecz");
	private List<JLabel> wizardLabels = new ArrayList<JLabel>();
	
	/*
	 * Panel components
	 * TODO add to a map?
	 */
	private JLabel dataChoiceLabel = new JLabel("Wybór danych");
	private JLabel genInvoiceLabel = new JLabel("Wygenerowana faktura");

	private JLabel genTableLabel = new JLabel("Wygenerowana tabelka");
	private JLabel invoiceDataLabel = new JLabel("Dane do faktury");
	private JLabel resultLabel = new JLabel("Wynik obliczeñ");
	private JLabel tableDataLabel = new JLabel("Dane do tabelki");

	public PaymentsTab(BlockingQueue<PROZEvent> blockingQueue) {
		this.blockingQueue = blockingQueue;
		panel.setLayout(new MigLayout());
		panel.add(dataChoiceLabel, "");
		wizardLabels.add(dataChoiceLabel);
		panel.add(resultLabel, "");
		wizardLabels.add(resultLabel);
		panel.add(tableDataLabel, "");
		wizardLabels.add(tableDataLabel);
		panel.add(genTableLabel, "");
		wizardLabels.add(genTableLabel);
		panel.add(invoiceDataLabel, "");
		wizardLabels.add(invoiceDataLabel);
		panel.add(genInvoiceLabel, "wrap");
		wizardLabels.add(genInvoiceLabel);
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new MigLayout());
		buttonsPanel.setPreferredSize(new Dimension(750, 30));
		prevButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				internalState.prev();
				internalState.addPanel();
			}
		});
		buttonsPanel.add(prevButton, "");
		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				internalState.next();
				internalState.addPanel();
			}
		});
		buttonsPanel.add(nextButton, "");
		panel.add(buttonsPanel, "south");
		internalState.addPanel();
		internalState.toggleButtons();
	}

	public void displayCalculationResults(final ResultMockup moc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				result = moc;
				internalState.displayCalculationResults();
			}
		});

	}

	public void displayLocumMeasurementsAndQuotations(
			final List<MeasurementMockup> measurements,
			final Map<String, List<QuotationMockup>> quotations) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				internalState.displayLocumMeasurementsAndQuotations(
						measurements, quotations);
			}
		});

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void displayLocumsList(final List<LocumMockup> locums) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				internalState.displayLocumsList(locums);
			}
		});
	}

	/**
	 * displays a pdf file with usage table
	 * 
	 * @param filename
	 */
	public void displayUsageTable(final String filename) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO excepetions, reuse in other rab
				internalState.displayPdf(filename);
			}
		});
	}

	@Override
	public JPanel getJPanel() {
		return panel;
	}

	/**
	 * {@inheritDoc} 
	 * @uml.property  name="name"
	 */
	@Override
	public String getName() {
		return name;
	}

	public void getReady() {
		logger.debug(name + " got ready");
		try {
			blockingQueue.put(new LocumsListNeededEvent(this));
		} catch (InterruptedException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
	}
}
