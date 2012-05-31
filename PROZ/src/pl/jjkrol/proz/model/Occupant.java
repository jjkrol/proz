package pl.jjkrol.proz.model;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.mockups.OccupantMockup;

public class Occupant {

	private final int id;
	private String name;
	private String address;
	private String telephone;
	private String nip;
	private Billing billingType;
	static Logger logger = Logger.getLogger(Occupant.class);
	public enum Billing{
		INVOICE{
			 public String toString() {
			        return "Faktura";	
			 }
		},
		BILL{
			 public String toString() {
			        return "Rachunek";	
			 }
		}
	}
	
	public Occupant(int id, String givenName){
		this.id = id;
		name = givenName;
		billingType = Billing.BILL;
	}
	
	public Occupant(int id, OccupantMockup mockup){
		this.id = id; 
		name = mockup.name;
		address = mockup.address;
		telephone = mockup.telephone;
		nip = mockup.nip;
		billingType = mockup.billingType;
	}

	public void setAttributes(OccupantMockup moc){
		this.name = moc.name;
		this.address = moc.address;
		this.telephone = moc.telephone;
		this.nip = moc.nip;
		this.billingType = moc.billingType;
	}
	
	public String getName() {
		return name;
	}
	
	public OccupantMockup getMockup(){
		return new OccupantMockup(id, name, address, telephone, nip, billingType);
	}
	
}
