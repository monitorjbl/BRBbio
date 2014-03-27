package com.thundermoose.bio.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tayjones on 3/16/14.
 */
public class NormalizedRow {

  private String plateName;
  private String geneId;
  private Map<Double, Float> data = new TreeMap<Double, Float>(new Comparator<Double>() {
    @Override
    public int compare(Double o1, Double o2) {
      return (int) (o1 - o2);
    }
  });

  public NormalizedRow() {
  }

  public NormalizedRow(String plateName, String geneId) {
    this.plateName = plateName;
    this.geneId = geneId;
  }

  public String getPlateName() {
    return plateName;
  }

  public void setPlateName(String plateName) {
    this.plateName = plateName;
  }

  public String getGeneId() {
    return geneId;
  }

  public void setGeneId(String geneId) {
    this.geneId = geneId;
  }

  public Map<Double, Float> getData() {
    return data;
  }

  public void setData(Map<Double, Float> data) {
    this.data = data;
  }
}
