package pl.jjkrol.proz.model;

public class Flat extends Locum{
	public Flat(float givenArea, String givenName){
		super(givenArea, givenName);
	}
	
	public Flat(float givenArea, String givenName, Ownership ownership){
		super(givenArea, givenName, ownership);
	}
}
