package pl.jjkrol.proz.model;

import java.util.*;
import pl.jjkrol.proz.model.NoSuchQuotationSet;
import org.apache.log4j.Logger;

/**
 * Responsible for calculating usage and payments for all services
 * 
 * @author jjkrol
 * 
 */
public class BronowskaCalculator implements PaymentCalculator {

	static Logger logger = Logger.getLogger(BronowskaCalculator.class);

	/**
	 * Calculates all usage information
	 * 
	 * @throws NoSuchQuotationSet
	 */
	public Result calculate(final Building building, final Locum loc,
			final Calendar from, final Calendar to, final String quotationName)
			throws NoSuchQuotationSet {
		Result res = new Result();
		res.setBuilding(building);
		res.setLocum(loc);
		res.setFrom(from);
		res.setTo(to);
		res.setQuotationName(quotationName);
		res.setResults(calculatePayment(building, loc, from, to, quotationName));
		res.setAdministrativeResults(calculateAdministrativePayment(building,
				loc, from, to, quotationName));
		return res;

	}

	/**
	 * Calculates payments for usage of single locum services
	 */
	public Map<BillableService, Float> calculatePayment(Building house,
			Locum loc, Calendar start, Calendar end, String quotationName)
			throws NoSuchQuotationSet {

		int monthDiff = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
		int yearDiff = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
		// number of full months elapsed between dates
		int period = (yearDiff > 0) ? yearDiff * 12 + monthDiff : monthDiff;

		int occupantsNumber = loc.getOccupants().size();
		float heatFactor = house.getHeatFactor(start, end);

		Map<MeasurableService, Float> usage = loc.getUsage(start, end);
		Map<BillableService, Float> billableUsage =
				calculateBillableUsage(usage, period, occupantsNumber,
						heatFactor);
		try {
			List<Quotation> quotations = loc.getQuotationSet(quotationName);
			return calculatePayment(billableUsage, quotations);
		} catch (NoSuchQuotationSet e) {
			throw new NoSuchQuotationSet();
		}
	}

	/**
	 * Calculates payments for usage of administrative services
	 */
	public Map<BillableService, Float> calculateAdministrativePayment(
			Building house, Locum loc, Calendar start, Calendar end,
			String quotationName) throws NoSuchQuotationSet {
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
	 * @param billableUsage
	 * @param quotations
	 * @return Result object with all data concerning payment
	 */
	private Map<BillableService, Float> calculatePayment(
			Map<BillableService, Float> billableUsage,
			List<Quotation> quotations) {
		Map<BillableService, Float> payment =
				new HashMap<BillableService, Float>();
		for (BillableService serv : billableUsage.keySet()) {
			float locumUsage = billableUsage.get(serv);
			float quotation = 0;
			// TODO no such quotation?
			for (Quotation quot : quotations) {
				if (quot.getService().equals(serv))
					quotation = quot.getPrice();
			}
			payment.put(serv, locumUsage * quotation);

		}
		return payment;
	}

	/**
	 * Calculates usage of services
	 * @param usage
	 * @param period
	 * @param size
	 * @param heatFactor
	 * @return map of usages
	 */
	private Map<BillableService, Float> calculateBillableUsage(
			Map<MeasurableService, Float> usage, int period, int size,
			float heatFactor) {
		Map<BillableService, Float> finalUsage =
				new HashMap<BillableService, Float>();

		float cw = usage.get(MeasurableService.CW);
		float zw = usage.get(MeasurableService.ZW);
		float ccw = usage.get(MeasurableService.CCW);
		finalUsage.put(BillableService.WODA, cw + zw - ccw);
		finalUsage.put(BillableService.PODGRZANIE, cw - ccw);
		finalUsage.put(BillableService.SCIEKI, cw + zw - ccw);
		finalUsage.put(BillableService.CO, usage.get(MeasurableService.CO)
				* heatFactor);
		finalUsage.put(BillableService.GAZ, usage.get(MeasurableService.GAZ));
		finalUsage.put(BillableService.EE, usage.get(MeasurableService.EE));

		finalUsage.put(BillableService.SMIECI, (float) period * size);
		finalUsage.put(BillableService.INTERNET, (float) period);

		return finalUsage;
	}

	/**
	 * Calculates usage of administrative services
	 * @param loc
	 * @param start
	 * @param end
	 * @param house
	 * @return
	 */
	private Map<BillableService, Float> getAdministrativeUsage(Locum loc,
			Calendar start, Calendar end, Building house) {

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
