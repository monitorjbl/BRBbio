package com.thundermoose.bio.model;

/**
 * Created by Thundermoose on 4/6/2014.
 */
public class Taxonomy {
  private String id;
  private String name;

  public Taxonomy() {
  }

  public Taxonomy(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
