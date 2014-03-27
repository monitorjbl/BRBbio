package com.thundermoose.bio.model;

public class ZFactor {

  private String plateName;
  private double timeMarker;
  private float zFactor;

  public ZFactor() {

  }

  public ZFactor(String plateName, double timeMarker, float zFactor) {
    this.plateName = plateName;
    this.timeMarker = timeMarker;
    this.zFactor = zFactor;
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

  public float getzFactor() {
    return zFactor;
  }

  public void setzFactor(float zFactor) {
    this.zFactor = zFactor;
  }

}
