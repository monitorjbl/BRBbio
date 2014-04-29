package com.thundermoose.bio.model;

import java.io.Serializable;

/**
 * Created by Thundermoose on 4/28/2014.
 */
public class HomologueData implements Serializable {
  private String geneId;
  private String geneSymbol;
  private String homologueId;
  private float data;

  public HomologueData() {
  }

  public HomologueData(String geneId, String homologueId, float data) {
    this.geneId = geneId;
    this.homologueId = homologueId;
    this.data = data;
  }

  public String getGeneId() {
    return geneId;
  }

  public void setGeneId(String geneId) {
    this.geneId = geneId;
  }

  public String getHomologueId() {
    return homologueId;
  }

  public void setHomologueId(String homologueId) {
    this.homologueId = homologueId;
  }

  public String getGeneSymbol() {
    return geneSymbol;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HomologueData that = (HomologueData) o;

    if (homologueId != null ? !homologueId.equalsIgnoreCase(that.homologueId) : that.homologueId != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return homologueId != null ? homologueId.toLowerCase().hashCode() : 0;
  }

  @Override
  public String toString() {
    return "HomologueData{" +
            "geneId='" + geneId + '\'' +
            ", homologueId='" + homologueId + '\'' +
            ", data=" + data +
            '}';
  }
}
