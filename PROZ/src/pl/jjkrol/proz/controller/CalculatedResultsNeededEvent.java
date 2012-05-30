package pl.jjkrol.proz.controller;

public class CalculatedResultsNeededEvent extends PROZEvent {
	LocumMockup locum;
	MeasurementMockup from;
	MeasurementMockup to;
	String quotation;
	
	public CalculatedResultsNeededEvent(LocumMockup locum, MeasurementMockup from,
			MeasurementMockup to, String quotation) {
		this.locum = locum;
		this.from = from;
		this.to = to;
		this.quotation = quotation;
	}
}
