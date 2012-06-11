package pl.jjkrol.proz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.HibernateUtil;
import pl.jjkrol.proz.model.Counter;
import org.apache.log4j.Logger;

/**
 * The Class CounterDAO.
 */
public class CounterDAO {

	/** The logger. */
	static Logger logger = Logger.getLogger(CounterDAO.class);

	/**
	 * Instantiates a new counter dao.
	 */
	public CounterDAO() {
		HibernateUtil.beginTransaction();
	}

	/**
	 * Gets the counter by id.
	 * 
	 * @param id
	 *            the id
	 * @return the counter by id
	 */
	public Counter getCounterById(final int id) {
		Session session = HibernateUtil.getSession();
		Counter counter = null;
		try {
			counter = (Counter) session.load(Counter.class, id);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return counter;
	}

	/**
	 * Gets the all counters.
	 * 
	 * @return the all counters
	 */
	public List<Counter> getAllCounters() {
		Session session = HibernateUtil.getSession();
		List<Counter> counters = null;
		try {
			counters = session.createQuery("from Counter").list();
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return counters;
	}

	/**
	 * Save or update.
	 * 
	 * @param counter
	 *            the counter
	 */
	public void saveOrUpdate(final Counter counter) {
		Session session = HibernateUtil.getSession();
		try {
			session.saveOrUpdate(counter);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
	}

}
