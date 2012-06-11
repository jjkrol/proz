package pl.jjkrol.proz.model;

/**
 * The Class Flat.
 */
public class Flat extends Locum{
	
	/**
	 * Instantiates a new flat.
	 *
	 * @param givenArea the given area
	 * @param givenName the given name
	 */
	public Flat(final float givenArea, final String givenName){
		super(givenArea, givenName);
	}
	
	/**
	 * Instantiates a new flat.
	 *
	 * @param givenArea the given area
	 * @param givenName the given name
	 * @param ownership the ownership
	 */
	public Flat(final float givenArea, final String givenName, final Ownership ownership){
		super(givenArea, givenName, ownership);
	}
}
