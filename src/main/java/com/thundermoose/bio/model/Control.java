package com.thundermoose.bio.model;

import java.util.Date;

public class Control {

  private long id;
  private long plateId;
  private String geneId;
  private String geneSymbol;
  private double timeMarker;
  private float data;

  private Date createDate;

  public Control() {

  }

  public Control(long id, long plateId, String geneId, String geneSymbol, double timeMarker, float data, Date createDate) {
    this.id = id;
    this.plateId = plateId;
    this.geneId = geneId;
    this.geneSymbol = geneSymbol;
    this.timeMarker = timeMarker;
    this.data = data;
    this.createDate = createDate;
  }

  public Control(long plateId, String geneId, String geneSymbol, float data, Date createDate) {
    this.plateId = plateId;
    this.geneId = geneId;
    this.geneSymbol = geneSymbol;
    this.data = data;
    this.createDate = createDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getPlateId() {
    return plateId;
  }

  public void setPlateId(long plateId) {
    this.plateId = plateId;
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

  public double getTimeMarker() {
    return timeMarker;
  }

  public void setTimeMarker(double timeMarker) {
    this.timeMarker = timeMarker;
  }

  public float getData() {
    return data;
  }

  public void setData(float data) {
    this.data = data;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
