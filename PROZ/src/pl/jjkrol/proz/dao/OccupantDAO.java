package pl.jjkrol.proz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.HibernateUtil;
import pl.jjkrol.proz.model.Occupant;
import org.apache.log4j.Logger;

public class OccupantDAO {
	static Logger logger = Logger.getLogger(OccupantDAO.class);
public OccupantDAO() {
	HibernateUtil.beginTransaction();
}
public Occupant getOccupantById(int id) {
		HibernateUtil.beginTransaction();
	Session session = HibernateUtil.getSession();
	Occupant occupant = null;
	try {
		occupant = (Occupant) session.load(Occupant.class, id);
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
	return occupant;
}

public List<Occupant> getAllOccupants(){
		HibernateUtil.beginTransaction();
	Session session = HibernateUtil.getSession();
	List<Occupant> occupants = null;
	try {
		occupants = session.createQuery("from Occupant").list();
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
	return occupants;
}
public void delete(Occupant occupant) {
		HibernateUtil.beginTransaction();
	Session session = HibernateUtil.getSession();
	try {
		session.delete(occupant);
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
}
public void deleteById(int id) {
		HibernateUtil.beginTransaction();
	Session session = HibernateUtil.getSession();
	try {
		Occupant tempOccupant = (Occupant) session.load(Occupant.class, id);
		session.delete(tempOccupant);
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
}
public void saveOrUpdate(Occupant occupant) {
		HibernateUtil.beginTransaction();
	Session session = HibernateUtil.getSession();
	try {
		session.saveOrUpdate(occupant);
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
}

}
