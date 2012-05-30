package pl.jjkrol.proz;

import java.util.List;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;

import pl.jjkrol.proz.controller.Controller;
import pl.jjkrol.proz.model.BronowskaCalculator;
import pl.jjkrol.proz.model.HibernateUtil;
import pl.jjkrol.proz.model.Locum;
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
		/* Hibernate test
		HibernateUtil.configureSessionFactory();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
		transaction = session.beginTransaction();
		List<Locum> locums = session.createQuery("from Locum").list();
		for (Locum loc : locums )
		{
		System.out.println(loc.getName());
		}
		transaction.commit();
		} catch (HibernateException e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		transaction.rollback();
		e.printStackTrace();
		} finally {
		session.close();
		}		
		*/
		
			Controller contr = Controller.getInstance();
			contr.prepareInitialData();
			contr.readSampleData();

			PaymentCalculator calc = new BronowskaCalculator();
			contr.run(calc);
			
		logger.info("Exiting application");
	}

}
