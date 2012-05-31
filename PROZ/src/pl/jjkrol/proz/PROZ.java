package pl.jjkrol.proz;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.model.BronowskaCalculator;
import pl.jjkrol.proz.model.PaymentCalculator;

/**
 * Main application class
 * @author jjkrol
 */
public class PROZ {
	static Logger logger = Logger.getLogger(PROZ.class);


	public static void main(String[] args) {
		//configure logger
		BasicConfigurator.configure();
		logger.info("Entering application");
			Controller contr = Controller.getInstance();
			PaymentCalculator calc = new BronowskaCalculator();
			contr.run(calc);
			
		logger.info("Exiting application");
	}

}
