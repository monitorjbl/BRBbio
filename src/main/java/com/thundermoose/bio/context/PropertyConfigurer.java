package com.thundermoose.bio.context;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * Created by Thundermoose on 4/7/2014.
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {

  @Override
  protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
    //dont want to write into bin directory of servlet container
    if (placeholder.equals("embedded.dataPath") && props.get("embedded.dataPath") == null) {
      return System.getProperty("java.io.tmpdir") + "/hts";
    } else {
      return super.resolvePlaceholder(placeholder, props, systemPropertiesMode);
    }
  }

}
