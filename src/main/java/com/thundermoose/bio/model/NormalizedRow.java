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
  private String geneSymbol;
  private Map<Double, Float> data = new TreeMap<>();

  public NormalizedRow() {
  }

  public NormalizedRow(String plateName, String geneId, String geneSymbol) {
    this.plateName = plateName;
    this.geneId = geneId;
    this.geneSymbol = geneSymbol;
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

  public String getGeneSymbol() {
    return geneSymbol;
  }

  public void setGeneSymbol(String geneSymbol) {
    this.geneSymbol = geneSymbol;
  }

  public Map<Double, Float> getData() {
    return data;
  }

  public void setData(Map<Double, Float> data) {
    this.data = data;
  }
}
