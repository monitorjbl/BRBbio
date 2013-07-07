package com.thundermoose.bio.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.hsqldb.jdbc.JDBCDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;


public class TestHibernate {

	private static DataDao	dao;

	@Test
	public void test() throws IOException {
		dao.getProcessedDataByRunId(0);
		/*List<Plate> plates = dao.getPlates();
		for(Plate plate : plates){
			System.out.println(plate.getPlateName());
			for(Control c : plate.getControls()){
				System.out.println("\t"+c.getNegativeControl());
			}
			for(RawData rd : plate.getRawData()){
				System.out.println("\t"+rd.getIdentifier());
			}
		}*/
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
		JdbcTestUtils.executeSqlScript(db, new FileSystemResource("src/main/resources/schema/views.sql"), false);

		// create test hibernate factory
		dao = new DataDao();
		dao.setJdbc(db);
		
		try{
			dao.loadExcel("test", new FileInputStream(new File("src/test/resources/test_data.xlsx")));
			System.out.println("data loaded");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}

	@AfterClass
	public static void shutdownDao() throws SQLException {
		DriverManager.getConnection("jdbc:hsqldb:file:target/hts;shutdown=true", "sa", "");
	}
}
