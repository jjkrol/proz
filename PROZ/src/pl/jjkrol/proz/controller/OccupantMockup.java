package pl.jjkrol.proz.controller;

import pl.jjkrol.proz.model.Occupant;

public class OccupantMockup {
	
	public final int id;
	public final String name;
	public final String address;
	public final String telephone;
	public final String nip;
	public final Occupant.Billing billingType;
	
	
	public OccupantMockup(int id, String givenName, String givenAddress, String givenTelephone, String givenNip, Occupant.Billing billingType){
		this.id = id;
		this.name = givenName;
		this.address = givenAddress;
		this.telephone = givenTelephone;
		this.nip = givenNip;
		this.billingType = billingType;
	}

	
}
