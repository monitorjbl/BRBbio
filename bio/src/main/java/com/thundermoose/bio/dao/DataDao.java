package com.thundermoose.bio.dao;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thundermoose.bio.excel.RawDataReader;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.RawData;

public class DataDao {

	@Autowired
	private SessionFactory	sessionFactory;

	@SuppressWarnings("unchecked")
	public List<Plate> getDataByPlate(long plateId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Plate WHERE id = :id ");
		query.setParameter("id", plateId);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Plate> getPlates() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Plate");
		return query.list();
	}

	public void loadExcel(String runName, InputStream is){
		try {
			new RawDataReader("negativecontrol", "Copb1_indi", sessionFactory.openSession()).readExcel(runName,is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
