package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.NcbiDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Component
public class NcbiImportManager {

  @Value("${homologue.taxonomy}")
  String taxonomyUrl;
  @Value("${homologue.data}")
  String homologueUrl;

  @Autowired
  NcbiDao dao;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void importTaxonomyData() throws IOException {
    dao.clearTaxonomyData();
    URL url = new URL(taxonomyUrl);
    try (
            InputStream is = url.openStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
    ) {
      String line;
      while ((line = bf.readLine()) != null) {
        String[] s = line.split("\t");
        dao.addTaxonomyData(s[0], s[1]);
      }
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void importHomologueData() throws IOException {
    dao.clearHomologueData();
    URL url = new URL(homologueUrl);
    try (
            InputStream is = url.openStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
    ) {
      String line;
      while ((line = bf.readLine()) != null) {
        String[] s = line.split("\t");
        dao.addHomologueData(s[0], s[1], s[2], s[3]);
      }
    }
  }
}
