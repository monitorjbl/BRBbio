package com.thundermoose.bio.dao;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Thundermoose on 3/30/2014.
 */
public class TestUtils {
  private static final Logger log = Logger.getLogger(TestUtils.class);

  public static JdbcDataSource getDataSource() {
    JdbcDataSource ds = new JdbcDataSource();
    ds.setURL("jdbc:h2:target/unittest");
    return ds;
  }

  public static void initDb() {
    // delete hsqldb files
    File[] toBeDeleted = new File("target").listFiles(new FileFilter() {
      public boolean accept(File theFile) {
        return theFile.isFile() && theFile.getName().startsWith("unittest.");
      }
    });
    for (File deletableFile : toBeDeleted) {
      deletableFile.delete();
    }

    // load schema
    JdbcTemplate db = new JdbcTemplate(getDataSource());
    JdbcTestUtils.executeSqlScript(db, new FileSystemResource("src/main/resources/schema/tables.sql"), false);
    JdbcTestUtils.executeSqlScript(db, new FileSystemResource("src/main/resources/schema/h2-functions.sql"), false);
    JdbcTestUtils.executeSqlScript(db, new FileSystemResource("src/main/resources/schema/base-data.sql"), false);

    log.info("Test DB initialized");
  }
}
