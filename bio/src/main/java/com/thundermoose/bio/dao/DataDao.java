package com.thundermoose.bio.dao;

import java.io.InputStream;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thundermoose.bio.excel.RawDataReader;
import com.thundermoose.bio.model.RawData;

public class DataDao {

	@Autowired
	private SessionFactory	sessionFactory;

	@SuppressWarnings("unchecked")
	public List<RawData> getDataByPlate(String plateId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from RawData where plateId = :id ");
		query.setParameter("id", plateId);
		return query.list();
	}

	public void loadExcel(InputStream is){
		try {
			new RawDataReader("negativecontrol", "Copb1_indi", sessionFactory.openSession()).readExcel(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
