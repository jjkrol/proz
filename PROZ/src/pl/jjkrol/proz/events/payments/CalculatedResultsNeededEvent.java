package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;

/**
 * @author   jjkrol
 */
public class CalculatedResultsNeededEvent extends PaymentsEvent {
	/**
	 * @uml.property  name="locum"
	 * @uml.associationEnd  
	 */
	public LocumMockup locum;
	/**
	 * @uml.property  name="from"
	 * @uml.associationEnd  
	 */
	public MeasurementMockup from;
	/**
	 * @uml.property  name="to"
	 * @uml.associationEnd  
	 */
	public MeasurementMockup to;
	public String quotation;
	
	public CalculatedResultsNeededEvent(LocumMockup locum, MeasurementMockup from,
			MeasurementMockup to, String quotation) {
		this.locum = locum;
		this.from = from;
		this.to = to;
		this.quotation = quotation;
	}
}
