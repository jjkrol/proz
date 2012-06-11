package pl.jjkrol.proz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.HibernateUtil;
import pl.jjkrol.proz.model.Occupant;
import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class OccupantDAO.
 */
public class OccupantDAO {

	/** The logger. */
	static Logger logger = Logger.getLogger(OccupantDAO.class);

	/**
	 * Instantiates a new occupant dao.
	 */
	public OccupantDAO() {
		HibernateUtil.beginTransaction();
	}

	/**
	 * Gets the occupant by id.
	 * 
	 * @param id
	 *            the id
	 * @return the occupant by id
	 */
	public Occupant getOccupantById(final int id) {
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getSession();
		Occupant occupant = null;
		try {
			occupant = (Occupant) session.load(Occupant.class, id);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return occupant;
	}

	/**
	 * Gets the all occupants.
	 * 
	 * @return the all occupants
	 */
	public List<Occupant> getAllOccupants() {
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getSession();
		List<Occupant> occupants = null;
		try {
			occupants = session.createQuery("from Occupant").list();
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return occupants;
	}

	/**
	 * Delete.
	 * 
	 * @param occupant
	 *            the occupant
	 */
	public void delete(final Occupant occupant) {
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getSession();
		try {
			session.delete(occupant);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Delete by id.
	 * 
	 * @param id
	 *            the id
	 */
	public void deleteById(final int id) {
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getSession();
		try {
			Occupant tempOccupant = (Occupant) session.load(Occupant.class, id);
			session.delete(tempOccupant);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
	}

	/**
	 * Save or update.
	 * 
	 * @param occupant
	 *            the occupant
	 */
	public void saveOrUpdate(final Occupant occupant) {
		HibernateUtil.beginTransaction();
		Session session = HibernateUtil.getSession();
		try {
			session.saveOrUpdate(occupant);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
	}

}
