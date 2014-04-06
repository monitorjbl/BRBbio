package com.thundermoose.bio.controllers;

import com.thundermoose.bio.dao.NcbiDao;
import com.thundermoose.bio.managers.NcbiImportManager;
import com.thundermoose.bio.managers.NcbiManager;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.Taxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Controller
public class NcbiController {
  @Autowired
  NcbiManager manager;
  @Autowired
  NcbiImportManager importManage;

  @RequestMapping(value = "/ncbi/getTaxonomies")
  @ResponseBody
  public List<Taxonomy> getTaxonomies() throws IOException {
    return manager.getTaxonomies();
  }

  @RequestMapping(value = "/ncbi/getHomologue")
  @ResponseBody
  public List<Homologue> getHomologues(@RequestParam long runId, @RequestParam String taxonomyId) throws IOException {
    return manager.getHomologues(runId, taxonomyId);
  }

  @RequestMapping(value = "/ncbi/refresh")
  @ResponseBody
  public void refresh() throws IOException {
    importManage.importTaxonomyData();
    importManage.importHomologueData();
  }
}
