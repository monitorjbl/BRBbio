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
  private NcbiDao dao;
  @Autowired
  private SystemDao systemDao;

  public List<Taxonomy> getTaxonomies() {
    return dao.getAllTaxonomies();
  }

  public Taxonomy getTaxonomy(String taxonomyId) {
    return dao.getTaxonomy(taxonomyId);
  }

  public List<Homologue> getHomologues(long runId, String taxonomyId) throws IOException {
    return dao.getHomologues(runId, taxonomyId);
  }

  public void getHomologuesExcel(long runId, String taxonomyId, OutputStream out) throws IOException {
    List<Homologue> ex = dao.getHomologues(runId, taxonomyId);
    @SuppressWarnings("serial")
    List<String> headers = new ArrayList<String>() {
      {
        add("Gene ID");
        add("Gene Symbol");
        add("Homologue Gene ID");
        add("Homologue Symbol");
      }
    };

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet();
    Row headerRow = sheet.createRow(0);

    //write headers
    for (String h : headers) {
      int in = headerRow.getLastCellNum();
      headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
    }

    for (Homologue dt : ex) {
      Row row = sheet.createRow(sheet.getLastRowNum() + 1);
      row.createCell(0).setCellValue(dt.getGeneId());
      row.createCell(1).setCellValue(dt.getGeneSymbol());
      row.createCell(2).setCellValue(dt.getHomologueId());
      row.createCell(3).setCellValue(dt.getHomologueSymbol());
    }

    wb.write(out);
  }

  public void getHomologuesTsv(long runId, String taxonomyId, OutputStream out) throws IOException {
    List<Homologue> ex = dao.getHomologues(runId, taxonomyId);

    StringBuilder tsv = new StringBuilder();
    tsv.append("Gene ID\tGene Symbol\tHomologue ID\tHomologue Symbol\n");

    for (Homologue dt : ex) {
      tsv.append(dt.getGeneId() + "\t" + dt.getGeneSymbol() + "\t" + dt.getHomologueId() + "\t" + dt.getHomologueSymbol() + "\n");
    }

    out.write(tsv.toString().getBytes());
  }

  public Date getLastLoadTime() {
    String val = systemDao.getSystemProperty(LOAD_PROPERTY);
    if (val != null) {
      return new Date(Long.parseLong(val));
    }
    return null;
  }

  public void refresh() throws IOException {
    systemDao.setSystemProperty(LOAD_PROPERTY, null);
    importManage.importTaxonomyData();
    importManage.importHomologueData();
    systemDao.setSystemProperty(LOAD_PROPERTY, Long.toString(System.currentTimeMillis()));
  }
}
