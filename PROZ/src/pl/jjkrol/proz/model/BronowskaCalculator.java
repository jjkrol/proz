package pl.jjkrol.proz.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import pl.jjkrol.proz.model.NoSuchQuotationSet;
import org.apache.log4j.Logger;

/**
 * Responsible for calculating usage and payments for all services
 * 
 * @author jjkrol
 */
public class BronowskaCalculator implements PaymentCalculator {

	/**
	 */
	private Result result;
	static Logger logger = Logger.getLogger(BronowskaCalculator.class);

	/**
	 * Calculates all usage information
	 * 
	 * @throws NoSuchQuotationSet
	 * @throws NoSuchDate 
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
	 * Calculates payments for usage of single locum services
	 * @throws NoSuchDate 
	 */
	private Map<BillableService, BigDecimal> calculatePayment(Building house,
			Locum loc, Calendar start, Calendar end, String quotationName)
			throws NoSuchQuotationSet, NoSuchDate {

		int monthDiff = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		int yearDiff = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		// number of full months elapsed between dates
		int period = (yearDiff > 0) ? yearDiff * 12 + monthDiff : monthDiff;

		int occupantsNumber = loc.getOccupants().size();
		float heatFactor = house.getHeatFactor(start, end);

		try {
		Map<MeasurableService, Float> startMeasurement =
				loc.getMeasurement(start);
		Map<MeasurableService, Float> endMeasurement = loc.getMeasurement(end);
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

	private Map<BillableService, Float> calculateBillableMeasurement(
			Map<MeasurableService, Float> measurements) {
		Map<BillableService, Float> billableMeasurements =
				new HashMap<BillableService, Float>();

		float cw = measurements.get(MeasurableService.CW);
		float zw = measurements.get(MeasurableService.ZW);
		float ccw = measurements.get(MeasurableService.CCW);
		billableMeasurements.put(BillableService.WODA, cw + zw - ccw);
		billableMeasurements.put(BillableService.PODGRZANIE, cw - ccw);
		billableMeasurements.put(BillableService.SCIEKI, cw + zw - ccw);
		billableMeasurements.put(BillableService.CO, measurements
				.get(MeasurableService.CO));
		billableMeasurements.put(BillableService.GAZ, measurements
				.get(MeasurableService.GAZ));
		billableMeasurements.put(BillableService.EE, measurements
				.get(MeasurableService.EE));

		return billableMeasurements;
	}

	/**
	 * Calculates payments for usage of administrative services
	 * @throws NoSuchDate 
	 */
	private Map<BillableService, BigDecimal> calculateAdministrativePayment(
			Building house, Locum loc, Calendar start, Calendar end,
			String quotationName) throws NoSuchQuotationSet, NoSuchDate {
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
	 * Calculates all payments
	 * 
	 * @param billableUsage
	 * @param quotations
	 * @return Result object with all data concerning payment
	 */
	private Map<BillableService, BigDecimal> calculatePayment(
			Map<BillableService, Float> billableUsage,
			List<Quotation> quotations) {
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
	 * Calculates usage of services
	 * 
	 * @param usage
	 * @param period
	 * @param size
	 * @param heatFactor
	 * @return map of usages
	 */
	private Map<BillableService, Float> calculateBillableUsage(int period,
			int size, float heatFactor) {
		Map<BillableService, Float> billableUsage =
				new HashMap<BillableService, Float>();

		float woda =
				result.getBillableMeasurementEnd().get(BillableService.WODA)
						- result.getBillableMeasurementStart().get(
								BillableService.WODA);
		billableUsage.put(BillableService.WODA, woda); 
		
		float podgrzanie =
				result.getBillableMeasurementEnd().get(BillableService.PODGRZANIE)
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
		logger.debug(result.getBillableMeasurementEnd().get(BillableService.CO)+" "+
								result.getBillableMeasurementStart().get(BillableService.CO)+" "+heatFactor);
		billableUsage.put(BillableService.CO, co * heatFactor);
		float gaz =
				result.getBillableMeasurementEnd().get(BillableService.GAZ)
						- result.getBillableMeasurementStart().get(
								BillableService.GAZ);
		billableUsage
				.put(BillableService.GAZ, gaz);
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
	 * Calculates usage of administrative services
	 * 
	 * @param loc
	 * @param start
	 * @param end
	 * @param house
	 * @return
	 * @throws NoSuchDate 
	 */
	private Map<BillableService, Float> getAdministrativeUsage(Locum loc,
			Calendar start, Calendar end, Building house) throws NoSuchDate {

		Map<MeasurableService, Float> houseUsage = house.getUsage(start, end);
		float heatFactor = house.getHeatFactor(start, end);
		float partFact = loc.getParticipationFactor();

		Map<BillableService, Float> retMap =
				new HashMap<BillableService, Float>();

		retMap.put(BillableService.CO, houseUsage.get(MeasurableService.CO_ADM)
				* heatFactor * partFact);
		retMap.put(BillableService.WODA, houseUsage
				.get(MeasurableService.POLEWACZKI)
				* partFact);
		retMap.put(BillableService.EE, houseUsage.get(MeasurableService.EE)
				* partFact);

		return retMap;
	}

}
