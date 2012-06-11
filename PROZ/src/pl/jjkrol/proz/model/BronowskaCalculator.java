package pl.jjkrol.proz.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import pl.jjkrol.proz.model.NoSuchQuotationSet;
import org.apache.log4j.Logger;

/**
 * Responsible for calculating usage and payments for all services.
 * 
 * @author jjkrol
 */
public class BronowskaCalculator implements PaymentCalculator {

	/** The result. */
	private Result result;

	/** The logger. */
	static Logger logger = Logger.getLogger(BronowskaCalculator.class);

	/**
	 * Calculates all usage information.
	 * 
	 * @param building
	 *            the building
	 * @param loc
	 *            the loc
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param quotationName
	 *            the quotation name
	 * @return the result
	 * @throws NoSuchQuotationSet
	 *             the no such quotation set
	 * @throws NoSuchDate
	 *             the no such date
	 */
	public Result calculate(final Building building, final Locum loc,
			final Calendar from, final Calendar to, final String quotationName)
			throws NoSuchQuotationSet, NoSuchDate {
		result = new Result();
		result.setBuilding(building);
		result.setLocum(loc);
		result.setFrom(from);
		result.setTo(to);
		result.setQuotationName(quotationName);
		result.setResults(calculatePayment(building, loc, from, to,
				quotationName));
		result.setAdministrativeResults(calculateAdministrativePayment(
				building, loc, from, to, quotationName));
		return result;

	}

	/**
	 * Calculates payments for usage of single locum services.
	 * 
	 * @param house
	 *            the house
	 * @param loc
	 *            the loc
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param quotationName
	 *            the quotation name
	 * @return the map
	 * @throws NoSuchQuotationSet
	 *             the no such quotation set
	 * @throws NoSuchDate
	 *             the no such date
	 */
	private Map<BillableService, BigDecimal> calculatePayment(
			final Building house, final Locum loc, final Calendar start,
			final Calendar end, final String quotationName)
			throws NoSuchQuotationSet, NoSuchDate {

		int monthDiff = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		int yearDiff = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		// number of full months elapsed between dates
		int period = (yearDiff > 0) ? yearDiff * 12 + monthDiff : monthDiff;

		int occupantsNumber = loc.getOccupants().size();
		float heatFactor = house.getHeatFactor(start, end);

		try {
			Map<LocumService, Float> startMeasurement =
					loc.getMeasurement(start);
			Map<LocumService, Float> endMeasurement = loc.getMeasurement(end);
			result.setBillableMeasurementStart(calculateBillableMeasurement(startMeasurement));
			result.setBillableMeasurementEnd(calculateBillableMeasurement(endMeasurement));
			Map<BillableService, Float> billableUsage =
					calculateBillableUsage(period, occupantsNumber, heatFactor);
			List<Quotation> quotations = loc.getQuotationSet(quotationName);
			return calculatePayment(billableUsage, quotations);
		} catch (NoSuchQuotationSet e) {
			throw new NoSuchQuotationSet();
		} catch (NoSuchDate e) {
			throw new NoSuchDate();
		}
	}

	/**
	 * Calculate billable measurement.
	 * 
	 * @param measurements
	 *            the measurements
	 * @return the map
	 */
	private Map<BillableService, Float> calculateBillableMeasurement(
			final Map<LocumService, Float> measurements) {
		Map<BillableService, Float> billableMeasurements =
				new HashMap<BillableService, Float>();

		float cw = measurements.get(LocumService.CW);
		float zw = measurements.get(LocumService.ZW);
		float ccw = measurements.get(LocumService.CCW);
		billableMeasurements.put(BillableService.WODA, cw + zw - ccw);
		billableMeasurements.put(BillableService.PODGRZANIE, cw - ccw);
		billableMeasurements.put(BillableService.SCIEKI, cw + zw - ccw);
		billableMeasurements.put(BillableService.CO, measurements
				.get(LocumService.CO));
		billableMeasurements.put(BillableService.GAZ, measurements
				.get(LocumService.GAZ));
		billableMeasurements.put(BillableService.EE, measurements
				.get(LocumService.EE));

		return billableMeasurements;
	}

	/**
	 * Calculates payments for usage of administrative services.
	 * 
	 * @param house
	 *            the house
	 * @param loc
	 *            the loc
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param quotationName
	 *            the quotation name
	 * @return the map
	 * @throws NoSuchQuotationSet
	 *             the no such quotation set
	 * @throws NoSuchDate
	 *             the no such date
	 */
	private Map<BillableService, BigDecimal> calculateAdministrativePayment(
			final Building house, final Locum loc, final Calendar start,
			final Calendar end, final String quotationName)
			throws NoSuchQuotationSet, NoSuchDate {
		Map<BillableService, Float> administrativeUsage =
				getAdministrativeUsage(loc, start, end, house);
		try {
			List<Quotation> quotations = loc.getQuotationSet(quotationName);
			return calculatePayment(administrativeUsage, quotations);
		} catch (NoSuchQuotationSet e) {
			throw new NoSuchQuotationSet();
		}
	}

	/**
	 * Calculates all payments.
	 * 
	 * @param billableUsage
	 *            the billable usage
	 * @param quotations
	 *            the quotations
	 * @return Result object with all data concerning payment
	 */
	private Map<BillableService, BigDecimal> calculatePayment(
			final Map<BillableService, Float> billableUsage,
			final List<Quotation> quotations) {
		Map<BillableService, BigDecimal> payment =
				new HashMap<BillableService, BigDecimal>();
		for (BillableService serv : billableUsage.keySet()) {
			float locumUsage = billableUsage.get(serv);
			float quotation = 0;
			// TODO no such quotation?
			for (Quotation quot : quotations) {
				if (quot.getService().equals(serv))
					quotation = quot.getPrice();
			}
			BigDecimal value =
					new BigDecimal(locumUsage * quotation).setScale(2,
							RoundingMode.HALF_EVEN);
			payment.put(serv, value);

		}
		return payment;
	}

	/**
	 * Calculates usage of services.
	 * 
	 * @param period
	 *            the period
	 * @param size
	 *            the size
	 * @param heatFactor
	 *            the heat factor
	 * @return map of usages
	 */
	private Map<BillableService, Float> calculateBillableUsage(
			final int period, final int size, final float heatFactor) {
		Map<BillableService, Float> billableUsage =
				new HashMap<BillableService, Float>();

		float woda =
				result.getBillableMeasurementEnd().get(BillableService.WODA)
						- result.getBillableMeasurementStart().get(
								BillableService.WODA);
		billableUsage.put(BillableService.WODA, woda);

		float podgrzanie =
				result.getBillableMeasurementEnd().get(
						BillableService.PODGRZANIE)
						- result.getBillableMeasurementStart().get(
								BillableService.PODGRZANIE);
		billableUsage.put(BillableService.PODGRZANIE, podgrzanie);
		float scieki =
				result.getBillableMeasurementEnd().get(BillableService.WODA)
						- result.getBillableMeasurementStart().get(
								BillableService.WODA);
		billableUsage.put(BillableService.SCIEKI, scieki);
		float co =
				result.getBillableMeasurementEnd().get(BillableService.CO)
						- result.getBillableMeasurementStart().get(
								BillableService.CO);
		logger.debug(result.getBillableMeasurementEnd().get(BillableService.CO)
				+ " "
				+ result.getBillableMeasurementStart().get(BillableService.CO)
				+ " " + heatFactor);
		billableUsage.put(BillableService.CO, co * heatFactor);
		float gaz =
				result.getBillableMeasurementEnd().get(BillableService.GAZ)
						- result.getBillableMeasurementStart().get(
								BillableService.GAZ);
		billableUsage.put(BillableService.GAZ, gaz);
		float ee =
				result.getBillableMeasurementEnd().get(BillableService.EE)
						- result.getBillableMeasurementStart().get(
								BillableService.EE);
		billableUsage.put(BillableService.EE, ee);

		billableUsage.put(BillableService.SMIECI, (float) period * size);
		billableUsage.put(BillableService.INTERNET, (float) period);

		return billableUsage;
	}

	/**
	 * Calculates usage of administrative services.
	 * 
	 * @param loc
	 *            the loc
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param house
	 *            the house
	 * @return the administrative usage
	 * @throws NoSuchDate
	 *             the no such date
	 */
	private Map<BillableService, Float> getAdministrativeUsage(final Locum loc,
			final Calendar start, final Calendar end, final Building house)
			throws NoSuchDate {

		Map<BuildingService, Float> houseUsage = house.getUsage(start, end);
		float heatFactor = house.getHeatFactor(start, end);
		float partFact = loc.getParticipationFactor();

		Map<BillableService, Float> retMap =
				new HashMap<BillableService, Float>();

		retMap.put(BillableService.CO, houseUsage.get(BuildingService.CO_ADM)
				* heatFactor * partFact);
		retMap.put(BillableService.WODA, houseUsage
				.get(BuildingService.POLEWACZKI)
				* partFact);
		retMap.put(BillableService.EE, houseUsage.get(BuildingService.EE)
				* partFact);

		return retMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pl.jjkrol.proz.model.PaymentCalculator#getLastResult()
	 */
	@Override
	public Result getLastResult() {
		return result;
	}

}
