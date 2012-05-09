package pl.jjkrol.proz.controller;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.io.FileReader;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import pl.jjkrol.proz.model.*;
import pl.jjkrol.proz.view.*;

import au.com.bytecode.opencsv.*;
import org.apache.log4j.Logger;

/**
 * A class responsible for the application control flow
 * @author jjkrol
 *
 */
public class Controller {
	private Locum m01, m1, m2, m3, m4, m5, m6, m7, m8;
	private House mainHouse;
	private final View view = new View();
	static Logger logger = Logger.getLogger(PROZJFrame.class);
	
	private final HashMap<Class, Method> eventDictionary = new HashMap<Class, Method>();

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
		try{
		eventDictionary.put(MainButtonClickedEvent.class, Controller.class.getMethod("reactToEvent"));
		eventDictionary.put(ViewPayments.class, Controller.class.getMethod("displayLocumsForPayments"));
		}
		catch(NoSuchMethodException e){
			logger.warn("Exception in initializing method dictionary: "+e.getMessage());
		}
	}

	public void prepareInitialData() {
		Map<MeasurableService, Counter> counters = new HashMap<MeasurableService, Counter>();
		House proto = new House("dom1", "Bronowska 52", counters);
		// ObjectSet<House> result = db.queryByExample(proto);
		// if (result.isEmpty()){
		createHouse();
		// db.store(mainHouse);
		System.out.println("Tworzê nowy dom");
		// }
		// else{
		// mainHouse = result.get(0);
		// }
		System.out.println(mainHouse.getLocums());
	}

	public void createHouse() {
		Map<MeasurableService, Counter> counters = new HashMap<MeasurableService, Counter>();
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
		Occupant szulc = new Occupant("Beata Kozak-Szulc");
		m1.addOccupant(szulc);
		m1.setBillingPerson(szulc);
		m1.addOccupant(new Occupant("Pan Szulc"));
		m1.setParticipationFactor(0.2f);

		m2 = new Flat(163, "m2", Ownership.OWN);
		m2.addOccupant(new Occupant("Katarzyna Wiœniewska"));
		m2.addOccupant(new Occupant("Jakub Król"));
		m2.setParticipationFactor(0.2f);

		m3 = new Flat(50, "m3");
		m3.addOccupant(new Occupant("Magdalena Lis"));
		m3.setParticipationFactor(0.1f);

		m4 = new Flat(32, "m4");
		m4.setParticipationFactor(0.2f);

		m5 = new Office(62, "m5");
		m5.addOccupant(new Occupant(""));
		m5.addOccupant(new Occupant(""));
		m5.setParticipationFactor(0.2f);

		m6 = new Office(57, "m6");
		m6.addOccupant(new Occupant(""));
		m6.setParticipationFactor(0.1f);

		m7 = new Flat(31, "m7", Ownership.OWN);
		m7.setParticipationFactor(0);

		m8 = new Flat(70, "m8", Ownership.OWN);
		m8.setParticipationFactor(0);

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

			Map<BillableService, Map<String, Quotation>> quotations = new HashMap<BillableService, Map<String, Quotation>>();
			quotations
					.put(BillableService.CO, new HashMap<String, Quotation>());
			quotations.put(BillableService.WODA,
					new HashMap<String, Quotation>());
			quotations
					.put(BillableService.EE, new HashMap<String, Quotation>());
			quotations.put(BillableService.GAZ,
					new HashMap<String, Quotation>());
			quotations.put(BillableService.INTERNET,
					new HashMap<String, Quotation>());
			quotations.put(BillableService.PODGRZANIE,
					new HashMap<String, Quotation>());
			quotations.put(BillableService.SMIECI,
					new HashMap<String, Quotation>());
			quotations.put(BillableService.SCIEKI,
					new HashMap<String, Quotation>());
			loc.setQuotations(quotations);
		}
	}

	public void readSampleData() {
		List<String[]> myEntries;
		try {
			CSVReader reader = new CSVReader(new FileReader("data.csv"));
			myEntries = reader.readAll();
		} catch (Exception e) {
			return; // TODO fix this
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

		List<MeasurableService> houseServices = Arrays.asList(
				MeasurableService.GAZ, MeasurableService.EE,
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

		List<MeasurableService> locumServices = Arrays.asList(
				MeasurableService.CO, MeasurableService.ZW,
				MeasurableService.CW, MeasurableService.CCW,
				MeasurableService.GAZ, MeasurableService.EE);

		for (Locum loc : mainHouse.getLocums()) {
			Map<Calendar, Map<MeasurableService, Float>> measures = new HashMap<Calendar, Map<MeasurableService, Float>>();

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

		Map<BillableService, Quotation> quotations = new HashMap<BillableService, Quotation>();
		quotations.put(BillableService.INTERNET, new Quotation(20f));
		quotations.put(BillableService.SMIECI, new Quotation(27.39f));
		quotations.put(BillableService.CO, new Quotation(90f));
		quotations.put(BillableService.WODA, new Quotation(2.59f));
		quotations.put(BillableService.SCIEKI, new Quotation(19f));
		quotations.put(BillableService.EE, new Quotation(0.51f));
		quotations.put(BillableService.GAZ, new Quotation(2.35f));
		quotations.put(BillableService.PODGRZANIE, new Quotation(23.24f));

		for (Locum loc : mainHouse.getLocums()) {
			loc.addQuotationSet("pierwsze", quotations);
		}

		/*
		 * stawka = Quotation.new("Od 2.01.2010",{ :internet => 20, :smieci =>
		 * 27.39, :co => 90, :woda => 2.59, :scieki => { :flat => 19, :office =>
		 * 22, }, :ee => { :flat => 0.51, :office => 0.58, }, :gaz => 2.35,
		 * :podgrzanie => 23.24, })
		 */
	}

	public void run(PaymentCalculator calc) {
		view.startGUI();
	}

	public List<String> getLocums() {
		List<String> locumList = new LinkedList<String>();
		for (Locum loc : mainHouse.getLocums()) {
			locumList.add(loc.getName());
		}
		return locumList;
	}

	public List<String> getLocumOccupants(String locumName) {
		List<String> occupantsList = new LinkedList<String>();
		for (Locum loc : mainHouse.getLocums()) {
			if (loc.getName() == locumName) {
				for (Occupant occ : loc.getOccupants())
					occupantsList.add(occ.getName());
			}
		}
		return occupantsList;
	}

	public void putEvent(PROZEvent event) {
		Method m = eventDictionary.get(event.getClass());
		try{
		m.invoke(this);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void reactToEvent(){
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					view.displayUserMessage("Kotek");
				}
			});
		
	}
	
	public void displayLocumsForPayments(){
		final List<LocumMockup> locums = new ArrayList<LocumMockup>(); 
		for(Locum loc : mainHouse.getLocums()){
			locums.add(loc.getMockup());
		}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					((PaymentsView) view.paymentsView).displayLocums(locums);
				}
			});
		
	}
}
