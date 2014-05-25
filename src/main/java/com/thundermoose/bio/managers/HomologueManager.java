package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.NcbiDao;
import com.thundermoose.bio.excel.ExcelDataReader;
import com.thundermoose.bio.model.HomologueData;
import com.thundermoose.bio.model.HomologueJoin;
import com.thundermoose.bio.model.HomologueSet;
import com.thundermoose.xlsx.StreamingReader;
import org.apache.commons.fileupload.FileItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thundermoose on 4/28/2014.
 */
@Component
public class HomologueManager {
  public static final Logger log = LoggerFactory.getLogger(HomologueManager.class);

  public static final List<XSSFColor> CELL_COLORS = new ArrayList<XSSFColor>() {
    {
      add(new XSSFColor(new Color(0xB1, 0x83, 0x6D)));
      add(new XSSFColor(new Color(0xB1, 0xAC, 0x6D)));
      add(new XSSFColor(new Color(0x96, 0xB1, 0x6D)));
      add(new XSSFColor(new Color(0x6D, 0xB1, 0x6D)));
      add(new XSSFColor(new Color(0x6D, 0xB1, 0x9E)));
      add(new XSSFColor(new Color(0x6D, 0x96, 0xB1)));
      add(new XSSFColor(new Color(0x6D, 0x70, 0xB1)));
      add(new XSSFColor(new Color(0x9E, 0x6D, 0xB1)));

    }
  };

  @Autowired
  private NcbiDao dao;
  @Autowired
  private NcbiManager ncbiManager;

  Map<String, String> homologues;
  boolean loading;

  public HomologueManager() {

  }

  synchronized void load() {
    if (ncbiManager.isRefreshing()) {
      throw new RuntimeException("NCBI data is refreshing, please wait for that to finish");
    }

    loading = true;
    homologues = DBMaker.newTempHashMap();
    log.debug("Loading homologue cache");
    dao.getHomologues(homologues);
    loading = false;
  }

  HomologueJoin intersection(List<HomologueSet> data) {
    HomologueJoin join = new HomologueJoin();
    join.setData(DBMaker.<List<HomologueData>>newTempHashSet());

    //use smallest set as lead
    List<HomologueSet> orderedData = new ArrayList<>(data);
    Collections.sort(orderedData, new Comparator<HomologueSet>() {
      @Override
      public int compare(HomologueSet o1, HomologueSet o2) {
        if (o1.getData().size() > o2.getData().size()) {
          return 1;
        } else if (o1.getData().size() < o2.getData().size()) {
          return -1;
        } else {
          return 0;
        }
      }
    });

    join.setFilenames(new ArrayList<String>());
    for (HomologueSet s : orderedData) {
      join.getFilenames().add(s.getFilename());
    }

    //find intersection
    HomologueSet lead = orderedData.get(0);
    for (HomologueData d : lead.getData().values()) {
      boolean intersect = true;
      List<HomologueData> l = new ArrayList<>();
      for (HomologueSet s : orderedData) {
        if (!s.equals(lead) && !s.getData().containsKey(d.getHomologueId())) {
          intersect = false;
          break;
        }
        l.add(s.getData().get(d.getHomologueId()));
      }

      if (intersect) {
        join.getData().add(l);
      }
    }

    return join;
  }

  HomologueSet readHomologueData(InputStream is) {
    if (homologues == null && !loading) {
      load();
    }

    try (StreamingReader reader = StreamingReader.builder()
            .bufferSize(4096)
            .rowCacheSize(100)
            .read(is)
    ) {
      Map<String, Integer> head = new HashMap<>();
      HomologueSet set = new HomologueSet();
      set.setData(DBMaker.<String, HomologueData>newTempHashMap());

      for (Row r : reader) {
        if (r.getCell(0) != null) {
          if (r.getRowNum() == 0) {
            for (Cell c : r) {
              head.put(c.getStringCellValue(), c.getColumnIndex());
            }
            continue;
          }

          int index = -1;
          try {
            HomologueData d = new HomologueData();

            index = head.get(ExcelDataReader.GENE_ID);
            d.setGeneId(r.getCell(index).getStringCellValue());

            index = head.get(ExcelDataReader.GENE_SYMBOL);
            d.setGeneSymbol(r.getCell(index).getStringCellValue());

            index = head.get(ExcelDataReader.DATA);
            d.setData((float) r.getCell(index).getNumericCellValue());

            d.setHomologueId(homologues.get(d.getGeneId()));
            set.getData().put(d.getHomologueId(), d);
          } catch (NullPointerException e) {
            e.printStackTrace();
            log.debug("empty value at " + CellReference.convertNumToColString(index) + (r.getRowNum() + 1));
          }
        }
      }

      return set;
    }
  }

  public HomologueJoin findMatches(List<FileItem> files) throws IOException {
    List<HomologueSet> data = new ArrayList<>();
    for (FileItem fi : files) {
      HomologueSet set = readHomologueData(fi.getInputStream());
      set.setFilename(fi.getName());
      data.add(set);
    }

    return intersection(data);
  }

  public void findMatchesExcel(List<FileItem> files, OutputStream out) throws IOException {
    HomologueJoin set = findMatches(files);
    if (set.getData().size() == 0) {
      return;
    }

    SXSSFWorkbook wb = new SXSSFWorkbook();
    Sheet sheet = wb.createSheet();

    Row fileHeader = sheet.createRow(0);
    fileHeader.createCell(0);
    Row colHeader = sheet.createRow(1);
    colHeader.createCell(0).setCellValue("Homologue ID");

    //find out how many headers to add
    int headerSize = set.getData().iterator().next().size();
    for (int i = 0; i < headerSize; i++) {
      int index = 1 + i * 3;
      CellRangeAddress range = new CellRangeAddress(0, 0, index, index + 2);
      sheet.addMergedRegion(range);

      Cell c = fileHeader.createCell(index);
      c.setCellValue(set.getFilenames().get(i));

      XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
      style.setFillForegroundColor(CELL_COLORS.get(i));
      style.setFillPattern(CellStyle.SOLID_FOREGROUND);
      style.setAlignment(HorizontalAlignment.CENTER);
      c.setCellStyle(style);

      colHeader.createCell(colHeader.getLastCellNum()).setCellValue("Gene ID");
      colHeader.createCell(colHeader.getLastCellNum()).setCellValue("Gene Symbol");
      colHeader.createCell(colHeader.getLastCellNum()).setCellValue("Data");
    }

    int row = 2;
    for (List<HomologueData> list : set.getData()) {
      Row r = sheet.createRow(row++);
      Cell homo = r.createCell(0);
      for (HomologueData d : list) {
        homo.setCellValue(d.getHomologueId());
        r.createCell(r.getLastCellNum()).setCellValue(d.getGeneId());
        r.createCell(r.getLastCellNum()).setCellValue(d.getGeneSymbol());
        r.createCell(r.getLastCellNum()).setCellValue(d.getData());
      }
    }

    wb.write(out);
  }

}
