package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.NcbiDao;
import com.thundermoose.bio.dao.SystemDao;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.NormalizedData;
import com.thundermoose.bio.model.NormalizedRow;
import com.thundermoose.bio.model.Taxonomy;
import com.thundermoose.bio.util.Utils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Component
public class NcbiManager {
  public static final String LOAD_PROPERTY = "NCBI_LAST_LOAD_TIME";

  @Autowired
  NcbiImportManager importManage;
  @Autowired
  HomologueManager homologueManager;
  @Autowired
  private NcbiDao dao;
  @Autowired
  private SystemDao systemDao;

  private boolean refreshing;

  public List<Taxonomy> getTaxonomies() {
    return dao.getAllTaxonomies();
  }

  public Taxonomy getTaxonomy(String taxonomyId) {
    return dao.getTaxonomy(taxonomyId);
  }

  public Date getLastLoadTime() {
    String val = systemDao.getSystemProperty(LOAD_PROPERTY);
    if (val != null) {
      return new Date(Long.parseLong(val));
    }
    return null;
  }

  public boolean isRefreshing() {
    return refreshing;
  }

  public synchronized void refresh() throws IOException {
    refreshing = true;
    systemDao.setSystemProperty(LOAD_PROPERTY, null);
    importManage.importTaxonomyData();
    importManage.importHomologueData();
    systemDao.setSystemProperty(LOAD_PROPERTY, Long.toString(System.currentTimeMillis()));
    refreshing = false;
  }
}
