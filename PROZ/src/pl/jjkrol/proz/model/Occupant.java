package pl.jjkrol.proz.model;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.mockups.OccupantMockup;

// TODO: Auto-generated Javadoc
/**
 * The Class Occupant.
 *
 * @author  jjkrol
 */
public class Occupant {

	/**
	 * reprezenting type of billing.
	 */
	public enum Billing {
		
		/** The BILL. */
		BILL {
			public String toString() {
				return "Rachunek";
			}
		},
		
		/** The INVOICE. */
		INVOICE {
			public String toString() {
				return "Faktura";
			}
		}
	}

	/** The logger. */
	static Logger logger = Logger.getLogger(Occupant.class);

	/** selected type of billing for the occupant. */
	private Billing billingType;
	
	/** occupant's id. */
	private int id;
	
	/** occupant's name. */
	private String name;
	
	/** occupant's address. */
	private String address;
	
	/** occupant's nip. */
	private String nip;
	
	/** occupant's telephone. */
	private String telephone;

	/**
	 * Instantiates a new occupant.
	 * For hibernate
	 */
	private Occupant() {

	}

	/**
	 * Instantiates a new occupant.
	 *
	 * @param mockup the mockup
	 */
	public Occupant(OccupantMockup mockup) {
		setAttributes(mockup);
	}

	/**
	 * Instantiates a new occupant.
	 *
	 * @param id the id
	 * @param givenName the given name
	 */
	public Occupant(int id, String givenName) {
		this.id = id;
		name = givenName;
		billingType = Billing.BILL;
	}

	/**
	 * Gets the address.
	 *
	 * @return  the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Gets the billing type.
	 *
	 * @return  the billingType
	 */
	public Billing getBillingType() {
		return billingType;
	}

	/**
	 * Gets the id.
	 *
	 * @return  the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the mockup.
	 *
	 * @return the mockup
	 */
	public OccupantMockup getMockup() {
		return new OccupantMockup(id, name, address, telephone, nip,
				billingType);
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
	 * Gets the nip.
	 *
	 * @return  the nip
	 */
	public String getNip() {
		return nip;
	}

	/**
	 * Gets the telephone.
	 *
	 * @return  the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Sets the address.
	 *
	 * @param address  the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Sets the attributes.
	 *
	 * @param moc the new attributes
	 */
	public void setAttributes(OccupantMockup moc) {
		this.name = moc.getName();
		this.address = moc.getAddress();
		this.telephone = moc.getTelephone();
		this.nip = moc.getNip();
		this.billingType = moc.getBillingType();
	}

	/**
	 * Sets the billing type.
	 *
	 * @param billingType  the billingType to set
	 */
	public void setBillingType(Billing billingType) {
		this.billingType = billingType;
	}

	/**
	 * Sets the id.
	 *
	 * @param id  the id to set
	 */
	private void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the name.
	 *
	 * @param name  the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the nip.
	 *
	 * @param nip  the nip to set
	 */
	public void setNip(String nip) {
		this.nip = nip;
	}

	/**
	 * Sets the telephone.
	 *
	 * @param telephone  the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
