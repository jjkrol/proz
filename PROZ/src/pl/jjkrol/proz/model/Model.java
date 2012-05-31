package pl.jjkrol.proz.model;

import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import au.com.bytecode.opencsv.CSVReader;
import pl.jjkrol.proz.mockups.OccupantMockup;

public class Model {
	private Building building;
	private Locum m01, m1, m2, m3, m4, m5, m6, m7, m8;
	private OccupantsRegister occupantsRegister = OccupantsRegister
			.getInstance();
	static Logger logger = Logger.getLogger(Model.class);

	/**
	 * Initializes database connection and reads all data or reads data from
	 * file
	 */
	public void initializeData() {
		createHouse();
		readSampleData();

	}

	public Building getMainHouse() {
		return building;
	}

	private void initDatabase() {
		/*
		 * Hibernate test HibernateUtil.configureSessionFactory(); Session
		 * session = HibernateUtil.getSessionFactory().openSession();
		 * Transaction transaction = null; try { transaction =
		 * session.beginTransaction(); List<Locum> locums =
		 * session.createQuery("from Locum").list(); for (Locum loc : locums ) {
		 * System.out.println(loc.getName()); } transaction.commit(); } catch
		 * (HibernateException e) { logger.debug(e.getMessage());
		 * e.printStackTrace(); transaction.rollback(); e.printStackTrace(); }
		 * finally { session.close(); }
		 */
	}

	public ResultMockup calculateResults(final PaymentCalculator calculator,
			final String locumName, final Calendar from, final Calendar to,
			final String quotationName) throws NoSuchLocum, NoSuchQuotationSet {

		Locum loc = building.getLocumByName(locumName);
		Result res =
				calculator.calculate(building, loc, from, to, quotationName);
		return res.getMockup();

	}

	/**
	 * reads data from specific csv fil
	 */
	private void readSampleData() {
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
				building.addMeasure(dates.get(i), currentService, new Float(
						value));
				i++;
			}
		}

		List<MeasurableService> locumServices =
				Arrays.asList(MeasurableService.CO, MeasurableService.ZW,
						MeasurableService.CW, MeasurableService.CCW,
						MeasurableService.GAZ, MeasurableService.EE);

		List<BillableService> enabledServices =
				new ArrayList<BillableService>();
		for (BillableService serv : BillableService.class.getEnumConstants()) {
			enabledServices.add(serv);
		}

		for (Locum loc : building.getLocums()) {
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

		for (Locum loc : building.getLocums()) {
			loc.addQuotationSet("pierwsze", quotations);
			loc.setEnabledServices(new ArrayList<BillableService>(
					enabledServices));
		}
		enabledServices = m1.getEnabledServices();
		enabledServices.remove(BillableService.SMIECI);
	}

	/**
	 * creates house and locums, inserts occupants
	 */
	private void createHouse() {
		Map<MeasurableService, Counter> counters =
				new HashMap<MeasurableService, Counter>();
		building = new Building("dom1", "Bronowska 52", counters);
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

		building.addLocum(m01);
		building.addLocum(m1);
		building.addLocum(m2);
		building.addLocum(m3);
		building.addLocum(m4);
		building.addLocum(m5);
		building.addLocum(m6);
		building.addLocum(m7);
		building.addLocum(m8);

		for (Locum loc : building.getLocums()) {
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

	public List<LocumMockup> getLocumsMockups() {
		List<LocumMockup> list = new ArrayList<LocumMockup>();
		for (Locum loc : building.getLocums()) {
			list.add(loc.getMockup());
		}
		return list;
	}

	public List<MeasurementMockup> getLocumMeasurementMockups(
			final String locumName) throws NoSuchLocum {
		Locum loc = building.getLocumByName(locumName);
		return loc.getMeasurementsMockups();
	}

	public Map<String, List<QuotationMockup>> getLocumQuotationMockups(
			final String locumName) throws NoSuchLocum {

		Locum loc = building.getLocumByName(locumName);
		return loc.getQuotationsMockups();
	}

	public void saveOccupant(int id, OccupantMockup moc) {
		occupantsRegister.editOccupant(id, moc);
	}

	public void deleteOccupantData(int id) {
		occupantsRegister.deleteOccupant(id);
	}

	public void addOccupantData(OccupantMockup moc) {
		occupantsRegister.createOccupant(moc);
	}

	public OccupantMockup getOccupantMockup(OccupantMockup moc) {
		return occupantsRegister.findOccupant(moc).getMockup();
	}

	public List<OccupantMockup> getOccupantsMockups() {
		List<OccupantMockup> occMocks = new ArrayList<OccupantMockup>();
		for (OccupantMockup occ : occupantsRegister.getOccupantsMockups()) {
			occMocks.add(occ);
		}
		return occMocks;
	}
}
