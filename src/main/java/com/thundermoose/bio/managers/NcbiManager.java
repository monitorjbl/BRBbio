package com.thundermoose.bio.managers;

import com.thundermoose.bio.dao.NcbiDao;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.Taxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Component
public class NcbiManager {
  @Autowired
  private NcbiDao dao;

  public List<Taxonomy> getTaxonomies() {
    return dao.getAllTaxonomies();
  }

  public List<Homologue> getHomologues(long runId, String taxonomyId) throws IOException {
    return dao.getHomologues(runId, taxonomyId);
  }
}
