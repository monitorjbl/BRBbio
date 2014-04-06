package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.model.NormalizedData;
import com.thundermoose.bio.util.Utils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thundermoose on 4/6/2014.
 */
public class ExcelExportManager {

  @Autowired
  DataDao dao;

  public void exportViabilityData(String username, long runId, String function) {
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

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_viability.xlsx\"");

  }
}
