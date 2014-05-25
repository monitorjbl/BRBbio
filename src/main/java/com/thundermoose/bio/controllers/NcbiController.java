package com.thundermoose.bio.controllers;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.managers.HomologueManager;
import com.thundermoose.bio.managers.NcbiImportManager;
import com.thundermoose.bio.managers.NcbiManager;
import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.HomologueData;
import com.thundermoose.bio.model.Taxonomy;
import com.thundermoose.bio.util.Utils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Controller
public class NcbiController {
  @Autowired
  NcbiManager manager;
  @Autowired
  HomologueManager homologueManager;
  @Autowired
  DataDao dataDao;

  @RequestMapping(value = "/ncbi/homologues", method = RequestMethod.POST)
  @ResponseBody
  public com.thundermoose.bio.model.HomologueJoin getHomologues(HttpServletRequest request) throws IOException, FileUploadException {
    return homologueManager.findMatches(readFiles(request));
  }

  @RequestMapping(value = "/ncbi/homologuesExcel", method = RequestMethod.POST)
  public void getHomologuesExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException {
    response.setHeader("Content-Disposition", "attachment; filename=\"homologue.xlsx\"");
    homologueManager.findMatchesExcel(readFiles(request), response.getOutputStream());
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

  private List<FileItem> readFiles(HttpServletRequest request) throws FileUploadException {
    // Create a factory for disk-based file items
    DiskFileItemFactory factory = new DiskFileItemFactory();

    // Configure a repository (to ensure a secure temp location is used)
    ServletContext servletContext = request.getSession().getServletContext();
    File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
    factory.setRepository(repository);

    // Create a new file upload handler
    ServletFileUpload up = new ServletFileUpload(factory);
    return up.parseRequest(request);
  }
}
