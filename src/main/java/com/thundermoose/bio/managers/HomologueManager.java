package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.NcbiDao;
import com.thundermoose.bio.excel.ExcelDataReader;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.HomologueData;
import com.thundermoose.xlsx.StreamingReader;
import org.apache.commons.fileupload.FileItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Created by Thundermoose on 4/28/2014.
 */
@Component
public class HomologueManager {
  public static final Logger log = LoggerFactory.getLogger(HomologueManager.class);

  @Autowired
  private NcbiDao dao;
  Map<String, String> homologues;

  public HomologueManager() {

  }

  void load() {
    homologues = DBMaker.newTempHashMap();
    dao.getHomologues(homologues);
  }

  Set<List<HomologueData>> intersection(List<Map<HomologueData, HomologueData>> data) {
    Set<List<HomologueData>> in = DBMaker.newTempHashSet();

    //use smallest set as lead
    Map<HomologueData, HomologueData> lead = null;
    for (Map<HomologueData, HomologueData> s : data) {
      if (lead == null || lead.size() > s.size()) {
        lead = s;
      }
    }

    //find intersection
    for (HomologueData d : lead.values()) {
      boolean intersect = true;
      List<HomologueData> l = new ArrayList<>();
      for (Map<HomologueData, HomologueData> s : data) {
        if (!s.equals(lead) && !s.containsKey(d)) {
          intersect = false;
          break;
        }
        l.add(s.get(d));
      }

      if (intersect) {
        in.add(l);
      }
    }

    return in;
  }

  Map<HomologueData, HomologueData> readHomologueData(InputStream is) {
    try (StreamingReader reader = StreamingReader.builder()
            .bufferSize(4096)
            .rowCacheSize(100)
            .read(is)
    ) {
      Map<String, Integer> head = new HashMap<>();
      Map<HomologueData, HomologueData> data = DBMaker.newTempHashMap();

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
            data.put(d, d);
          } catch (NullPointerException e) {
            log.debug("empty value at " + CellReference.convertNumToColString(index) + (r.getRowNum() + 1));
          }
        }
      }

      return data;
    }
  }

  public Set<List<HomologueData>> findMatches(List<FileItem> files) throws IOException {
    List<Map<HomologueData, HomologueData>> data = new ArrayList<>();
    for (FileItem fi : files) {
      data.add(readHomologueData(fi.getInputStream()));
    }

    return intersection(data);
  }

}
