package com.thundermoose.bio.model;

import java.util.List;
import java.util.Set;

/**
 * Created by Thundermoose on 5/24/2014.
 */
public class HomologueJoin {
  private List<String> filenames;
  private Set<List<HomologueData>> data;

  public List<String> getFilenames() {
    return filenames;
  }

  public void setFilenames(List<String> filenames) {
    this.filenames = filenames;
  }

  public Set<List<HomologueData>> getData() {
    return data;
  }

  public void setData(Set<List<HomologueData>> data) {
    this.data = data;
  }
}
