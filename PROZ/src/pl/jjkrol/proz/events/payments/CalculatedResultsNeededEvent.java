package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;

/**
 * The Class CalculatedResultsNeededEvent.
 * 
 * @author jjkrol
 */
public class CalculatedResultsNeededEvent extends PaymentsEvent {

	/** The locum. */
	public LocumMockup locum;

	/** The from. */
	public MeasurementMockup from;

	/** The to. */
	public MeasurementMockup to;

	/** The quotation. */
	public String quotation;

	/**
	 * Instantiates a new calculated results needed event.
	 * 
	 * @param locum
	 *            the locum
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param quotation
	 *            the quotation
	 */
	public CalculatedResultsNeededEvent(final LocumMockup locum,
			final MeasurementMockup from, final MeasurementMockup to,
			final String quotation) {
		this.locum = locum;
		this.from = from;
		this.to = to;
		this.quotation = quotation;
	}
}
