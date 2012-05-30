package pl.jjkrol.proz.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

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

import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.model.Counter;
import pl.jjkrol.proz.model.DocumentBuilder;
import pl.jjkrol.proz.model.DocumentDirector;
import pl.jjkrol.proz.model.Flat;
import pl.jjkrol.proz.model.House;
import pl.jjkrol.proz.model.Locum;
import pl.jjkrol.proz.model.MeasurableService;
import pl.jjkrol.proz.model.NoSuchLocum;
import pl.jjkrol.proz.model.NoSuchQuotationSet;
import pl.jjkrol.proz.model.Occupant;
import pl.jjkrol.proz.model.OccupantsRegister;
import pl.jjkrol.proz.model.Office;
import pl.jjkrol.proz.model.Ownership;
import pl.jjkrol.proz.model.PaymentCalculator;
import pl.jjkrol.proz.model.Quotation;
import pl.jjkrol.proz.model.UsageTableBuilder;
import pl.jjkrol.proz.view.InvoicesTab;
import pl.jjkrol.proz.view.LocumsTab;
import pl.jjkrol.proz.view.MeasurementsTab;
import pl.jjkrol.proz.view.OccupantsTab;
import pl.jjkrol.proz.view.PROZJFrame;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.ReportsTab;
import pl.jjkrol.proz.view.SpecificTab;
import pl.jjkrol.proz.view.View;
import au.com.bytecode.opencsv.CSVReader;

/**
 * A class responsible for the application control flow
 * 
 * @author jjkrol
 * 
 */
public class Controller {
	private Locum m01, m1, m2, m3, m4, m5, m6, m7, m8;
	private House mainHouse;
	private BlockingQueue<PROZEvent> blockingQueue =
			new LinkedBlockingQueue<PROZEvent>();
	private PaymentCalculator calculator;
	private final View view = new View();
	private final OccupantsRegister occupantsRegister = OccupantsRegister
			.getInstance();
	static Logger logger = Logger.getLogger(PROZJFrame.class);

	private final HashMap<Class, PROZStrategy> eventDictionary =
			new HashMap<Class, PROZStrategy>();

	private static volatile Controller instance = null;

	public static Controller getInstance() {
		if (instance == null) {
			synchronized (Controller.class) {
				if (instance == null)
					instance = new Controller();
			}
		}
		return instance;
	}
	
	protected Controller() {
			eventDictionary.put(ViewPaymentsEvent.class, 
					new DisplayLocumsForPayments());
			eventDictionary.put(OccupantsListNeededEvent.class,
					new DisplayOccupantsForOccupants());
			eventDictionary
					.put(OccupantChosenForViewingEvent.class, new DisplayOccupantDataForOccupants());
			eventDictionary.put(AddOccupantEvent.class, new AddOccupantData());
			eventDictionary.put(SaveOccupantEvent.class, new SaveOccupantData());
			eventDictionary.put(DeleteOccupantEvent.class, new DeleteOccupantData());

			eventDictionary.put(LocumsListNeededEvent.class, new DisplayLocums());
			eventDictionary.put(LocumChosenForViewingEvent.class, new DisplayLocumMeasurements());
			eventDictionary.put( LocumMeasurementsAndQuotationsNeededEvent.class,  new DisplayLocumMeasurementsAndQuotations());
			eventDictionary.put(CalculatedResultsNeededEvent.class, new DisplayCalculatedResults());
			eventDictionary.put(GenerateUsageTableEvent.class, new GenerateUsageTable());
	}

	/**
	 * main function, creates and runs gui
	 * 
	 * @param calc
	 */
	public void run(PaymentCalculator calculator) {
		this.calculator = calculator;
		List<SpecificTab> views = new ArrayList<SpecificTab>();
		views.add(new MeasurementsTab());
		views.add(new PaymentsTab());
		views.add(new OccupantsTab());
		views.add(new LocumsTab());
		views.add(new InvoicesTab());
		views.add(new ReportsTab());
		view.startGUI(views);
		runLoop();
	}

	private void runLoop() {
		while (true) {
			try {
				PROZEvent event = blockingQueue.take();
				logger.debug("Event taken");
				if(!eventDictionary.containsKey(event.getClass())) {
					logger.warn("No such class in the dictionary");
				}
				PROZStrategy obj = eventDictionary.get(event.getClass()); 
				try {
					logger.debug(obj);
					obj.execute(event);
				}catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				logger.warn("Interrupted exception on take event "
						+ e.getMessage());
			}
		}
	}

	public void prepareInitialData() {
		createHouse();
		System.out.println("Tworzê nowy dom");
		System.out.println(mainHouse.getLocums());
	}

	public void putEvent(PROZEvent event) {
		try {
			blockingQueue.put(event);
		} catch (InterruptedException e) {
			logger.warn("Interrupted exception on put event " + e.getMessage());
		}

	}

	/*
	 * VIEW SPECIFIC METHODS
	 */

	abstract private class PROZStrategy{
		abstract void execute(PROZEvent e);
	}
	/**
	 * displays locums on payments panel TODO change for the general method
	 * displayLocums
	 */
	class DisplayLocumsForPayments extends PROZStrategy{
	void execute(PROZEvent e) {
		final List<LocumMockup> locums = new ArrayList<LocumMockup>();
		for (Locum loc : mainHouse.getLocums()) {
			locums.add(loc.getMockup());
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				PaymentsTab v =
						(PaymentsTab) view.getSpecificView(PaymentsTab.class);
				v.displayLocums(locums);
			}
		});
	}
	}

	/**
	 * displays occupants on occupants panel
	 */
	class DisplayOccupantsForOccupants extends PROZStrategy{
		void execute(PROZEvent e) {
		final List<OccupantMockup> occMocks = new ArrayList<OccupantMockup>();
		for (OccupantMockup occ : occupantsRegister.getOccupantsMockups()) {
			occMocks.add(occ);
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				OccupantsTab v =
						(OccupantsTab) view.getSpecificView(OccupantsTab.class);
				v.displayOccupantsList(occMocks);
			}
		});
	}
	}

	class DisplayOccupantDataForOccupants extends PROZStrategy{
	public void execute(PROZEvent e) {
		Occupant occ =
				occupantsRegister
						.findOccupant(((OccupantChosenForViewingEvent) e).moc);
		final OccupantMockup moc = occ.getMockup();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				OccupantsTab v =
						(OccupantsTab) view.getSpecificView(OccupantsTab.class);
				v.displayOccupantsData(moc);
			}
		});
	}
	}
class AddOccupantData extends PROZStrategy{
	public void execute(PROZEvent e) {
		OccupantMockup moc = ((AddOccupantEvent) e).mockup;
		occupantsRegister.createOccupant(moc);
		PROZStrategy s = new DisplayOccupantsForOccupants();
		s.execute(e);
	}
}
	class SaveOccupantData extends PROZStrategy{
	public void execute(PROZEvent e) {
		OccupantMockup moc = ((SaveOccupantEvent) e).mockup;
		occupantsRegister.editOccupant(moc.id, moc);
		PROZStrategy s = new DisplayOccupantsForOccupants();
		s.execute(e);
	}
	}
class DeleteOccupantData extends PROZStrategy{
	public void execute(PROZEvent e) {
		OccupantMockup moc = ((DeleteOccupantEvent) e).mockup;
		occupantsRegister.deleteOccupant(moc.id);
		PROZStrategy s = new DisplayOccupantsForOccupants();
		s.execute(e);
	}
}
	/**
	 * displays locums on locums panel
	 */
class DisplayLocums extends PROZStrategy{
	public DisplayLocums() {
		logger.debug("kotek");
	}
	
	public void execute(PROZEvent e) {
		final List<LocumMockup> locMocks = new ArrayList<LocumMockup>();
		for (Locum loc : mainHouse.getLocums()) {
			locMocks.add(loc.getMockup());
		}
		final LocumsDisplayer d = ((LocumsListNeededEvent) e).caller;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				d.displayLocumsList(locMocks);
			}
		});
	}
}
	/**
	 * display single locum measurements on the measurements tab
	 * 
	 * @param e
	 */
class DisplayLocumMeasurements extends PROZStrategy{
	public void execute(PROZEvent e) {
		LocumMockup emptyMockup = ((LocumChosenForViewingEvent) e).moc;
		String locumName = emptyMockup.name;
		try {
			Locum loc = mainHouse.getLocumByName(locumName);
			final List<MeasurementMockup> mocs = loc.getMeasurementsMockups();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MeasurementsTab v =
							(MeasurementsTab) view
									.getSpecificView(MeasurementsTab.class);
					v.displayMeasurements(mocs);
				}
			});
		} catch (NoSuchLocum exception) {
			// TODO some messagebox?
		}

	}
}
class DisplayLocumMeasurementsAndQuotations extends PROZStrategy{
	public void execute(PROZEvent e) {
		LocumMockup emptyMockup =
				((LocumMeasurementsAndQuotationsNeededEvent) e).moc;
		String locumName = emptyMockup.name;
		try {
			Locum loc = mainHouse.getLocumByName(locumName);

			// get measurement and quotation data
			final List<MeasurementMockup> measurements =
					loc.getMeasurementsMockups();
			final Map<String, List<QuotationMockup>> quotations =
					loc.getQuotationsMockups();
			final PaymentsTab v =
					(PaymentsTab) view.getSpecificView(PaymentsTab.class);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					v.displayLocumMeasurementsAndQuotations(measurements,
							quotations);
				}
			});
		} catch (NoSuchLocum exception) {
			// TODO some messagebox?
		}
	}
}
class DisplayCalculatedResults extends PROZStrategy{
	public void execute(PROZEvent e) {
		LocumMockup emptyLocum =
				((CalculatedResultsNeededEvent) e).locum;
		Calendar from =
				((CalculatedResultsNeededEvent) e).from.date;
		Calendar to =
				((CalculatedResultsNeededEvent) e).to.date;
		String quotation =
				((CalculatedResultsNeededEvent) e).quotation;
		try {
			Locum loc = mainHouse.getLocumByName(emptyLocum.name);
			final Map<BillableService, Float> results = 
					calculator.calculatePayment(mainHouse, loc, from, to, quotation);
			final Map<BillableService, Float> administrativeResults = 
					calculator.calculateAdministrativePayment(mainHouse, loc, from, to, quotation);
			// get measurement and quotation data
			final PaymentsTab c = (PaymentsTab) view.getSpecificView(PaymentsTab.class);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					c.displayCalculationResults(results, administrativeResults);
				}
			});
		} catch (NoSuchLocum exception) {
			// TODO some messagebox?
		}
		catch(NoSuchQuotationSet exception) {
			//TODO some messagebox?
		}
	}
}
	/*
	 * DATA SPECIFIC METHODS
	 */
	/**
	 * reads data from specific csv fil
	 */
	public void readSampleData() {
		List<String[]> myEntries;
		try {
			CSVReader reader = new CSVReader(new FileReader("data/data.csv"));
			myEntries = reader.readAll();
		} catch (Exception e) {
			logger.warn("Data file not found!");
			return;
		}

		int lineIndex = 0;
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		List<Calendar> dates = new ArrayList<Calendar>();
		String[] line = myEntries.get(lineIndex++);
		for (String value : line) {
			Date date;
			try {
				date = df.parse(value);
			} catch (Exception e) {
				return; // TODO fix this
			}
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			dates.add(cal);
		}

		List<MeasurableService> houseServices =
				Arrays.asList(MeasurableService.GAZ, MeasurableService.EE,
						MeasurableService.EE_ADM, MeasurableService.CIEPLO,
						MeasurableService.CO_ADM, MeasurableService.WODA_GL,
						MeasurableService.CW, MeasurableService.POLEWACZKI);

		for (MeasurableService currentService : houseServices) {
			line = myEntries.get(lineIndex++);

			int i = 0;
			for (String value : line) {
				mainHouse.addMeasure(dates.get(i), currentService, new Float(
						value));
				i++;
			}
		}

		List<MeasurableService> locumServices =
				Arrays.asList(MeasurableService.CO, MeasurableService.ZW,
						MeasurableService.CW, MeasurableService.CCW,
						MeasurableService.GAZ, MeasurableService.EE);

		List<BillableService> enabledServices = new ArrayList<BillableService>();
		for(BillableService serv : BillableService.class.getEnumConstants()) {
			enabledServices.add(serv);
		}
		
		for (Locum loc : mainHouse.getLocums()) {
			Map<Calendar, Map<MeasurableService, Float>> measures =
					new HashMap<Calendar, Map<MeasurableService, Float>>();

			for (MeasurableService currentService : locumServices) {
				line = myEntries.get(lineIndex++);

				int i = 0;
				for (String value : line) {
					if (value.isEmpty())
						value = "0";
					Calendar date = dates.get(i);
					if (!measures.containsKey(date))
						measures.put(date,
								new HashMap<MeasurableService, Float>());
					measures.get(date).put(currentService, new Float(value));
					i++;
				}
			}

			for (Calendar date : measures.keySet()) {
				loc.addMeasures(date, measures.get(date));
			}
		}
		// quotations

		List<Quotation> quotations = new ArrayList<Quotation>();
		quotations.add(new Quotation(20f, BillableService.INTERNET));
		quotations.add(new Quotation(27.39f, BillableService.SMIECI));
		quotations.add(new Quotation(90f, BillableService.CO));
		quotations.add(new Quotation(2.59f, BillableService.WODA));
		quotations.add(new Quotation(19f, BillableService.SCIEKI));
		quotations.add(new Quotation(0.51f, BillableService.EE));
		quotations.add(new Quotation(2.35f, BillableService.GAZ));
		quotations.add(new Quotation(23.24f, BillableService.PODGRZANIE));

		for (Locum loc : mainHouse.getLocums()) {
			loc.addQuotationSet("pierwsze", quotations);
			loc.setEnabledServices(new ArrayList<BillableService>(enabledServices));
		}
		enabledServices = m1.getEnabledServices();
		enabledServices.remove(BillableService.SMIECI);
	}

	/**
	 * creates house and locums, inserts occupants
	 */
	public void createHouse() {
		Map<MeasurableService, Counter> counters =
				new HashMap<MeasurableService, Counter>();
		mainHouse = new House("dom1", "Bronowska 52", counters);
		counters.put(MeasurableService.GAZ, new Counter("m3"));
		counters.put(MeasurableService.POLEWACZKI, new Counter("m3"));
		counters.put(MeasurableService.EE, new Counter("kWh"));
		counters.put(MeasurableService.WODA_GL, new Counter("m3"));
		counters.put(MeasurableService.EE_ADM, new Counter("kWh"));
		counters.put(MeasurableService.CO, new Counter("m3"));
		counters.put(MeasurableService.CIEPLO, new Counter("kWh"));
		counters.put(MeasurableService.CO_ADM, new Counter("m3"));
		counters.put(MeasurableService.CW, new Counter("m3"));

		m01 = new Flat(50, "m01");
		m01.setParticipationFactor(0);

		m1 = new Flat(50, "m1");
		m1.setParticipationFactor(0.2f);

		m2 = new Flat(163, "m2", Ownership.OWN);
		m2.setParticipationFactor(0.2f);

		m3 = new Flat(50, "m3");
		m3.setParticipationFactor(0.1f);

		m4 = new Flat(32, "m4");
		m4.setParticipationFactor(0.2f);

		m5 = new Office(62, "m5");
		m5.setParticipationFactor(0.2f);

		m6 = new Office(57, "m6");
		m6.setParticipationFactor(0.1f);

		m7 = new Flat(31, "m7", Ownership.OWN);
		m7.setParticipationFactor(0);

		m8 = new Flat(70, "m8", Ownership.OWN);
		m8.setParticipationFactor(0);

		generateOccupants();

		mainHouse.addLocum(m01);
		mainHouse.addLocum(m1);
		mainHouse.addLocum(m2);
		mainHouse.addLocum(m3);
		mainHouse.addLocum(m4);
		mainHouse.addLocum(m5);
		mainHouse.addLocum(m6);
		mainHouse.addLocum(m7);
		mainHouse.addLocum(m8);

		for (Locum loc : mainHouse.getLocums()) {
			counters = new HashMap<MeasurableService, Counter>();
			counters.put(MeasurableService.CO, new Counter("m3"));
			counters.put(MeasurableService.ZW, new Counter("m3"));
			counters.put(MeasurableService.CW, new Counter("m3"));
			counters.put(MeasurableService.CCW, new Counter("m3"));
			counters.put(MeasurableService.GAZ, new Counter("m3"));
			counters.put(MeasurableService.EE, new Counter("kWh"));
			loc.setCounters(counters);

			/*
			 * Map<BillableService, Map<String, Quotation>> quotations = new
			 * HashMap<BillableService, Map<String, Quotation>>(); quotations
			 * .put(BillableService.CO, new HashMap<String, Quotation>());
			 * quotations.put(BillableService.WODA, new HashMap<String,
			 * Quotation>()); quotations .put(BillableService.EE, new
			 * HashMap<String, Quotation>());
			 * quotations.put(BillableService.GAZ, new HashMap<String,
			 * Quotation>()); quotations.put(BillableService.INTERNET, new
			 * HashMap<String, Quotation>());
			 * quotations.put(BillableService.PODGRZANIE, new HashMap<String,
			 * Quotation>()); quotations.put(BillableService.SMIECI, new
			 * HashMap<String, Quotation>());
			 * quotations.put(BillableService.SCIEKI, new HashMap<String,
			 * Quotation>()); loc.setQuotations(quotations);
			 */
		}
	}

	private void generateOccupants() {
		Occupant occ;
		occ = occupantsRegister.createOccupant("Beata Kozak-Szulc");
		m1.addOccupant(occ);
		m1.setBillingPerson(occ);
		occ = occupantsRegister.createOccupant("Pan Szulc");
		m1.addOccupant(occ);
		occ = occupantsRegister.createOccupant("Katarzyna Wiœniewska");
		m2.addOccupant(occ);
		occ = occupantsRegister.createOccupant("Jakub Król");
		m2.addOccupant(occ);
		occ = occupantsRegister.createOccupant("Magdalena Lis");
		m3.addOccupant(occ);
	}
	
	private class GenerateUsageTable extends PROZStrategy{
	public void execute(PROZEvent e) {
			//FIXME move to model
		final Map<BillableService, Float> results = ((GenerateUsageTableEvent)e).results;
			final Map<BillableService, Float> administrativeResults = ((GenerateUsageTableEvent)e).administrativeResults;
			DocumentBuilder builder = new UsageTableBuilder();
			DocumentDirector director = new DocumentDirector(builder);
			director.buildDocument(results, administrativeResults);
		}
	}
	
	public void generateInvoice(PROZEvent e) {
			//FIXME move to model
			final Map<BillableService, Float> results = ((GenerateUsageTableEvent)e).results;
				final Map<BillableService, Float> administrativeResults = ((GenerateUsageTableEvent)e).administrativeResults;
				Document document = new Document(PageSize.A4, 50, 50, 50, 50);
				try {
					PdfWriter writer =
							PdfWriter.getInstance(document, new FileOutputStream(
									"C:\\tabelka.pdf"));
					document.open();
					PdfPTable t = new PdfPTable(7);
					t.setWidthPercentage(100f);
					t.setWidths(new int[] {3,1,1,1,1,1,1});
					try {
		            BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", 
		                    BaseFont.CP1250, BaseFont.EMBEDDED); 
		                    Font font = new Font(bf, 12); 
					PdfPCell cell;
					t.getDefaultCell().setPadding(5);
					cell = new PdfPCell();
					cell.setRowspan(2);
					t.addCell(cell);
					cell = new PdfPCell(new Phrase("Okres rozliczeniowy"));
					cell.setColspan(2);	
					t.addCell(cell);
					cell = new PdfPCell(new Phrase("Op³aty mieszkaniowe", font));
					cell.setColspan(2);	
					t.addCell(cell);
					cell = new PdfPCell(new Phrase("Op³aty administracyjne",font));
					cell.setColspan(2);	
					t.addCell(cell);
					cell = new PdfPCell(new Phrase("poczatek"));
					t.addCell(cell);
					cell = new PdfPCell(new Phrase("koniec"));
					t.addCell(cell);
					t.addCell(new Phrase("stawka", font));
					t.addCell(new Phrase("op³ata", font));
					t.addCell(new Phrase("wsp.", font));
					t.addCell(new Phrase("op³ata", font));
					t.addCell("Centralne Ogrzewanie");
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.CO)));
					t.addCell("");
					t.addCell("");
					t.addCell(new Phrase("Woda zimna i ciep³a", font));
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.WODA)));
					t.addCell("");
					t.addCell("");
					t.addCell(new Phrase("Podgrzanie ciep³ej wody", font));
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.PODGRZANIE)));
					t.addCell("");
					t.addCell("");
					t.addCell(new Phrase("Wywóz œcieków",font));
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.SCIEKI)));
					t.addCell("");
					t.addCell("");
					t.addCell("Gaz");
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.GAZ)));
					t.addCell("");
					t.addCell("");
					t.addCell("Energia elektryczna");
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.EE)));
					t.addCell("");
					t.addCell("");
					t.addCell(new Phrase("Wywóz œmieci", font));
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.SMIECI)));;
					t.addCell("");
					t.addCell("");
					t.addCell("Internet");
					t.addCell("");
					t.addCell("");
					t.addCell("");
					t.addCell(new DecimalFormat("#.##").format(results.get(BillableService.INTERNET)));
					t.addCell("");
					t.addCell("");
					t.addCell(new Phrase("Nale¿na suma op³at za media", font));
					cell = new PdfPCell();
					cell.setColspan(3);
					t.addCell(cell);
					t.addCell("suma");
					t.addCell("");
					t.addCell("suma");
					document.add(t);
					Paragraph par = new Paragraph("Pobrana op³ata: op³ata",font);
					document.add(par);
					par = new Paragraph("Nale¿noœæ: op³ata", font);
					document.add(par);
					document.close();
					}
					catch(IOException exc) {
						
					}
					try {
						Process p =
								Runtime.getRuntime()
										.exec("rundll32 url.dll,FileProtocolHandler c:\\tabelka.pdf");
						p.waitFor();
					} catch (Exception exc) {

					}
				} catch (FileNotFoundException exc) {
				} catch (DocumentException exc) {
				}
			}
}
