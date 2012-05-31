package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.events.PROZEvent;

public class CalculatedResultsNeededEvent extends PROZEvent {
	public LocumMockup locum;
	public MeasurementMockup from;
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
