package com.thundermoose.bio.context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thundermoose on 3/30/14.
 */
public class Listener implements ApplicationListener<ContextRefreshedEvent> {
  Logger log = Logger.getLogger(Listener.class);

  @Value("${embedded.use:true}")
  boolean useEmbedded;
  @Autowired
  @Qualifier("dataSource")
  DataSource ds;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Context refresh");
    if (DataSourceRouter.getDataSourceKey() == null) {
      if (useEmbedded) {
        DataSourceRouter.setDataSourceKey("embedded");
      } else {
        DataSourceRouter.setDataSourceKey("standalone");
      }
      log.info("Using " + DataSourceRouter.getDataSourceKey() + " database");

      try {
        prepDatabase();
      } catch (MetaDataAccessException e) {
        throw new RuntimeException(e);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }

  }

  private void prepDatabase() throws MetaDataAccessException, SQLException {
    List<String> tables = new ArrayList<String>();
    Connection c = ds.getConnection();
    try {
      DatabaseMetaData md = c.getMetaData();
      ResultSet rs = md.getTables(null, null, "%", null);
      while (rs.next()) {
        tables.add(rs.getString(3).toLowerCase());
      }
    } finally {
      c.close();
    }

    if (!tables.contains("hts_version_info")) {
      log.info("Database is uninitialized, creating schema. Default user is admin/admin.");

      JdbcTestUtils.executeSqlScript(new JdbcTemplate(ds),  new ClassPathResource("schema/tables.sql"), true);
      if(DataSourceRouter.getDataSourceKey().equals("embedded")){
        JdbcTestUtils.executeSqlScript(new JdbcTemplate(ds),  new ClassPathResource("schema/h2-functions.sql"), true);
      }
      JdbcTestUtils.executeSqlScript(new JdbcTemplate(ds), new ClassPathResource("schema/base-data.sql"), true);
    } else {
      log.debug("Found tables: "+tables);
    }
  }
}