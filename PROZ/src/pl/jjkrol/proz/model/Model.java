package pl.jjkrol.proz.model;

import java.awt.TrayIcon.MessageType;
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
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.jjkrol.proz.dao.BuildingDAO;
import pl.jjkrol.proz.dao.CounterDAO;
import pl.jjkrol.proz.dao.LocumDAO;
import pl.jjkrol.proz.dao.OccupantDAO;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import au.com.bytecode.opencsv.CSVReader;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.model.Occupant.Billing;

/**
 * @author jjkrol
 */
public class Model {
	/** A building, for which the application is used */
	private Building building;
	/** Temporary map for fetching locums of the building */
	private Map<String, Locum> buildingLocums = new HashMap<String, Locum>();
	/** logger */
	static Logger logger = Logger.getLogger("model");

	/**
	 * initializes database
	 */
	public Model() {
		HibernateUtil.beginTransaction();
		logger.debug("init model");
	}

	/**
	 * Initializes database connection and reads all data or reads data from
	 * file
	 */
	public void initializeData() {
		createHouse();
		readSampleData();

	}

	/**
	 * TODO delete this
	 * 
	 * @return
	 */
	public Building getMainHouse() {
		return building;
	}

	/**
	 * returns calculated results
	 * 
	 * @param calculator
	 * @param locumName
	 * @param from
	 * @param to
	 * @param quotationName
	 * @return
	 * @throws NoSuchLocum
	 * @throws NoSuchQuotationSet
	 * @throws NoSuchDate 
	 */
	public ResultMockup calculateResults(final PaymentCalculator calculator,
			final String locumName, final Calendar from, final Calendar to,
			final String quotationName) throws NoSuchLocum, NoSuchQuotationSet, NoSuchDate {

		Locum loc = building.getLocumByName(locumName);
		Result res =
				calculator.calculate(building, loc, from, to, quotationName);
		return res.getMockup();

	}

	/**
	 * TODO delete this
	 * reads data from specific csv file
	 */
	private void readSampleData() {
		
		//TODO transform this to use for importing data from csv
		/*
		List<String[]> myEntries;
		try {
			CSVReader reader = new CSVReader(new FileReader("data/data.csv"));
			myEntries = reader.readAll();
		} catch (Exception e) {
			logger.warn("Data file not found!");
			return;
		}

		List<MeasurableService> houseServices =
				Arrays.asList(MeasurableService.GAZ, MeasurableService.EE,
						MeasurableService.EE_ADM, MeasurableService.CIEPLO,
						MeasurableService.CO_ADM, MeasurableService.WODA_GL,
						MeasurableService.CW, MeasurableService.POLEWACZKI);

		List<MeasurableService> locumServices =
				Arrays.asList(MeasurableService.CO, MeasurableService.ZW,
						MeasurableService.CW, MeasurableService.CCW,
						MeasurableService.GAZ, MeasurableService.EE);


		
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
		
		for (MeasurableService currentService : houseServices) {
			line = myEntries.get(lineIndex++);

			int i = 0;
			for (String value : line) {
				building.addMeasure(dates.get(i), currentService, new Float(
						value));
				i++;
			}
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
		}*/	
		
		List<BillableService> enabledServices =
				new ArrayList<BillableService>();
		for (BillableService serv : BillableService.class.getEnumConstants()) {
			enabledServices.add(serv);
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
	}

	/**
	 * TODO transform it to read most from database creates house and locums,
	 * inserts occupants
	 */
	private void createHouse() {
		Map<MeasurableService, Counter> counters =
				new HashMap<MeasurableService, Counter>();
		BuildingDAO buildingDAO = new BuildingDAO();
		building = buildingDAO.getBuildingById(1);

		for (MeasurableService serv : building.getCounters().keySet()) {
			logger.debug("Licznik " + serv + " "
					+ building.getCounters().get(serv).getUnit());
		}

		// loadLocums();

		/*for (Locum loc : building.getLocums()) {
			counters = new HashMap<MeasurableService, Counter>();
			MeasurableService[] services =
					{ MeasurableService.CO, MeasurableService.ZW,
							MeasurableService.CW, MeasurableService.CCW,
							MeasurableService.GAZ };

			for (MeasurableService serv : services) {
				CounterDAO counterDAO = new CounterDAO();
				Counter count = new Counter("m3");
				counterDAO.saveOrUpdate(count);
				counters.put(serv, count);
			}

			CounterDAO counterDAO = new CounterDAO();
			Counter count = new Counter("kWh");
			counterDAO.saveOrUpdate(count);
			counters.put(MeasurableService.EE, count);

			loc.setCounters(counters);
		}*/
	}

	/**
	 * @return mockups of all locums
	 */
	public List<LocumMockup> getLocumsMockups() {
		List<LocumMockup> list = new ArrayList<LocumMockup>();
		for (Locum loc : building.getLocums()) {
			list.add(loc.getMockup());
		}
		return list;
	}

	/**
	 * 
	 * @param locumName
	 * @return measurement mockups of the specified locum
	 * @throws NoSuchLocum
	 */
	public List<MeasurementMockup> getLocumMeasurementMockups(
			final String locumName) throws NoSuchLocum {
		Locum loc = building.getLocumByName(locumName);
		return loc.getMeasurementsMockups();
	}

	/**
	 * @param locumName
	 * @return quotation mockups of the specified locum
	 * @throws NoSuchLocum
	 */
	public Map<String, List<QuotationMockup>> getLocumQuotationMockups(
			final String locumName) throws NoSuchLocum {

		Locum loc = building.getLocumByName(locumName);
		return loc.getQuotationsMockups();
	}

	/**
	 * saves occupant's data TODO get rid of id
	 * 
	 * @param id
	 * @param moc
	 * @throws NoSuchOccupant
	 */
	public void saveOccupant(int id, OccupantMockup moc) throws NoSuchOccupant {
		OccupantDAO occupantDAO = new OccupantDAO();
		Occupant occ = occupantDAO.getOccupantById(id);
		occ.setAttributes(moc);
	}

	public void deleteOccupantData(int id) throws NoSuchOccupant {
		OccupantDAO occupantDAO = new OccupantDAO();
		occupantDAO.deleteById(id);
	}

	public void addOccupantData(OccupantMockup moc) {
		OccupantDAO occupantDAO = new OccupantDAO();
		Occupant occupant = new Occupant(moc);
		occupantDAO.saveOrUpdate(occupant);
	}

	public OccupantMockup getOccupantMockup(OccupantMockup moc)
			throws NoSuchOccupant {
		OccupantDAO occupantDAO = new OccupantDAO();
		Occupant occupant = occupantDAO.getOccupantById(moc.getId());
		return occupant.getMockup();
	}

	public List<OccupantMockup> getOccupantsMockups() {
		OccupantDAO occupantDAO = new OccupantDAO();
		List<Occupant> occupants = occupantDAO.getAllOccupants();

		List<OccupantMockup> occMocks = new ArrayList<OccupantMockup>();

		for (Occupant occ : occupants) {
			occMocks.add(occ.getMockup());
		}

		return occMocks;
	}

	public void closeDatabase() {
		logger.debug("Closing database");
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();
		logger.debug("Database closed");
	}

	public void deleteMeasurements(LocumMockup loc, Calendar date) {
		LocumDAO locumDAO = new LocumDAO();
		Locum locum = locumDAO.getLocumByName(loc.getName());
		locum.removeMeasures(date);
	}

	public void addMeasurementData(LocumMockup loc, MeasurementMockup mea) {
		LocumDAO locumDAO = new LocumDAO();
		Locum locum = locumDAO.getLocumByName(loc.getName());
		locum.addMeasures(mea.getDate(), mea.getValues());	}

	public void saveMeasurementData(LocumMockup loc, MeasurementMockup mea) {
		LocumDAO locumDAO = new LocumDAO();
		Locum locum = locumDAO.getLocumByName(loc.getName());
		locum.setMeasures(mea.getDate(), mea.getValues());	}
}
