package pl.jjkrol.proz.model;

import java.util.*;
import pl.jjkrol.proz.model.NoSuchQuotationSet;
import org.apache.log4j.Logger;
public class BronowskaCalculator implements PaymentCalculator {
static Logger logger = Logger.getLogger(BronowskaCalculator.class);
	public Map<BillableService, Float> calculatePayment(House house, Locum loc,
			Calendar start, Calendar end, String quotationName)
			throws NoSuchQuotationSet {

		int period = end.get(Calendar.MONTH) - start.get(Calendar.MONTH); //TODO correct?
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

	public Map<BillableService, Float> calculateAdministrativePayment(
			House house, Locum loc, Calendar start, Calendar end,
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

	private Map<BillableService, Float> getAdministrativeUsage(Locum loc,
			Calendar start, Calendar end, House house) {

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
