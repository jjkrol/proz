package pl.jjkrol.proz.model;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.apache.log4j.Logger;

public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	static Logger logger = Logger.getLogger(HibernateUtil.class);
	public static SessionFactory configureSessionFactory() throws HibernateException {
		try {
	    Configuration cfg = new Configuration();
	    cfg.configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();        
	    sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		}catch(NoClassDefFoundError e) {
			logger.debug(e.getMessage());
		}
	    return sessionFactory;
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
