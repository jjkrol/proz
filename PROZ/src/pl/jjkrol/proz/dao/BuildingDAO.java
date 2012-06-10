package pl.jjkrol.proz.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.Building;
import pl.jjkrol.proz.model.HibernateUtil;

public class BuildingDAO {
	static Logger logger = Logger.getLogger(BuildingDAO.class);
public BuildingDAO() {
	HibernateUtil.beginTransaction();
}
public Building getBuildingById(int id) {
	Session session = HibernateUtil.getSession();
	Building building = null;
	try {
//		session.createQuery("from Building").list();
		building = (Building) session.load(Building.class, id);
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
	return building;
}
public List<Building> getAllBuildings(){
	Session session = HibernateUtil.getSession();
	List<Building> buildings = null;
	try {
		buildings = session.createQuery("from Building").list();
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
	return buildings;
}		

}
