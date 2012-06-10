package pl.jjkrol.proz.mockups;

import pl.jjkrol.proz.model.Occupant;

/**
 * @author  jjkrol
 */
public class OccupantMockup {

	/**
	 * occupant's id
	 * @uml.property  name="id"
	 */
	private final int id;
	/**
	 * occupant's name
	 * @uml.property  name="name"
	 */
	private final String name;
	/**
	 * occupant's address
	 * @uml.property  name="address"
	 */
	private final String address;
	/**
	 * occupant's telephone
	 * @uml.property  name="telephone"
	 */
	private final String telephone;
	/**
	 * occupant's nip
	 * @uml.property  name="nip"
	 */
	private final String nip;
	/**
	 * type of billing
	 * @uml.property  name="billingType"
	 * @uml.associationEnd  
	 */
	private final Occupant.Billing billingType;

	public OccupantMockup(int id, String givenName, String givenAddress,
			String givenTelephone, String givenNip, Occupant.Billing billingType) {
		this.id = id;
		this.name = givenName;
		this.address = givenAddress;
		this.telephone = givenTelephone;
		this.nip = givenNip;
		this.billingType = billingType;
	}

	/**
	 * @return  the id
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return  the name
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return  the address
	 * @uml.property  name="address"
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return  the telephone
	 * @uml.property  name="telephone"
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @return  the nip
	 * @uml.property  name="nip"
	 */
	public String getNip() {
		return nip;
	}

	/**
	 * @return  the billingType
	 * @uml.property  name="billingType"
	 */
	public Occupant.Billing getBillingType() {
		return billingType;
	}

}
