package pl.jjkrol.proz;

import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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

	public static void listResult(List<?> result) {
		System.out.println(result.size());
		for (Object o : result) {
			System.out.println(o);
		}
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		logger.info("Entering application");
		try {
			UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager
					.getLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {

		}
			Controller contr = Controller.getInstance();
			contr.prepareInitialData();
			contr.readSampleData();

			PaymentCalculator calc = new BronowskaCalculator();
			contr.run(calc);
			
		logger.info("Exiting application");
	}

}
