package pl.jjkrol.proz.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import pl.jjkrol.proz.model.HibernateUtil;
import pl.jjkrol.proz.model.Counter;
import org.apache.log4j.Logger;

public class CounterDAO {
	static Logger logger = Logger.getLogger(CounterDAO.class);
public CounterDAO() {
	HibernateUtil.beginTransaction();
}
public Counter getCounterById(int id) {
	Session session = HibernateUtil.getSession();
	Counter counter = null;
	try {
		counter = (Counter) session.load(Counter.class, id);
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
	return counter;
}

public List<Counter> getAllCounters(){
	Session session = HibernateUtil.getSession();
	List<Counter> counters = null;
	try {
		counters = session.createQuery("from Counter").list();
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
	return counters;
}

public void saveOrUpdate(Counter counter) {
	Session session = HibernateUtil.getSession();
	try {
		session.saveOrUpdate(counter);
	}catch(HibernateException e){
		logger.warn(e.getMessage());
	}
}

}
