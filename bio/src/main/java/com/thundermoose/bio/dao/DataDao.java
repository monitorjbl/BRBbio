package com.thundermoose.bio.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thundermoose.bio.model.RawData;

public class DataDao {

	@Autowired
	private SessionFactory	sessionFactory;

	public List<RawData> getData(String plateId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from RawData where plateId = :id ");
		query.setParameter("id", plateId);
		return query.list();
	}
}
