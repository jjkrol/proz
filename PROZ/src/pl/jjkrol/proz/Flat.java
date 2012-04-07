package pl.jjkrol.proz;

public class Flat extends Locum{
	Flat(float givenArea, String givenName){
		super(givenArea, givenName);
	}
	
	Flat(float givenArea, String givenName, Ownership ownership){
		super(givenArea, givenName, ownership);
	}
}
