package pl.jjkrol.proz.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.Building;
import pl.jjkrol.proz.model.HibernateUtil;

/**
 * The Class BuildingDAO.
 */
public class BuildingDAO {

	/** The logger. */
	static Logger logger = Logger.getLogger(BuildingDAO.class);

	/**
	 * Instantiates a new building dao.
	 */
	public BuildingDAO() {
		HibernateUtil.beginTransaction();
	}

	/**
	 * Gets the building by id.
	 * 
	 * @param id
	 *            the id
	 * @return the building by id
	 */
	public Building getBuildingById(final int id) {
		Session session = HibernateUtil.getSession();
		Building building = null;
		try {
			// session.createQuery("from Building").list();
			building = (Building) session.load(Building.class, id);
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return building;
	}

	/**
	 * Gets the all buildings.
	 * 
	 * @return the all buildings
	 */
	public List<Building> getAllBuildings() {
		Session session = HibernateUtil.getSession();
		List<Building> buildings = null;
		try {
			buildings = session.createQuery("from Building").list();
		} catch (HibernateException e) {
			logger.warn(e.getMessage());
		}
		return buildings;
	}

}
