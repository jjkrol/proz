package pl.jjkrol.proz.model;

/**
 * Enumeration type respresenting services that occupant pays for
 * @author jjkrol
 *
 */
public enum BillableService {
	/**
	 * Central heating
	 */
	CO,
	/**
	 * Electric energy
	 */
	EE,
	/**
	 * Gas ?
	 */
	GAZ,
	/**
	 * water
	 */
	WODA, 
	/**
	 * Heating the water (for hot water)
	 */
	PODGRZANIE, 
	/**
	 * sewage
	 */
	SCIEKI,
	/**
	 * trash
	 */
	SMIECI,
	/**
	 * Internet
	 */
	INTERNET
}
