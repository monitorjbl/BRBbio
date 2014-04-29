package com.thundermoose.bio.model;

import java.util.Date;

public class RawData {
  private long id;
  private long plateId;
  private String geneId;
  private String geneSymbol;
  private double timeMarker;
  private float data;
  private Date createDate;

  public RawData() {

  }

  public RawData(long plateId, String geneId, String geneSymbol, double timeMarker, float data) {
    this.plateId = plateId;
    this.geneId = geneId;
    this.geneSymbol = geneSymbol;
    this.timeMarker = timeMarker;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RawData rawData = (RawData) o;

    if (Float.compare(rawData.data, data) != 0) return false;
    if (id != rawData.id) return false;
    if (plateId != rawData.plateId) return false;
    if (Double.compare(rawData.timeMarker, timeMarker) != 0) return false;
    if (createDate != null ? !createDate.equals(rawData.createDate) : rawData.createDate != null) return false;
    if (geneId != null ? !geneId.equals(rawData.geneId) : rawData.geneId != null) return false;
    if (geneSymbol != null ? !geneSymbol.equals(rawData.geneSymbol) : rawData.geneSymbol != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (plateId ^ (plateId >>> 32));
    result = 31 * result + (geneId != null ? geneId.hashCode() : 0);
    result = 31 * result + (geneSymbol != null ? geneSymbol.hashCode() : 0);
    temp = Double.doubleToLongBits(timeMarker);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (data != +0.0f ? Float.floatToIntBits(data) : 0);
    result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
    return result;
  }
}
