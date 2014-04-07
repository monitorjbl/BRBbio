package com.thundermoose.bio.controllers;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.managers.NcbiImportManager;
import com.thundermoose.bio.managers.NcbiManager;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.Taxonomy;
import com.thundermoose.bio.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Controller
public class NcbiController {
  @Autowired
  NcbiManager manager;
  @Autowired
  DataDao dataDao;

  @RequestMapping(value = "/ncbi/getTaxonomies")
  @ResponseBody
  public List<Taxonomy> getTaxonomies() throws IOException {
    return manager.getTaxonomies();
  }

  @RequestMapping(value = "/ncbi/getHomologue")
  @ResponseBody
  public List<Homologue> getHomologue(@RequestParam long runId, @RequestParam String taxonomyId) throws IOException {
    return manager.getHomologues(runId, taxonomyId);
  }

  @RequestMapping(value = "/ncbi/getHomologueExcel")
  @ResponseBody
  public void getHomologueExcel(@RequestParam("runId") long runId, @RequestParam("taxonomyId") String taxonomyId, HttpServletResponse response) throws IOException {
    String username = Utils.getCurrentUsername();
    String tax = manager.getTaxonomy(taxonomyId).getName().toLowerCase().replaceAll(" ", "_");
    String filename = dataDao.getRunById(runId, username).getRunName() + "_" + tax + "_homologue.xlsx";
    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    manager.getHomologuesExcel(runId, taxonomyId, response.getOutputStream());
  }

  @RequestMapping(value = "/ncbi/getHomologueTsv")
  @ResponseBody
  public void getHomologueTsv(@RequestParam("runId") long runId, @RequestParam("taxonomyId") String taxonomyId, HttpServletResponse response) throws IOException {
    String username = Utils.getCurrentUsername();
    String tax = manager.getTaxonomy(taxonomyId).getName().toLowerCase().replaceAll(" ", "_");
    String filename = dataDao.getRunById(runId, username).getRunName() + "_" + tax + "_homologue.tsv";
    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
    manager.getHomologuesTsv(runId, taxonomyId, response.getOutputStream());
  }

  @RequestMapping(value = "/ncbi/lastLoadTime")
  @ResponseBody
  public Date getLastLoadTime() throws IOException {
    return manager.getLastLoadTime();
  }

  @RequestMapping(value = "/ncbi/refresh")
  @ResponseBody
  public void refresh() throws IOException {
    manager.refresh();
  }
}
