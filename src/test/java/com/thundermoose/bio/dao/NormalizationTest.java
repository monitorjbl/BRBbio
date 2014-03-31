package com.thundermoose.bio.dao;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.thundermoose.bio.model.NormalizedData;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import static org.junit.Assert.*;

public class NormalizationTest {
  private static final Logger log = Logger.getLogger(NormalizationTest.class);
  private static DataDao dao;

  @Test
  public void testNormalizedData() throws IOException {
    List<NormalizedData> data = dao.getNormalizedDataByRunId(1, "admin", "(rawData/AVG(Copb1_indi))/(AVG(negativecontrol)/AVG(Copb1_indi))");

    NormalizedData agk = Iterables.find(data, new Predicate<NormalizedData>() {
      @Override
      public boolean apply(NormalizedData d) {
        return d.getGeneId().equals("Agk") && d.getTimeMarker() == 0;
      }
    });
    assertNotNull(agk);
    assertEquals(0.95384914, round(agk.getNormalized()), 0.0);
  }

  public static double round(double val) {
    return new BigDecimal(val).setScale(8, BigDecimal.ROUND_HALF_UP).round(MathContext.DECIMAL64).doubleValue();
  }

  @BeforeClass
  public static void createDao() throws SQLException, ClassNotFoundException, DataAccessException, IOException {
    TestUtils.initDb();

    // load test data
    dao = new DataDao();
    dao.setJdbc(new JdbcTemplate(TestUtils.getDataSource()));
    dao.loadRawDataExcel("admin", "Test-Run", new ArrayList<String>() {
      {
        add("negativecontrol");
        add("Copb1_indi");
      }
    }, new FileInputStream(new File("src/test/resources/test_data.xlsx")));
    log.info("data loaded");

  }
}
