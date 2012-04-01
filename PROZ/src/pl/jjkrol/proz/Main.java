package pl.jjkrol.proz;

import java.util.*;

public class Main {
	static Locum m1;
	static Locum m2;
	static Locum m3;
	static Locum m4;
	static House mainHouse;

	public static void main(String[] args) {

		createHouse();
		loadInitData();

		Map<Service, Float> usage = mainHouse.getUsage(new GregorianCalendar(
				2010, 1, 2), new GregorianCalendar(2010, 2, 4));
		for (Service serv : usage.keySet()) {
			System.out.println(serv + ": " + usage.get(serv));
		}

		/*
		 * require './lokal.rb' require './templateTreeBuilder.rb' dom =
		 * House.new m01 = Flat.new("m01") m1 = Flat.new("m1") m2 =
		 * Flat.new("m2") m3 = Flat.new("m3") m4 = Flat.new("m4") m5 =
		 * Office.new("m5") m6 = Office.new("m6") m7 = Flat.new("m7") m8 =
		 * Office.new("m8") flats = [m01, m1, m2, m3, m4, m5, m6, m7, m8] stawka
		 * = Quotation.new("Od 2.01.2010",{ :internet => 20, :smieci => 27.39,
		 * :co => 90, :woda => 2.59, :scieki => { :flat => 19, :office => 22, },
		 * :ee => { :flat => 0.51, :office => 0.58, }, :gaz => 2.35, :podgrzanie
		 * => 23.24, }) puts m2.calculatePayment("2.01.2010","4.02.2010",
		 * stawka, dom)
		 */
	}

	private static void createHouse() {
		mainHouse = new House("dom1");
		m1 = new Locum(50, "m1");
		m1.addOccupant(new Occupant("Jan Kowalski"));
		m2 = new Locum(50, "m2");
		m3 = new Locum(50, "m3");
		m4 = new Locum(50, "m4");
		mainHouse.addLocum(m1);
		mainHouse.addLocum(m2);
		mainHouse.addLocum(m3);
		mainHouse.addLocum(m4);
		mainHouse.listLocums();
	}

	private static void loadInitData() {
		Calendar cal1 = new GregorianCalendar(2010, 1, 2);
		Map<Service, Float> measure1 = new HashMap<Service, Float>();
		measure1.put(Service.GAZ, 53095.5f);
		measure1.put(Service.EE, 101112.4f);
		measure1.put(Service.WODA_GL, 7255.71f);
		measure1.put(Service.CW, 1151.15f);
		measure1.put(Service.POLEWACZKI, 1749.04f);
		measure1.put(Service.EE_ADM, 42372.5f);
		measure1.put(Service.CIEPLO, 331675f);
		measure1.put(Service.CO_ADM, 714.84f);
		measure1.put(Service.CO, 0f);

		Calendar cal2 = new GregorianCalendar(2010, 2, 4);
		Map<Service, Float> measure2 = new HashMap<Service, Float>();
		measure2.put(Service.GAZ, 55386.8f);
		measure2.put(Service.EE, 103101f);
		measure2.put(Service.WODA_GL, 7374.08f);
		measure2.put(Service.CW, 1173.44f);
		measure2.put(Service.POLEWACZKI, 1749.04f);
		measure2.put(Service.EE_ADM, 43192.5f);
		measure2.put(Service.CIEPLO, 352768f);
		measure2.put(Service.CO_ADM, 743.14f);
		measure2.put(Service.CO, 989.15f);

		mainHouse.addMeasures(cal1, measure1);
		mainHouse.addMeasures(cal2, measure2);
	}

}
