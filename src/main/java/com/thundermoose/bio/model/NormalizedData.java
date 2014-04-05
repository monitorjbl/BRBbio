package com.thundermoose.bio.model;

public class NormalizedData {

  private String plateName;
  private double timeMarker;
  private String geneId;
  private String geneSymbol;
  private float normalized;

  public NormalizedData() {

  }

  public NormalizedData(String plateName, String geneId, String geneSymbol, double timeMarker, float normalized) {
    this.plateName = plateName;
    this.geneId = geneId;
    this.geneSymbol = geneSymbol;
    this.timeMarker = timeMarker;
    this.normalized = normalized;
  }

  public String getPlateName() {
    return plateName;
  }

  public void setPlateName(String plateName) {
    this.plateName = plateName;
  }

  public double getTimeMarker() {
    return timeMarker;
  }

  public void setTimeMarker(double timeMarker) {
    this.timeMarker = timeMarker;
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

  public float getNormalized() {
    return normalized;
  }

  public void setNormalized(float normalized) {
    this.normalized = normalized;
  }

}
