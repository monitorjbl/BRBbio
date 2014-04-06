package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.model.NormalizedData;
import com.thundermoose.bio.model.ZFactor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Component
public class TsvExportManager {

  @Autowired
  DataDao dao;

  public void exportNormalizedData(String username, long runId, String function, OutputStream out) throws IOException {
    List<NormalizedData> ex = dao.getNormalizedDataByRunId(runId, username, function);

    String headers = "Plate Name\tEntrez Gene ID\tGene Symbol\t";
    Map<String, String> rowmap = new TreeMap<String, String>();

    for (NormalizedData dt : ex) {
      if (!headers.contains(dt.getTimeMarker() + "hr")) {
        headers += dt.getTimeMarker() + "hr\t";
      }

      String key = dt.getPlateName() + "_" + dt.getGeneSymbol();
      if (!rowmap.containsKey(key)) {
        String row = dt.getPlateName() + "\t" + dt.getGeneId() + "\t" + dt.getGeneSymbol() + "\t";
        rowmap.put(key, row);
      }
      rowmap.put(key, rowmap.get(key) + dt.getNormalized() + "\t");
    }

    StringBuilder tsv = new StringBuilder();
    tsv.append(headers + "\n");
    for (String s : rowmap.keySet()) {
      tsv.append(rowmap.get(s) + "\n");
    }

    out.write(tsv.toString().getBytes());
  }

  public void exportZFactorData(String username, long runId, String function, OutputStream out) throws IOException {
    List<ZFactor> ex = dao.getZFactorsByRunId(runId, username, function);

    String headers = "Plate Name\t";
    Map<String, String> rowmap = new TreeMap<String, String>();

    for (ZFactor dt : ex) {
      if (!headers.contains(dt.getTimeMarker() + "hr")) {
        headers += dt.getTimeMarker() + "hr\t";
      }

      String key = dt.getPlateName();
      if (!rowmap.containsKey(key)) {
        String row = dt.getPlateName() + "\t";
        rowmap.put(key, row);
      }
      rowmap.put(key, rowmap.get(key) + dt.getzFactor() + "\t");
    }

    StringBuilder tsv = new StringBuilder();
    tsv.append(headers + "\n");
    for (String s : rowmap.keySet()) {
      tsv.append(rowmap.get(s) + "\n");
    }

    out.write(tsv.toString().getBytes());
  }

  public void exportViabilityData(String username, long runId, String function, OutputStream out) throws IOException {
    List<NormalizedData> ex = dao.getViabilityByRunId(runId, username, function);

    String headers = "Plate Name\tGene\tViability";
    Map<String, String> rowmap = new TreeMap<String, String>();

    for (NormalizedData dt : ex) {
      String key = dt.getPlateName() + "_" + dt.getGeneSymbol();
      if (!rowmap.containsKey(key)) {
        String row = dt.getPlateName() + "\t" + dt.getGeneId() + "\t" + dt.getGeneSymbol() + "\t";
        rowmap.put(key, row);
      }
      rowmap.put(key, rowmap.get(key) + dt.getNormalized() + "\t");
    }

    StringBuilder tsv = new StringBuilder();
    tsv.append(headers + "\n");
    for (String s : rowmap.keySet()) {
      tsv.append(rowmap.get(s) + "\n");
    }

    out.write(tsv.toString().getBytes());
  }
}
