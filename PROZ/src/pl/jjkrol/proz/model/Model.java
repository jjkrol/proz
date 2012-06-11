package pl.jjkrol.proz.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import pl.jjkrol.proz.dao.BuildingDAO;
import pl.jjkrol.proz.dao.LocumDAO;
import pl.jjkrol.proz.dao.OccupantDAO;
import pl.jjkrol.proz.mockups.BuildingMeasurementMockup;
import pl.jjkrol.proz.mockups.InvoiceData;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.OccupantMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import pl.jjkrol.proz.mockups.UsageTableData;
import pl.jjkrol.proz.mockups.UsageTableMockup;

/**
 * The Class Model.
 * 
 * @author jjkrol
 */
public class Model {

	/** logger. */
	static Logger logger = Logger.getLogger("model");

	/** A building, for which the application is used. */
	private Building building;

	/** The calculator. */
	final private PaymentCalculator calculator;

	/**
	 * initializes database.
	 * 
	 * @param calculator
	 *            the calculator
	 */
	public Model(final PaymentCalculator calculator) {
		this.calculator = calculator;
		HibernateUtil.beginTransaction();
		logger.debug("init model");
	}

	/**
	 * Adds the building measurement data.
	 * 
	 * @param mea
	 *            the mea
	 */
	public void addBuildingMeasurementData(final BuildingMeasurementMockup mea) {
		building.addMeasures(mea.getDate(), mea.getValues());
	}

	/**
	 * Adds the measurement data.
	 * 
	 * @param loc
	 *            the loc
	 * @param mea
	 *            the mea
	 */
	public void addMeasurementData(final LocumMockup loc,
			final MeasurementMockup mea) {
		LocumDAO locumDAO = new LocumDAO();
		Locum locum = locumDAO.getLocumByName(loc.getName());
		locum.addMeasures(mea.getDate(), mea.getValues());
	}

	/**
	 * Adds the occupant data.
	 * 
	 * @param moc
	 *            the moc
	 */
	public void addOccupantData(final OccupantMockup moc) {
		OccupantDAO occupantDAO = new OccupantDAO();
		Occupant occupant = new Occupant(moc);
		occupantDAO.saveOrUpdate(occupant);
	}

	/**
	 * returns calculated results.
	 * 
	 * @param locumName
	 *            the locum name
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param quotationName
	 *            the quotation name
	 * @return the result mockup
	 * @throws NoSuchLocum
	 *             the no such locum
	 * @throws NoSuchQuotationSet
	 *             the no such quotation set
	 * @throws NoSuchDate
	 *             the no such date
	 */
	public ResultMockup calculateResults(final String locumName,
			final Calendar from, final Calendar to, final String quotationName)
			throws NoSuchLocum, NoSuchQuotationSet, NoSuchDate {

		Locum loc = building.getLocumByName(locumName);
		Result res =
				calculator.calculate(building, loc, from, to, quotationName);
		return res.getMockup();

	}

	/**
	 * Close database.
	 */
	public void closeDatabase() {
		logger.debug("Closing database");
		HibernateUtil.commitTransaction();
		HibernateUtil.closeSession();
		logger.debug("Database closed");
	}

	/**
	 * Delete building measurements.
	 * 
	 * @param mea
	 *            the mea
	 */
	public void deleteBuildingMeasurements(final BuildingMeasurementMockup mea) {
		building.removeMeasures(mea.getDate());
	}

	/**
	 * Delete measurements.
	 * 
	 * @param loc
	 *            the loc
	 * @param date
	 *            the date
	 */
	public void deleteMeasurements(final LocumMockup loc, final Calendar date) {
		LocumDAO locumDAO = new LocumDAO();
		Locum locum = locumDAO.getLocumByName(loc.getName());
		locum.removeMeasures(date);
	}

	/**
	 * Delete occupant data.
	 * 
	 * @param id
	 *            the id
	 * @throws NoSuchOccupant
	 *             the no such occupant
	 */
	public void deleteOccupantData(final int id) throws NoSuchOccupant {
		OccupantDAO occupantDAO = new OccupantDAO();
		occupantDAO.deleteById(id);
	}

	/**
	 * Gets the building measurement mockups.
	 * 
	 * @return the building measurement mockups
	 */
	public List<BuildingMeasurementMockup> getBuildingMeasurementMockups() {
		return building.getMeasurementMockups();
	}

	/**
	 * Gets the invoice data.
	 * 
	 * @return the invoice data
	 */
	public InvoiceData getInvoiceData() {
		return null;
	}

	/**
	 * Gets the locum measurement mockups.
	 * 
	 * @param locumName
	 *            the locum name
	 * @return measurement mockups of the specified locum
	 * @throws NoSuchLocum
	 *             the no such locum
	 */
	public List<MeasurementMockup> getLocumMeasurementMockups(
			final String locumName) throws NoSuchLocum {
		Locum loc = building.getLocumByName(locumName);
		return loc.getMeasurementsMockups();
	}

	/**
	 * Gets the locum quotation mockups.
	 * 
	 * @param locumName
	 *            the locum name
	 * @return quotation mockups of the specified locum
	 * @throws NoSuchLocum
	 *             the no such locum
	 */
	public Map<String, List<QuotationMockup>> getLocumQuotationMockups(
			final String locumName) throws NoSuchLocum {

		Locum loc = building.getLocumByName(locumName);
		return loc.getQuotationsMockups();
	}

	/**
	 * Gets the locums mockups.
	 * 
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
	 * TODO delete this.
	 * 
	 * @return the main house
	 */
	public Building getMainHouse() {
		return building;
	}

	/**
	 * Gets the occupant mockup.
	 * 
	 * @param moc
	 *            the moc
	 * @return the occupant mockup
	 * @throws NoSuchOccupant
	 *             the no such occupant
	 */
	public OccupantMockup getOccupantMockup(final OccupantMockup moc)
			throws NoSuchOccupant {
		OccupantDAO occupantDAO = new OccupantDAO();
		Occupant occupant = occupantDAO.getOccupantById(moc.getId());
		return occupant.getMockup();
	}

	/**
	 * Gets the occupants mockups.
	 * 
	 * @return the occupants mockups
	 */
	public List<OccupantMockup> getOccupantsMockups() {
		OccupantDAO occupantDAO = new OccupantDAO();
		List<Occupant> occupants = occupantDAO.getAllOccupants();

		List<OccupantMockup> occMocks = new ArrayList<OccupantMockup>();

		for (Occupant occ : occupants) {
			occMocks.add(occ.getMockup());
		}

		return occMocks;
	}

	/**
	 * Gets the usage table data.
	 * 
	 * @return the usage table data
	 */
	public UsageTableData getUsageTableData() {
		Result result = calculator.getLastResult();
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getSession();
		// Query query = session.createQuery("");
		// List list = query.
		UsageTableData usageTableData =
				(UsageTableData) session
						.createQuery(
								"select new pl.jjkrol.proz.mockups.UsageTableData(loc.name, loc.billingPerson.name, loc.advancement) from Locum loc WHERE loc.name='m1'")
						.uniqueResult();
		usageTableData.setFrom(result.getFrom());
		usageTableData.setTo(result.getTo());
		return usageTableData;
	}

	/**
	 * Gets the usage table mockup.
	 * 
	 * @param usageTableData
	 *            the usage table data
	 * @return the usage table mockup
	 */
	public UsageTableMockup getUsageTableMockup(
			final UsageTableData usageTableData) {
		Result result = calculator.getLastResult();
		UsageTable usageTable = result.getUsageTable(usageTableData);
		return usageTable.getMockup();
	}

	/**
	 * Initializes database connection and reads all data or reads data from
	 * file.
	 */
	public void initializeData() {
		createHouse();
		readSampleData();

	}

	/**
	 * Save building measurement data.
	 * 
	 * @param mea
	 *            the mea
	 */
	public void saveBuildingMeasurementData(final BuildingMeasurementMockup mea) {
		building.setMeasures(mea.getDate(), mea.getValues());
	}

	/**
	 * Save measurement data.
	 * 
	 * @param loc
	 *            the loc
	 * @param mea
	 *            the mea
	 */
	public void saveMeasurementData(final LocumMockup loc,
			final MeasurementMockup mea) {
		LocumDAO locumDAO = new LocumDAO();
		Locum locum = locumDAO.getLocumByName(loc.getName());
		locum.setMeasures(mea.getDate(), mea.getValues());
	}

	/**
	 * saves occupant's data TODO get rid of id.
	 * 
	 * @param id
	 *            the id
	 * @param moc
	 *            the moc
	 * @throws NoSuchOccupant
	 *             the no such occupant
	 */
	public void saveOccupant(final int id, final OccupantMockup moc)
			throws NoSuchOccupant {
		OccupantDAO occupantDAO = new OccupantDAO();
		Occupant occ = occupantDAO.getOccupantById(id);
		occ.setAttributes(moc);
	}

	/**
	 * TODO transform it to read most from database creates house and locums,
	 * inserts occupants.
	 */
	private void createHouse() {
		Map<LocumService, Counter> counters =
				new HashMap<LocumService, Counter>();
		BuildingDAO buildingDAO = new BuildingDAO();
		building = buildingDAO.getBuildingById(1);

		for (BuildingService serv : building.getCounters().keySet()) {
			logger.debug("Licznik " + serv + " "
					+ building.getCounters().get(serv).getUnit());
		}

		// loadLocums();

		/*
		 * for (Locum loc : building.getLocums()) { counters = new
		 * HashMap<MeasurableService, Counter>(); MeasurableService[] services =
		 * { MeasurableService.CO, MeasurableService.ZW, MeasurableService.CW,
		 * MeasurableService.CCW, MeasurableService.GAZ };
		 * 
		 * for (MeasurableService serv : services) { CounterDAO counterDAO = new
		 * CounterDAO(); Counter count = new Counter("m3");
		 * counterDAO.saveOrUpdate(count); counters.put(serv, count); }
		 * 
		 * CounterDAO counterDAO = new CounterDAO(); Counter count = new
		 * Counter("kWh"); counterDAO.saveOrUpdate(count);
		 * counters.put(MeasurableService.EE, count);
		 * 
		 * loc.setCounters(counters); }
		 */
	}

	/**
	 * TODO delete this reads data from specific csv file.
	 */
	private void readSampleData() {

		// TODO transform this to use for importing data from csv
		/*
		 * List<String[]> myEntries; try { CSVReader reader = new CSVReader(new
		 * FileReader("data/data.csv")); myEntries = reader.readAll(); } catch
		 * (Exception e) { logger.warn("Data file not found!"); return; }
		 * 
		 * List<MeasurableService> houseServices =
		 * Arrays.asList(MeasurableService.GAZ, MeasurableService.EE,
		 * MeasurableService.EE_ADM, MeasurableService.CIEPLO,
		 * MeasurableService.CO_ADM, MeasurableService.WODA_GL,
		 * MeasurableService.CW, MeasurableService.POLEWACZKI);
		 * 
		 * List<MeasurableService> locumServices =
		 * Arrays.asList(MeasurableService.CO, MeasurableService.ZW,
		 * MeasurableService.CW, MeasurableService.CCW, MeasurableService.GAZ,
		 * MeasurableService.EE);
		 * 
		 * 
		 * 
		 * int lineIndex = 0; DateFormat df = new
		 * SimpleDateFormat("dd.MM.yyyy");
		 * 
		 * List<Calendar> dates = new ArrayList<Calendar>(); String[] line =
		 * myEntries.get(lineIndex++); for (String value : line) { Date date;
		 * try { date = df.parse(value); } catch (Exception e) { return; // TODO
		 * fix this } Calendar cal = new GregorianCalendar(); cal.setTime(date);
		 * dates.add(cal); }
		 * 
		 * for (MeasurableService currentService : houseServices) { line =
		 * myEntries.get(lineIndex++);
		 * 
		 * int i = 0; for (String value : line) {
		 * building.addMeasure(dates.get(i), currentService, new Float( value));
		 * i++; } } for (Locum loc : building.getLocums()) { Map<Calendar,
		 * Map<MeasurableService, Float>> measures = new HashMap<Calendar,
		 * Map<MeasurableService, Float>>();
		 * 
		 * for (MeasurableService currentService : locumServices) { line =
		 * myEntries.get(lineIndex++);
		 * 
		 * int i = 0; for (String value : line) { if (value.isEmpty()) value =
		 * "0"; Calendar date = dates.get(i); if (!measures.containsKey(date))
		 * measures.put(date, new HashMap<MeasurableService, Float>());
		 * measures.get(date).put(currentService, new Float(value)); i++; } }
		 * for (Calendar date : measures.keySet()) { loc.addMeasures(date,
		 * measures.get(date)); } }
		 */

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
}
