package com.thundermoose.bio.context;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by Thundermoose on 3/30/2014.
 */
public class DataSourceRouter extends AbstractRoutingDataSource {
  private static String holder;

  @Override
  protected Object determineCurrentLookupKey() {
    return holder;
  }

  //allow setting this only once
  public static void setDataSourceKey(String key) {
    if (holder == null) {
      holder = key;
    }
  }

  public static String getDataSourceKey() {
    return holder;
  }
}
