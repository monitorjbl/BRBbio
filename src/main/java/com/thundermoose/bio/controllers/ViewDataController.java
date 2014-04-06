package com.thundermoose.bio.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thundermoose.bio.dao.NcbiDao;
import com.thundermoose.bio.managers.ExcelExportManager;
import com.thundermoose.bio.managers.TsvExportManager;
import com.thundermoose.bio.model.Taxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.model.NormalizedData;
import com.thundermoose.bio.model.Run;
import com.thundermoose.bio.model.ZFactor;
import com.thundermoose.bio.util.Utils;

@Controller
public class ViewDataController {

  @Autowired
  private DataDao dao;
  @Autowired
  private ExcelExportManager excelManager;
  @Autowired
  private TsvExportManager tsvManager;

  @RequestMapping(value = "/viewNormalizedData")
  public ModelAndView normalizedDataUi() {
    ModelAndView mv = new ModelAndView("viewNormalizedData");
    mv.addObject("runs", dao.getRuns(false, Utils.getCurrentUsername()));
    return mv;
  }

  @RequestMapping(value = "/viewViability")
  public ModelAndView viabilityUi() {
    ModelAndView mv = new ModelAndView("viewViability");
    mv.addObject("runs", dao.getRuns(true, Utils.getCurrentUsername()));
    return mv;
  }

  @RequestMapping(value = "/viewZFactor")
  public ModelAndView zFactorUi() {
    ModelAndView mv = new ModelAndView("viewZFactor");
    mv.addObject("runs", dao.getRuns(false, Utils.getCurrentUsername()));
    return mv;
  }

  @RequestMapping(value = "/deleteRun")
  public ModelAndView deleteRunUi() {
    ModelAndView mv = new ModelAndView("deleteRun");
    mv.addObject("runs", dao.getRuns(true, Utils.getCurrentUsername()));
    return mv;
  }

  @RequestMapping(value = "/getRuns")
  @ResponseBody
  public List<Run> getRuns(@RequestParam boolean includeViability) {
    return dao.getRuns(includeViability, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/deleteRunById")
  @ResponseBody
  public void deleteRun(@RequestParam long runId) {
    dao.deleteRun(runId, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/getNormalizedData")
  @ResponseBody
  public List<NormalizedData> getNormalizedData(@RequestParam long runId, @RequestParam String func) {
    return dao.getNormalizedDataByRunId(runId, Utils.getCurrentUsername(), func);
  }

  @RequestMapping(value = "/getViabilityData")
  @ResponseBody
  public List<NormalizedData> getViabilityData(@RequestParam long runId, @RequestParam String func) {
    return dao.getViabilityByRunId(runId, Utils.getCurrentUsername(), func);
  }

  @RequestMapping(value = "/getZFactorData")
  @ResponseBody
  public List<ZFactor> getZFactors(@RequestParam long runId, @RequestParam String func) {
    return dao.getZFactorsByRunId(runId, Utils.getCurrentUsername(), func);
  }

  @RequestMapping(value = "/getRawDataControlsForRun")
  @ResponseBody
  public List<String> getRawDataControlsForRun(@RequestParam(value = "runId") long runId) {
    return dao.getRawDataControlsForRun(runId, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/getViabilityControlsForRun")
  @ResponseBody
  public List<String> getViabilityControlsForRun(@RequestParam(value = "runId") long runId) {
    return dao.getViabilityControlsForRun(runId, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/getNormalizedDataExcel")
  public void getNormalizedDataExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    String username = Utils.getCurrentUsername();

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_normalized.xlsx\"");
    excelManager.exportNormalizedData(username, runId, function, response.getOutputStream());
  }

  @RequestMapping(value = "/getNormalizedDataTsv")
  public void getNormalizedDataTsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    String username = Utils.getCurrentUsername();

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, username).getRunName() + "_normalized.tsv\"");
    tsvManager.exportNormalizedData(username, runId, function, response.getOutputStream());
  }

  @RequestMapping(value = "/getZFactorExcel")
  public void getZFactorExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    String username = Utils.getCurrentUsername();

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_zfactor.xlsx\"");
    excelManager.exportZFactorData(username, runId, function, response.getOutputStream());
  }

  @RequestMapping(value = "/getZFactorTsv")
  public void getZFactorTsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    String username = Utils.getCurrentUsername();

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_zfactor.tsv\"");
    tsvManager.exportZFactorData(username, runId, function, response.getOutputStream());
  }

  @RequestMapping(value = "/getViabilityDataExcel")
  public void getViabilityExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    String username = Utils.getCurrentUsername();

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_viability.xlsx\"");
    excelManager.exportViabilityData(username, runId, function, response.getOutputStream());
  }

  @RequestMapping(value = "/getViabilityDataTsv")
  public void getViabilityDataTsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    String username = Utils.getCurrentUsername();

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_viability.tsv\"");
    tsvManager.exportViabilityData(username, runId, function, response.getOutputStream());
  }

}
