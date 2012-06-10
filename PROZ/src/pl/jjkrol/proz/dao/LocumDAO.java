package pl.jjkrol.proz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.HibernateUtil;
import pl.jjkrol.proz.model.Locum;
import org.apache.log4j.Logger;

public class LocumDAO {
	
	static Logger logger = Logger.getLogger(LocumDAO.class);
	
	public LocumDAO() {
		HibernateUtil.beginTransaction();
	}
	
	public Locum getLocumByName(String name) {
		Session session = HibernateUtil.getSession();
		Locum locum = null;
		try {
			locum = (Locum) session.load(Locum.class, name);
		}catch(HibernateException e){
			logger.warn(e.getMessage());
		}
		return locum;
	}
	
	public List<Locum> getAllLocums(){
		Session session = HibernateUtil.getSession();
		List<Locum> locums = null;
		try {
			locums = session.createQuery("from Locum").list();
		}catch(HibernateException e){
			logger.warn(e.getMessage());
		}
		return locums;
	}

}
