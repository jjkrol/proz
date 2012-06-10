package pl.jjkrol.proz.controller;

import java.util.Calendar;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.payments.CalculatedResultsNeededEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import pl.jjkrol.proz.model.BronowskaCalculator;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchDate;
import pl.jjkrol.proz.model.NoSuchLocum;
import pl.jjkrol.proz.model.NoSuchQuotationSet;
import pl.jjkrol.proz.model.PaymentCalculator;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.View;
import org.apache.log4j.Logger;

/**
 * displays results of payment calculations
 */
class DisplayCalculatedResultsStrategy extends PaymentsStrategy {
	static Logger logger = Logger.getLogger(PROZStrategy.class);

	DisplayCalculatedResultsStrategy(View view, Model model) {
		super(view, model);
	}

	public void execute(final PROZEvent event) {
		LocumMockup emptyLocum = ((CalculatedResultsNeededEvent) event).locum;
		Calendar from = ((CalculatedResultsNeededEvent) event).from.getDate();
		Calendar to = ((CalculatedResultsNeededEvent) event).to.getDate();
		String quotation = ((CalculatedResultsNeededEvent) event).quotation;
		// FIXME this should not be here
		PaymentCalculator calculator = new BronowskaCalculator();
		try {
			final ResultMockup result =
					model.calculateResults(calculator, emptyLocum.getName(),
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
