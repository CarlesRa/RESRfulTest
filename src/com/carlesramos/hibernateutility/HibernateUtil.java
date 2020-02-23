package com.carlesramos.hibernateutility;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	private static final SessionFactory sessionFactory = buildSessionFactory();
	private static SessionFactory buildSessionFactory() {
		try {
		
			return new Configuration().configure().buildSessionFactory(new StandardServiceRegistryBuilder().
					configure().build() );
		}catch (Throwable ex) {

			throw new ExceptionInInitializerError(ex);
		}  
		}  
		public static SessionFactory getSessionFactory() {
			return sessionFactory; 
		}
		
		/*
		 * Configuration configuration = new Configuration().configure();
StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().
applySettings(configuration.getProperties());
SessionFactory factory = configuration.buildSessionFactory(builder.build());
		 * */
}


