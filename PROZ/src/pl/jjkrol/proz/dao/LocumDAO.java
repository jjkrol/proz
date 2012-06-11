package pl.jjkrol.proz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.HibernateUtil;
import pl.jjkrol.proz.model.Locum;
import org.apache.log4j.Logger;

/**
 * The Class LocumDAO.
 */
public class LocumDAO {

	/** The logger. */
	static Logger logger = Logger.getLogger(LocumDAO.class);

	/**
	 * Instantiates a new locum dao.
	 */
	public LocumDAO() {
		HibernateUtil.beginTransaction();
	}

	/**
	 * Gets the locum by name.
	 * 
	 * @param name
	 *            the name
	 * @return the locum by name
	 */
	public Locum getLocumByName(final String name) {
		Session session = HibernateUtil.getSession();
		Locum locum = null;
		try {
			locum = (Locum) session.load(Locum.class, name);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return locum;
	}

	/**
	 * Gets the all locums.
	 * 
	 * @return the all locums
	 */
	public List<Locum> getAllLocums() {
		Session session = HibernateUtil.getSession();
		List<Locum> locums = null;
		try {
			locums = session.createQuery("from Locum").list();
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return locums;
	}

}
