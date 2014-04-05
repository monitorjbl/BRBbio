package com.thundermoose.bio.model;

import java.util.Date;

public class ViabilityData {
  private long id;
  private long plateId;
  private String geneId;
  private String geneSymbol;
  private float data;
  private Date createDate;

  public ViabilityData() {

  }

  public ViabilityData(long plateId, String geneId, String geneSymbol, float data) {
    this.plateId = plateId;
    this.geneId = geneId;
    this.geneSymbol = geneSymbol;
    this.data = data;
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

  public String getGeneSymbol() {
    return geneSymbol;
  }

  public String getGeneId() {
    return geneId;
  }

  public void setGeneId(String geneId) {
    this.geneId = geneId;
  }

  public void setGeneSymbol(String geneSymbol) {
    this.geneSymbol = geneSymbol;
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
