package com.thundermoose.bio.model;

/**
 * Created by Thundermoose on 4/6/2014.
 */
public class Homologue {
  private String geneId;
  private String geneSymbol;
  private String homologueId;
  private String homologueSymbol;

  public Homologue() {
  }

  public Homologue(String geneId, String geneSymbol, String homologueId, String homologueSymbol) {
    this.geneId = geneId;
    this.geneSymbol = geneSymbol;
    this.homologueId = homologueId;
    this.homologueSymbol = homologueSymbol;
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

  public String getHomologueId() {
    return homologueId;
  }

  public void setHomologueId(String homologueId) {
    this.homologueId = homologueId;
  }

  public String getHomologueSymbol() {
    return homologueSymbol;
  }

  public void setHomologueSymbol(String homologueSymbol) {
    this.homologueSymbol = homologueSymbol;
  }
}
