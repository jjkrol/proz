package pl.jjkrol.proz.events.payments;

import pl.jjkrol.proz.mockups.UsageTableData;

/**
 * The Class GenerateUsageTableEvent.
 * 
 * @author jjkrol
 */
public class GenerateUsageTableEvent extends PaymentsEvent {

	/** The usage table data. */
	public final UsageTableData usageTableData;

	/**
	 * Instantiates a new generate usage table event.
	 * 
	 * @param usageTableData
	 *            the usage table data
	 */
	public GenerateUsageTableEvent(final UsageTableData usageTableData) {
		this.usageTableData = usageTableData;
	}
}
