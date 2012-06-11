package pl.jjkrol.proz.controller.payments;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import pl.jjkrol.proz.events.PROZEvent;
import pl.jjkrol.proz.events.payments.GenerateInvoiceEvent;
import pl.jjkrol.proz.mockups.LocumMockup;
import pl.jjkrol.proz.mockups.ResultMockup;
import pl.jjkrol.proz.model.BillableService;
import pl.jjkrol.proz.model.DocumentBuilder;
import pl.jjkrol.proz.model.DocumentDirector;
import pl.jjkrol.proz.model.Model;
import pl.jjkrol.proz.model.NoBuilderSet;
import pl.jjkrol.proz.model.UsageTableBuilder;
import pl.jjkrol.proz.view.NoSuchTabException;
import pl.jjkrol.proz.view.PaymentsTab;
import pl.jjkrol.proz.view.View;

/**
 * generates a pdf with a usage table and displays it.
 */
public class GenerateInvoiceStrategy extends PaymentsStrategy {
	
	/** The logger. */
	static Logger logger = Logger.getLogger("strategy");

	/**
	 * Instantiates a new generate invoice strategy.
	 *
	 * @param view the view
	 * @param model the model
	 */
	public GenerateInvoiceStrategy(final View view, final Model model) {
		super(view, model);
	}

	/**
	 * {@inheritDoc}
	 */
	public void execute(final PROZEvent event) {
		final ResultMockup result = ((GenerateInvoiceEvent) event).result;
		
		final Calendar from = result.getFrom();
		final Calendar to = result.getTo();
		LocumMockup loc = result.getLocum();

		String name = loc.getName();
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String dateFrom = df.format(from.getTime());
		String dateTo = df.format(to.getTime());
		String filepath = "tabele/" + dateFrom + "_" + dateTo + "/";
		String filename = name + ".pdf";

		
		Map<String, String> valuesMap = getValuesMap(result, from, to, df);

		DocumentBuilder builder = new UsageTableBuilder();
		DocumentDirector director = new DocumentDirector();
		director.setBuilder(builder);
		try {
			director.buildDocument(filepath, filename, valuesMap);
		} catch (NoBuilderSet e) {
			logger.warn("No builder set!");
		}
		PaymentsTab c;
		try {
			c = (PaymentsTab) view.getSpecificView(PaymentsTab.class);
			c.displayUsageTable(filepath + filename);
		} catch (NoSuchTabException e) {
			logger.warn("No such tab!");
		}

	}

	/**
	 * Gets the values map.
	 *
	 * @param result the result
	 * @param from the from
	 * @param to the to
	 * @param df the df
	 * @return the values map
	 */
	private Map<String, String> getValuesMap(final ResultMockup result,
			final Calendar from, final Calendar to, SimpleDateFormat df) {
		Map<String, String> valuesMap = new HashMap<String, String>();
		BigDecimal sum = getResults(result, valuesMap);
		valuesMap.put("sum", new DecimalFormat("0.00").format(sum));

		BigDecimal administrativeSum =
				getAdministrativeResults(result, valuesMap);
		valuesMap.put("adm_sum", new DecimalFormat("0.00")
				.format(administrativeSum));
		
		getStartMeasurement(result, valuesMap);
		
		getEndMeasurement(result, valuesMap);
		
		LocumMockup loc = result.getLocum();
		BigDecimal advancement = loc.getAdvancement();
		valuesMap.put("advancement", new DecimalFormat("0.00").format(advancement));
		valuesMap.put("locum_name", loc.getName());
		//FIXME: get billable occupant
		valuesMap.put("occupant", loc.getOccupants().get(0).getName());
		
		BigDecimal wholeSum = sum.add(administrativeSum);
		valuesMap.put("whole_sum", new DecimalFormat("0.00").format(wholeSum));
		BigDecimal toPay = wholeSum.subtract(advancement);
		valuesMap.put("to_pay", new DecimalFormat("0.00").format(toPay));

		getDateValues(from, to, df, valuesMap);
		return valuesMap;
	}

	/**
	 * Gets the end measurement.
	 *
	 * @param result the result
	 * @param valuesMap the values map
	 * @return the end measurement
	 */
	private void getEndMeasurement(final ResultMockup result,
			Map<String, String> valuesMap) {
		for (BillableService serv : result.getBillableMeasurementEnd().keySet()) {
			String key = "mea_end_"+serv.toString();
			Float value = result.getBillableMeasurementEnd().get(serv);
			String stringValue = new DecimalFormat("0.00").format(value);
			valuesMap.put(key, stringValue);
		}
	}

	/**
	 * Gets the start measurement.
	 *
	 * @param result the result
	 * @param valuesMap the values map
	 * @return the start measurement
	 */
	private void getStartMeasurement(final ResultMockup result,
			Map<String, String> valuesMap) {
		for (BillableService serv : result.getBillableMeasurementStart().keySet()) {
			String key = "mea_start_"+serv.toString();
			Float value = result.getBillableMeasurementStart().get(serv);
			String stringValue = new DecimalFormat("0.00").format(value);
			valuesMap.put(key, stringValue);
		}
	}

	/**
	 * Gets the administrative results.
	 *
	 * @param result the result
	 * @param valuesMap the values map
	 * @return the administrative results
	 */
	private BigDecimal getAdministrativeResults(final ResultMockup result,
			Map<String, String> valuesMap) {
		BigDecimal administrativeSum = new BigDecimal(0);
		for (BillableService serv : result.getAdministrativeResults().keySet()) {
			String key = "adm_" + serv.toString();
			BigDecimal value = result.getAdministrativeResults().get(serv);
			String stringValue = new DecimalFormat("0.00").format(value);
			administrativeSum = administrativeSum.add(value);
			valuesMap.put(key, stringValue);
		}
		return administrativeSum;
	}

	/**
	 * Gets the results.
	 *
	 * @param result the result
	 * @param valuesMap the values map
	 * @return the results
	 */
	private BigDecimal getResults(final ResultMockup result,
			Map<String, String> valuesMap) {
		BigDecimal sum = new BigDecimal(0);
		for (BillableService serv : result.getResults().keySet()) {
			String key = serv.toString();
			BigDecimal value = result.getResults().get(serv);
			String stringValue = new DecimalFormat("0.00").format(value);
			sum = sum.add(value);
			valuesMap.put(key, stringValue);
		}
		return sum;
	}

	/**
	 * Gets the date values.
	 *
	 * @param from the from
	 * @param to the to
	 * @param df the df
	 * @param valuesMap the values map
	 * @return the date values
	 */
	private void getDateValues(final Calendar from, final Calendar to,
			SimpleDateFormat df, Map<String, String> valuesMap) {
		valuesMap.put("from", df.format(from.getTime()));
		valuesMap.put("to", df.format(to.getTime()));
	}
}
