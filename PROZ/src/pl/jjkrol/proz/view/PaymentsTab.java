package pl.jjkrol.proz.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;

import pl.jjkrol.proz.controller.CalculatedResultsNeededEvent;
import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.controller.GenerateUsageTableEvent;
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

		public void displayCalculationResults(
				Map<BillableService, Float> results,
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

		private LocumMockup selectedLocum = new LocumMockup(null, 0, 0, null,
				null, null);
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
						core.putEvent(new LocumMeasurementsAndQuotationsNeededEvent(
								moc));
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
								if (moc.date.after(selected.date)) {
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
			selectedLocum = (LocumMockup) locumsCombo.getSelectedItem();
			MeasurementMockup meaFrom =
					(MeasurementMockup) measurementsFromCombo.getSelectedItem();
			MeasurementMockup meaTo =
					(MeasurementMockup) measurementsToCombo.getSelectedItem();
			String quot = (String) quotationsCombo.getSelectedItem();
			core.putEvent(new CalculatedResultsNeededEvent(selectedLocum,
					meaFrom, meaTo, quot));
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
			// TODO sort this descending
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

		LocumMockup getSelectedLocum() {
			return selectedLocum;
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
		private Map<BillableService, JLabel> serviceLabels =
				new HashMap<BillableService, JLabel>();
		private Map<BillableService, JTextField> serviceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, JLabel> administrativeServiceLabels =
				new HashMap<BillableService, JLabel>();
		private Map<BillableService, JTextField> administrativeServiceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, Float> results;
		private Map<BillableService, Float> administrativeResults;
		private List<BillableService> administrativeServices = new ArrayList<BillableService>();
		private JTextField sumField = new JTextField("20");
		private JButton generateTableButton = new JButton("Stwórz tabelê");
		private ActionListener generateTable = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RandomAccessFile raf;
				try {
				PagePanel pan = new PagePanel();
				panel.add(pan);
					raf = new RandomAccessFile("C:\\tabelka.pdf", "r");
				FileChannel fc = raf.getChannel();
				PDFFile file;
					file = new PDFFile(fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()));
				PDFPage page = file.getPage(1);
				pan.showPage(page);
				panel.repaint();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//core.putEvent(new GenerateUsageTableEvent(results,
				//		administrativeResults));
			}
		};

		public AcceptResultsState() {
			this(null);
		}

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
			generateTableButton.addActionListener(generateTable);
			statePanel.add(generateTableButton);
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

		public void displayCalculationResults(
				Map<BillableService, Float> results,
				Map<BillableService, Float> administrativeResults) {
			this.results = results;
			this.administrativeResults = administrativeResults;
			Float sum = 0f;
			LocumMockup selectedLocum =
					((DataChooseState) previousState).getSelectedLocum();
			for (BillableService serv : results.keySet()) {
				if (!selectedLocum.enabledServices.contains(serv)) {
					results.put(serv, 0f);
				}
				JTextField input = serviceFields.get(serv);
				Float resultValue = results.get(serv);
				sum += resultValue;
				input.setText(resultValue.toString());
				if(administrativeServices.contains(serv)) {
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
	}

	/**
	 * 
	 */
	private class AcceptInvoiceDataState extends State {
		/**
		 * storing previous state, so entered data could be preserved
		 */
		private State previousState;
		private Map<BillableService, JLabel> serviceLabels =
				new HashMap<BillableService, JLabel>();
		private Map<BillableService, JTextField> serviceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, JLabel> administrativeServiceLabels =
				new HashMap<BillableService, JLabel>();
		private Map<BillableService, JTextField> administrativeServiceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, Float> results;
		private Map<BillableService, Float> administrativeResults;
		private List<BillableService> administrativeServices = new ArrayList<BillableService>();
		private JTextField sumField = new JTextField("20");
		private JButton generateTableButton = new JButton("Stwórz tabelê");
		private ActionListener generateTable = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				core.putEvent(new GenerateUsageTableEvent(results,
						administrativeResults));
			}
		};

		public AcceptInvoiceDataState() {
			// TODO Auto-generated constructor stub
		}

		public AcceptInvoiceDataState(State previousState) {

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
			generateTableButton.addActionListener(generateTable);
			statePanel.add(generateTableButton);
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

		public void displayCalculationResults(
				Map<BillableService, Float> results,
				Map<BillableService, Float> administrativeResults) {
			this.results = results;
			this.administrativeResults = administrativeResults;
			Float sum = 0f;
			LocumMockup selectedLocum =
					((DataChooseState) previousState).getSelectedLocum();
			for (BillableService serv : results.keySet()) {
				if (!selectedLocum.enabledServices.contains(serv)) {
					results.put(serv, 0f);
				}
				JTextField input = serviceFields.get(serv);
				Float resultValue = results.get(serv);
				sum += resultValue;
				input.setText(resultValue.toString());
				if(administrativeServices.contains(serv)) {
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
	}

	/**
	 * 
	 */
	private class AcceptUsageTableDataState extends State {
		/**
		 * storing previous state, so entered data could be preserved
		 */
		private State previousState;
		private Map<BillableService, JLabel> serviceLabels =
				new HashMap<BillableService, JLabel>();
		private Map<BillableService, JTextField> serviceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, JLabel> administrativeServiceLabels =
				new HashMap<BillableService, JLabel>();
		private Map<BillableService, JTextField> administrativeServiceFields =
				new HashMap<BillableService, JTextField>();
		private Map<BillableService, Float> results;
		private Map<BillableService, Float> administrativeResults;
		private List<BillableService> administrativeServices = new ArrayList<BillableService>();
		private JTextField sumField = new JTextField("20");
		private JButton generateTableButton = new JButton("Stwórz tabelê");
		private ActionListener generateTable = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				core.putEvent(new GenerateUsageTableEvent(results,
						administrativeResults));
			}
		};

		public AcceptUsageTableDataState() {
			// TODO Auto-generated constructor stub
		}

		public AcceptUsageTableDataState(State previousState) {

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
			generateTableButton.addActionListener(generateTable);
			statePanel.add(generateTableButton);
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

		public void displayCalculationResults(
				Map<BillableService, Float> results,
				Map<BillableService, Float> administrativeResults) {
			this.results = results;
			this.administrativeResults = administrativeResults;
			Float sum = 0f;
			LocumMockup selectedLocum =
					((DataChooseState) previousState).getSelectedLocum();
			for (BillableService serv : results.keySet()) {
				if (!selectedLocum.enabledServices.contains(serv)) {
					results.put(serv, 0f);
				}
				JTextField input = serviceFields.get(serv);
				Float resultValue = results.get(serv);
				sum += resultValue;
				input.setText(resultValue.toString());
				if(administrativeServices.contains(serv)) {
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
		panel.setLayout(new MigLayout());
		panel.add(firstLab, "");
		panel.add(secondLab, "wrap");
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new MigLayout());
		buttonsPanel.setPreferredSize(new Dimension(750, 30));
		JButton prevButton = new JButton("Wstecz");
		prevButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				internalState.prev();
				internalState.addPanel();
			}
		});
		buttonsPanel.add(prevButton, "");
		JButton nextButton = new JButton("Dalej");
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
	}

	@Override
	public JPanel getJPanel() {
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
