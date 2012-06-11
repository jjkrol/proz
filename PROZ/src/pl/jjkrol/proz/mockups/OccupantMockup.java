package pl.jjkrol.proz.mockups;

import pl.jjkrol.proz.model.Occupant;

/**
 * The Class OccupantMockup.
 * 
 * @author jjkrol
 */
public class OccupantMockup {

	/** occupant's id. */
	private final int id;

	/** occupant's name. */
	private final String name;

	/** occupant's address. */
	private final String address;

	/** occupant's telephone. */
	private final String telephone;

	/** occupant's nip. */
	private final String nip;

	/** type of billing. */
	private final Occupant.Billing billingType;

	/**
	 * Instantiates a new occupant mockup.
	 * 
	 * @param id
	 *            the id
	 * @param givenName
	 *            the given name
	 * @param givenAddress
	 *            the given address
	 * @param givenTelephone
	 *            the given telephone
	 * @param givenNip
	 *            the given nip
	 * @param billingType
	 *            the billing type
	 */
	public OccupantMockup(final int id, final String givenName,
			final String givenAddress, final String givenTelephone,
			final String givenNip, final Occupant.Billing billingType) {
		this.id = id;
		this.name = givenName;
		this.address = givenAddress;
		this.telephone = givenTelephone;
		this.nip = givenNip;
		this.billingType = billingType;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the address.
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Gets the telephone.
	 * 
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Gets the nip.
	 * 
	 * @return the nip
	 */
	public String getNip() {
		return nip;
	}

	/**
	 * Gets the billing type.
	 * 
	 * @return the billingType
	 */
	public Occupant.Billing getBillingType() {
		return billingType;
	}

}
