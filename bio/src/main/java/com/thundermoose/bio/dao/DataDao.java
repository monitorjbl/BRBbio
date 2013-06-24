package com.thundermoose.bio.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.google.common.io.Resources;
import com.thundermoose.bio.excel.RawDataReader;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.ProcessedData;
import com.thundermoose.bio.model.Run;

public class DataDao {

	@Autowired
	private SessionFactory	sessionFactory;

	@Autowired
	private JdbcTemplate		jdbc;

	@SuppressWarnings("unchecked")
	public List<Plate> getPlates() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Plate");
		return query.list();
	}

	public Plate getPlateById(long plateId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Plate WHERE id = :id ");
		query.setParameter("id", plateId);
		return (Plate) query.uniqueResult();
	}

	public Run getRunById(long runId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Run WHERE id = :id ");
		query.setParameter("id", runId);
		return (Run) query.uniqueResult();
	}

	public List<ProcessedData> getProcessedDataByRunId(long runId) throws IOException {
		return jdbc.query(Resources.toString(Resources.getResource("sql/report.sql"), Charset.defaultCharset()), new Object[]{runId}, new ProcessedDataRowMapper());
	}

	public void loadExcel(String runName, InputStream is) {
		try {
			new RawDataReader("negativecontrol", "Copb1_indi", sessionFactory.openSession()).readExcel(runName, is);
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

	public JdbcTemplate getJdbc() {
		return jdbc;
	}

	public void setJdbc(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	private class ProcessedDataRowMapper implements RowMapper<ProcessedData>{

		public ProcessedData mapRow(ResultSet rs, int rownum) throws SQLException {
			return new ProcessedData();
		}
		
	}

}
