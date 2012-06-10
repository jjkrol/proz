package pl.jjkrol.proz.model;

/**
 * Enumeration type respresenting services that occupant pays for
 * @author     jjkrol
 */
public enum BillableService {
	/**
	 * @uml.property  name="cO"
	 * @uml.associationEnd  
	 */
	CO,
	/**
	 * @uml.property  name="eE"
	 * @uml.associationEnd  
	 */
	EE,
	/**
	 * @uml.property  name="gAZ"
	 * @uml.associationEnd  
	 */
	GAZ,
	/**
	 * @uml.property  name="wODA"
	 * @uml.associationEnd  
	 */
	WODA, 
	/**
	 * @uml.property  name="pODGRZANIE"
	 * @uml.associationEnd  
	 */
	PODGRZANIE, 
	/**
	 * @uml.property  name="sCIEKI"
	 * @uml.associationEnd  
	 */
	SCIEKI,
	/**
	 * @uml.property  name="sMIECI"
	 * @uml.associationEnd  
	 */
	SMIECI,
	/**
	 * @uml.property  name="iNTERNET"
	 * @uml.associationEnd  
	 */
	INTERNET
}
