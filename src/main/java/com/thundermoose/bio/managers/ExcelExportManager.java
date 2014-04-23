package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.model.NormalizedData;
import com.thundermoose.bio.model.NormalizedRow;
import com.thundermoose.bio.model.ZFactor;
import com.thundermoose.bio.util.Utils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Component
public class ExcelExportManager {

  @Autowired
  DataDao dao;

  public void exportNormalizedData(String username, long runId, String function, OutputStream out) throws IOException {
    List<NormalizedRow> ex = dao.getNormalizedRowDataByRunId(runId, username, function);
    @SuppressWarnings("serial")
    List<String> headers = new ArrayList<String>() {
      {
        add("Plate Name");
        add("Entrez Gene ID");
        add("Gene Symbol");
      }
    };

    List<Double> markers = dao.getTimeMarkers(runId, Utils.getCurrentUsername());
    for (Double d : markers) {
      headers.add(d + "hr");
    }

    Workbook wb = new SXSSFWorkbook();
    Sheet sheet = wb.createSheet();
    Row headerRow = sheet.createRow(0);

    //write headers
    for (String h : headers) {
      int in = headerRow.getLastCellNum();
      headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
    }

    for (NormalizedRow dt : ex) {
      Row row = sheet.createRow(sheet.getLastRowNum() + 1);
      row.createCell(0).setCellValue(dt.getPlateName());
      row.createCell(1).setCellValue(dt.getGeneId());
      row.createCell(2).setCellValue(dt.getGeneSymbol());
      for (Double d : markers) {
        if (dt.getData().get(d) != null) {
          row.createCell(row.getLastCellNum()).setCellValue(dt.getData().get(d));
        } else {
          row.createCell(row.getLastCellNum());
        }
      }
    }

    wb.write(out);
  }

  public void exportZFactorData(String username, long runId, String function, OutputStream out) throws IOException {
    List<ZFactor> ex = dao.getZFactorsByRunId(runId, username, function);
    @SuppressWarnings("serial")
    List<String> headers = new ArrayList<String>() {
      {
        add("Plate Name");
      }
    };
    Map<String, Row> rowmap = new HashMap<String, Row>();

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet();
    Row headerRow = sheet.createRow(0);

    for (ZFactor dt : ex) {
      if (!headers.contains(dt.getTimeMarker() + "hr")) {
        headers.add(dt.getTimeMarker() + "hr");
      }

      String key = dt.getPlateName();
      if (!rowmap.containsKey(key)) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0).setCellValue(dt.getPlateName());
        rowmap.put(key, row);
      }
      Row row = rowmap.get(key);
      row.createCell(row.getLastCellNum()).setCellValue(dt.getzFactor());
    }

    for (String h : headers) {
      int in = headerRow.getLastCellNum();
      headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
    }

    wb.write(out);
  }

  public void exportViabilityData(String username, long runId, String function, OutputStream out) throws IOException {
    List<NormalizedData> ex = dao.getViabilityByRunId(runId, username, function);

    @SuppressWarnings("serial")
    List<String> headers = new ArrayList<String>() {
      {
        add("Plate Name");
        add("Entrez Gene ID");
        add("Gene Symbol");
        add("Viability");
      }
    };

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet();
    Row headerRow = sheet.createRow(0);

    for (NormalizedData dt : ex) {
      Row row = sheet.createRow(sheet.getLastRowNum() + 1);
      row.createCell(0).setCellValue(dt.getPlateName());
      row.createCell(1).setCellValue(dt.getGeneId());
      row.createCell(2).setCellValue(dt.getGeneSymbol());
      row.createCell(3).setCellValue(dt.getNormalized());
    }

    for (String h : headers) {
      int in = headerRow.getLastCellNum();
      headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
    }

    wb.write(out);
  }
}
