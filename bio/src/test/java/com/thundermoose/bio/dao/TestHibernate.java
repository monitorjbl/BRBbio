package com.thundermoose.bio.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("/application-context.xml")
public class TestHibernate {

	private static DataDao	dao;

	@Test
	public void test() {
		// System.out.println(dao.getDataByPlate("HTB1582"));
	}

	@Test
	public void testFile() throws FileNotFoundException {
		dao.loadExcel("test", new FileInputStream(new File("src/test/resources/test_data.xlsx")));
	}

	@BeforeClass
	public static void createDao() throws SQLException, ClassNotFoundException, DataAccessException, IOException {
		// delete hsqldb files
		File[] toBeDeleted = new File("target").listFiles(new FileFilter() {
			public boolean accept(File theFile) {
				return theFile.isFile() && theFile.getName().startsWith("hts.");
			}
		});
		for (File deletableFile : toBeDeleted) {
			deletableFile.delete();
		}

		// create datasource (will be autocreated on connect)
		JDBCDataSource ds = new JDBCDataSource();
		ds.setDatabase("jdbc:hsqldb:file:target/hts");
		ds.setUser("sa");
		ds.setPassword("");

		// load schema
		JdbcTemplate db = new JdbcTemplate(ds);
		db.execute("SET DATABASE SQL SYNTAX MYS TRUE");
		JdbcTestUtils.executeSqlScript(db, new FileSystemResource("src/main/resources/schema/tables.sql"), false);

		// configure hibernate
		Configuration configuration = new Configuration();
		configuration.configure("test_hibernate.cfg.xml");
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		// create test hibernate factory
		dao = new DataDao();
		dao.setSessionFactory(sessionFactory);
	}
}
