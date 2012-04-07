package pl.jjkrol.proz;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.io.FileReader;
import au.com.bytecode.opencsv.*;

public class Controller {
	Locum m01, m1, m2, m3, m4, m5, m6, m7, m8;
	House mainHouse;

	public void createHouse() {

		mainHouse = new House("dom1", "Bronowska 52");

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
		// mainHouse.listLocums();
	}

	public void readSampleData() {
		List<String[]> myEntries;
		try {
			CSVReader reader = new CSVReader(new FileReader("data.csv"));
			myEntries = reader.readAll();
		} catch (Exception e) {
			return;
		}

		List<Calendar> dates = new ArrayList<Calendar>();
		int lineIndex = 0;
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
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
					if(value.isEmpty()) value = "0";
					Calendar date = dates.get(i);
					if(!measures.containsKey(date))
						measures.put(date, new HashMap<MeasurableService, Float>());
					measures.get(date).put(currentService, new Float(value));
					i++;
				}
			}
			
			for(Calendar date : measures.keySet()){
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

	private void showData(Calendar start, Calendar end) {
		for (Locum loc : mainHouse.getLocums()) {
			System.out.println(loc.getName());
			printUsage(loc, start, end);
		}
	}

	private Map<MeasurableService, Float> getMeasureMap(List<Float> values) {
		List<MeasurableService> services = Arrays.asList(MeasurableService.CO,
				MeasurableService.ZW, MeasurableService.CW,
				MeasurableService.CCW, MeasurableService.GAZ,
				MeasurableService.EE);
		Map<MeasurableService, Float> returnMap = new HashMap<MeasurableService, Float>();
		for (int i = 0; i < values.size(); i++) {
			returnMap.put(services.get(i), values.get(i));
		}

		return returnMap;
	}

	private void printUsage(Measurable measurable, Calendar start, Calendar end) {
		Map<MeasurableService, Float> usage = measurable.getUsage(start, end);
		for (MeasurableService serv : usage.keySet()) {
			System.out.println(serv + ": " + usage.get(serv));
		}
	}

	public void run() {
		// showData(start, end);
		Calculator calc = new Calculator();
		
		Calendar start = new GregorianCalendar(2010, Calendar.JULY, 31);
		Calendar end = new GregorianCalendar(2010, Calendar.SEPTEMBER, 1);
		
		Map<BillableService, Float> payment = calc.calculatePayment(mainHouse,
				m1, start, end, "pierwsze");
		System.out.println(payment);
	}
}
