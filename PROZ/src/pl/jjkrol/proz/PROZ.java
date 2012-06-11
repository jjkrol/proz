package pl.jjkrol.proz;

import java.util.List;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.model.BronowskaCalculator;
import pl.jjkrol.proz.model.PaymentCalculator;

/**
 * Main application class.
 * 
 * @author jjkrol
 */
public class PROZ {

	/** The logger. */
	static Logger logger = Logger.getLogger(PROZ.class);

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {
		// configure logger
		BasicConfigurator.configure();
		// PropertyConfigurator.configure("log.properties");
		logger.info("Entering application");

		// Get default locale
		Locale locale = Locale.getDefault();

		// Set the default locale to pre-defined locale
		Locale.setDefault(Locale.ENGLISH);

		// Set the default locale to custom locale
		locale = new Locale("en", "EN");
		Locale.setDefault(locale);

		Controller contr = Controller.getInstance();
		contr.run();

		logger.info("Exiting application");
	}

}
