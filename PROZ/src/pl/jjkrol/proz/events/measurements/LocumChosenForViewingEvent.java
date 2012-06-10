package pl.jjkrol.proz.events.measurements;

import pl.jjkrol.proz.mockups.LocumMockup;


/**
 * @author   jjkrol
 */
public class LocumChosenForViewingEvent extends MeasurementsEvent {
	/**
	 * @uml.property  name="moc"
	 * @uml.associationEnd  
	 */
	public final LocumMockup moc;
	public LocumChosenForViewingEvent(LocumMockup moc) {
		this.moc = moc;
	}
}
