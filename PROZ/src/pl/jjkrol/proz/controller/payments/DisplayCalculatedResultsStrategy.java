package pl.jjkrol.proz.controller.payments;

import java.util.Calendar;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.payments.CalculatedResultsNeededEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchDate;
import pl.jjkrol.proz.model.NoSuchLocum;
import pl.jjkrol.proz.model.NoSuchQuotationSet;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.View;

/**
 * displays results of payment calculations.
 */
public class DisplayCalculatedResultsStrategy extends PaymentsStrategy {
	
	/** The logger. */
	static Logger logger = Logger.getLogger(PROZStrategy.class);

	/**
	 * Instantiates a new display calculated results strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	public DisplayCalculatedResultsStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		LocumMockup emptyLocum = ((CalculatedResultsNeededEvent) event).locum;
		Calendar from = ((CalculatedResultsNeededEvent) event).from.getDate();
		Calendar to = ((CalculatedResultsNeededEvent) event).to.getDate();
		String quotation = ((CalculatedResultsNeededEvent) event).quotation;
		try {
			final ResultMockup result =
					model.calculateResults(emptyLocum.getName(),
							from, to, quotation);

			final PaymentsTab c =
					(PaymentsTab) view.getSpecificView(PaymentsTab.class);

			c.displayCalculationResults(result);
		} catch (NoSuchLocum e) {
			logger.warn("No such locum!");
		} catch (NoSuchQuotationSet e) {
			logger.warn("No such quotation set!");
		} catch (NoSuchTabException e) {
			logger.warn("No such tab!");
		} catch (NoSuchDate e) {
			logger.warn("No such date!");
		}
	}
}
