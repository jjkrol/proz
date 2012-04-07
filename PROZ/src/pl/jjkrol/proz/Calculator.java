package pl.jjkrol.proz;

import java.util.*;

public class Calculator {

	public Map<BillableService, Float> calculatePayment(House house, Locum loc,
			Calendar start, Calendar end, Calendar quotationDate) {
		// Map<MeasurableService, Float> resultMap = new
		// HashMap<MeasurableService, Float>();

		Map<BillableService, Float> payment = new HashMap<BillableService, Float>();
		Map<BillableService, Float> administrativePayment = new HashMap<BillableService, Float>();
		
		Map<MeasurableService, Float> usage = loc.getUsage(start, end);
		int period = end.get(Calendar.MONTH) - start.get(Calendar.MONTH); // correct?
		int occupantsNumber = loc.getOccupants().size();
		float heatFactor = house.getHeatFactor(start, end);

		Map<BillableService, Float> billableUsage = calculateBillableUsage(
				usage, period, occupantsNumber, heatFactor);
		Map<BillableService, Float> administrativeUsage = getAdministrativeUsage(loc, 
				start, end, house);
		Map<BillableService, Quotation> quotations = loc.getQuotationSet(quotationDate);

		for (BillableService serv : billableUsage.keySet()){
			float locumUsage = billableUsage.get(serv);
			float quotation = quotations.get(serv).getPrice();
			payment.put(serv, locumUsage*quotation);
			
		}
		for (BillableService serv : administrativeUsage.keySet()){
			float locumAdministrativeUsage = administrativeUsage.get(serv);
			float quotation = quotations.get(serv).getPrice();
			administrativePayment.put(serv, locumAdministrativeUsage*quotation);
			
		}
		System.out.println(administrativePayment);
		return payment;
	}

	private Map<BillableService, Float> calculateBillableUsage(
			Map<MeasurableService, Float> usage, int period, int size,
			float heatFactor) {
		Map<BillableService, Float> finalUsage = new HashMap<BillableService, Float>();
		finalUsage.put(BillableService.CO, usage.get(MeasurableService.CO)
				* heatFactor);
		float cw = usage.get(MeasurableService.CW);
		float zw = usage.get(MeasurableService.ZW);
		float ccw = usage.get(MeasurableService.CCW);
		finalUsage.put(BillableService.WODA, cw + zw - ccw);
		finalUsage.put(BillableService.PODGRZANIE, cw - ccw);
		finalUsage.put(BillableService.SCIEKI, cw + zw - ccw);
		finalUsage.put(BillableService.GAZ, usage.get(MeasurableService.GAZ));
		finalUsage.put(BillableService.EE, usage.get(MeasurableService.EE));
		finalUsage.put(BillableService.SMIECI, (float) period * size);
		finalUsage.put(BillableService.INTERNET, (float) period);

		return finalUsage;
	}

	private Map<BillableService, Float> getAdministrativeUsage(Locum loc, Calendar start,
			Calendar end, House house) {
		
		Map<BillableService, Float> retMap = new HashMap<BillableService, Float>();
		Map<MeasurableService, Float> houseUsage = house.getUsage(start, end);
		float heatFactor = house.getHeatFactor(start, end);
		float partFact = loc.getParticipationFactor();
		System.out.println(houseUsage.get(MeasurableService.EE));
		retMap.put(BillableService.CO, houseUsage.get(MeasurableService.CO_ADM)
				* heatFactor * partFact);
		retMap.put(BillableService.WODA,
				houseUsage.get(MeasurableService.POLEWACZKI) * partFact);
		retMap.put(BillableService.EE, houseUsage.get(MeasurableService.EE) * partFact);
		
		return retMap;
	}

}
