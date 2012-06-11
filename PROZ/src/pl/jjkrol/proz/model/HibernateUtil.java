package pl.jjkrol.proz.model;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.apache.log4j.Logger;

/**
 * The Class HibernateUtil.
 *
 * @author jjkrol
 */
public class HibernateUtil {
	
	/** The session factory. */
	private static SessionFactory sessionFactory;
	
	/** The service registry. */
	private static ServiceRegistry serviceRegistry;
	
	/** The logger. */
	static Logger logger = Logger.getLogger(HibernateUtil.class);

	static {
		try {
			Configuration cfg = new Configuration();
			cfg.configure();
			serviceRegistry =
					new ServiceRegistryBuilder().applySettings(
							cfg.getProperties()).buildServiceRegistry();
			sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		} catch (NoClassDefFoundError e) {
			logger.debug(e.getMessage());
		}
	}

	/** The Constant threadSession. */
	private static final ThreadLocal<Session> threadSession =
			new ThreadLocal<Session>();
	
	/** The Constant threadTransaction. */
	private static final ThreadLocal<Transaction> threadTransaction =
			new ThreadLocal<Transaction>();

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public static Session getSession() {
		Session s = (Session) threadSession.get();
		// Open a new Session, if this thread has none yet

		try {
			if (s == null) {
				s = sessionFactory.openSession();
				threadSession.set(s);
			}
		} catch (HibernateException ex) {
			logger.warn(ex.getMessage());
		}
		return s;
	}

	/**
	 * Close session.
	 */
	public static void closeSession() {
		try {
			Session s = (Session) threadSession.get();
			threadSession.set(null);
			if (s != null && s.isOpen())
				s.close();
		} catch (HibernateException ex) {
			logger.warn(ex.getMessage());
		}
	}

	/**
	 * Begin transaction.
	 */
	public static void beginTransaction() {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			if (tx == null) {
				tx = getSession().beginTransaction();
				threadTransaction.set(tx);
			}
		} catch (HibernateException ex) {
			logger.warn(ex.getMessage());
		}
	}

	/**
	 * Commit transaction.
	 */
	public static void commitTransaction() {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack())
				tx.commit();
			threadTransaction.set(null);
		} catch (HibernateException ex) {
			rollbackTransaction();
			logger.warn(ex.getMessage());
		}
	}

	/**
	 * Rollback transaction.
	 */
	public static void rollbackTransaction() {
		Transaction tx = (Transaction) threadTransaction.get();
		try {
			threadTransaction.set(null);
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {

				tx.rollback();
			}
		} catch (HibernateException ex) {
			logger.warn(ex.getMessage());
		} finally {
			closeSession();
		}
	}
}
