package pl.jjkrol.proz;

public class Occupant {

	private final String name;
	private String address;
	private String telephone;
	private String nip;
	private Billing billingType;
	
	enum Billing{
		INVOICE, BILL;
	}
	
	Occupant(String givenName){
		name = givenName;
	}

	public boolean equals(Object otherObject){
		if (this == otherObject) return true;
		if (!(otherObject instanceof Occupant) ) return false;
		Occupant otherOccupant = (Occupant) otherObject;
		if (name.equals(otherOccupant.getName())) return true;	
		return false;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getNip() {
		return nip;
	}

	public void setNip(String nip) {
		this.nip = nip;
	}

	public Billing getBillingType() {
		return billingType;
	}

	public void setBillingType(Billing billingType) {
		this.billingType = billingType;
	}

	public String getName() {
		return name;
	}
	
	
}
