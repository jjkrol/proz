package pl.jjkrol.proz.controller.payments;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.mockups.InvoiceData;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.View;

/**
 * The Class DisplayInvoiceDataStrategy.
 */
public class DisplayInvoiceDataStrategy extends PROZStrategy {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(PROZStrategy.class);
	
	/**
	 * Instantiates a new display invoice data strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	public DisplayInvoiceDataStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		final InvoiceData invoiceData = model.getInvoiceData(); 
		try {
			PaymentsTab c = (PaymentsTab) view.getSpecificView(PaymentsTab.class);
			c.displayInvoiceData(invoiceData);
		} catch (NoSuchTabException e) {
			logger.warn("No such tab!");
		}
	}
}
