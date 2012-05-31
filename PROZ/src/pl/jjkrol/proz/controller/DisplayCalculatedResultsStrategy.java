package pl.jjkrol.proz.controller;

import java.util.Calendar;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.payments.CalculatedResultsNeededEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import pl.jjkrol.proz.model.BronowskaCalculator;
import pl.jjkrol.proz.model.Model;
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
class DisplayCalculatedResultsStrategy extends PROZStrategy {
	static Logger logger = Logger.getLogger(PROZStrategy.class);

	DisplayCalculatedResultsStrategy(View view, Model model) {
		super(view, model);
	}

	public void execute(final PROZEvent e) {
		LocumMockup emptyLocum = ((CalculatedResultsNeededEvent) e).locum;
		Calendar from = ((CalculatedResultsNeededEvent) e).from.date;
		Calendar to = ((CalculatedResultsNeededEvent) e).to.date;
		String quotation = ((CalculatedResultsNeededEvent) e).quotation;
		PaymentCalculator calculator = new BronowskaCalculator(); // FIXME this
																	// should
																	// not be
																	// here
		try {
			final ResultMockup result =
					model.calculateResults(calculator, emptyLocum.getName(), from,
							to, quotation);

			final PaymentsTab c =
					(PaymentsTab) view.getSpecificView(PaymentsTab.class);

			c.displayCalculationResults(result);
		} catch (NoSuchLocum exception) {
			logger.warn("No such locum!");
		} catch (NoSuchQuotationSet exception) {
			logger.warn("No such quotation set!");
		} catch (NoSuchTabException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}
}
