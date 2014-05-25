package com.thundermoose.bio.model;

import java.util.Map;

/**
 * Created by Thundermoose on 5/24/2014.
 */
public class HomologueSet {
  private String filename;
  private Map<String, HomologueData> data;

  public HomologueSet() {
  }

  public HomologueSet(String filename, Map<String, HomologueData> data) {
    this.filename = filename;
    this.data = data;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public Map<String, HomologueData> getData() {
    return data;
  }

  public void setData(Map<String, HomologueData> data) {
    this.data = data;
  }
}
