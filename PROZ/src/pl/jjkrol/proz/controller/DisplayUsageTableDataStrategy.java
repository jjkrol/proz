package pl.jjkrol.proz.controller;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.UsageTableData;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.View;

/**
 * The Class DisplayUsageTableDataStrategy.
 */
public class DisplayUsageTableDataStrategy extends PROZStrategy {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(PROZStrategy.class);
	
	/**
	 * Instantiates a new display usage table data strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	public DisplayUsageTableDataStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		final UsageTableData usageTableData = model.getUsageTableData(); 
		try {
			PaymentsTab c = (PaymentsTab) view.getSpecificView(PaymentsTab.class);
			c.displayUsageTableData(usageTableData);
		} catch (NoSuchTabException e) {
			logger.warn("No such tab!");
		}
	}
}
