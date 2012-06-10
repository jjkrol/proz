package pl.jjkrol.proz.controller.payments;

import java.util.List;
import java.util.Map;

import pl.jjkrol.proz.controller.PROZStrategy;
import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.payments.LocumMeasurementsAndQuotationsNeededEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.MeasurementMockup;
import pl.jjkrol.proz.mockups.QuotationMockup;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoSuchLocum;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.View;

import org.apache.log4j.Logger;

	/**
	 * displays measurements and quotations of the locum for showing in the
	 * payments tab
	 */
	public class DisplayLocumMeasurementsAndQuotationsStrategy extends PaymentsStrategy {
		static Logger logger = Logger.getLogger(PROZStrategy.class);
		
		public DisplayLocumMeasurementsAndQuotationsStrategy(View view, Model model) {
			super(view, model);
		}

		public void execute(final PROZEvent e) {
			LocumMockup emptyMockup =
					((LocumMeasurementsAndQuotationsNeededEvent) e).moc;
			String locumName = emptyMockup.getName();
			try {

				// get measurement and quotation data
				final List<MeasurementMockup> measurements =
						model.getLocumMeasurementMockups(locumName);
				final Map<String, List<QuotationMockup>> quotations =
						model.getLocumQuotationMockups(locumName);

				final PaymentsTab v =
						(PaymentsTab) view.getSpecificView(PaymentsTab.class);
						v.displayLocumMeasurementsAndQuotations(measurements,
								quotations);
			} catch (NoSuchLocum exception) {
				logger.warn("No such locum: " + locumName + "!");
				// TODO some messagebox?
			} catch (NoSuchTabException exception) {
				logger.warn("No such tab");
				exception.printStackTrace();
			}
		}
	}
