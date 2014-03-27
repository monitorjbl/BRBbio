package com.thundermoose.bio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thundermoose.bio.model.NormalizedRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class ViewData {

  @Autowired
  private DataDao dao;

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
  public
  @ResponseBody
  List<Run> getRuns(@RequestParam boolean includeViability) {
    return dao.getRuns(includeViability, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/deleteRunById")
  public
  @ResponseBody
  void deleteRun(@RequestParam long runId) {
    dao.deleteRun(runId, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/getNormalizedData")
  public
  @ResponseBody
  List<NormalizedData> getNormalizedData(@RequestParam long runId, @RequestParam String func) {
    return dao.getNormalizedDataByRunId(runId, Utils.getCurrentUsername(), func);
  }

  @RequestMapping(value = "/getViabilityData")
  public
  @ResponseBody
  List<NormalizedData> getViabilityData(@RequestParam long runId, @RequestParam String func) {
    return dao.getViabilityByRunId(runId, Utils.getCurrentUsername(), func);
  }

  @RequestMapping(value = "/getZFactorData")
  public
  @ResponseBody
  List<ZFactor> getZFactors(@RequestParam long runId, @RequestParam String func) {
    return dao.getZFactorsByRunId(runId, Utils.getCurrentUsername(), func);
  }

  @RequestMapping(value = "/getRawDataControlsForRun")
  public
  @ResponseBody
  List<String> getRawDataControlsForRun(@RequestParam long runId) {
    return dao.getRawDataControlsForRun(runId, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/getViabilityControlsForRun")
  public
  @ResponseBody
  List<String> getViabilityControlsForRun(@RequestParam long runId) {
    return dao.getViabilityControlsForRun(runId, Utils.getCurrentUsername());
  }

  @RequestMapping(value = "/getNormalizedDataExcel")
  public void getNormalizedDataExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    List<NormalizedRow> ex = dao.getNormalizedRowDataByRunId(runId, Utils.getCurrentUsername(), function);
    @SuppressWarnings("serial")
    List<String> headers = new ArrayList<String>() {
      {
        add("Plate Name");
        add("Gene");
      }
    };

    List<Double> markers = dao.getTimeMarkers(runId, Utils.getCurrentUsername());
    for (Double d : markers) {
      headers.add(d + "hr");
    }

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet();
    Row headerRow = sheet.createRow(0);

    //write headers
    for (String h : headers) {
      int in = headerRow.getLastCellNum();
      headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
    }

    for (NormalizedRow dt : ex) {
      Row row = sheet.createRow(sheet.getLastRowNum() + 1);
      row.createCell(0).setCellValue(dt.getPlateName());
      row.createCell(1).setCellValue(dt.getGeneId());
      for (Double d : markers) {
        if (dt.getData().get(d) != null) {
          row.createCell(row.getLastCellNum()).setCellValue(dt.getData().get(d));
        } else {
          row.createCell(row.getLastCellNum());
        }
      }
    }

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_normalized.xlsx\"");
    wb.write(response.getOutputStream());
  }

  @RequestMapping(value = "/getNormalizedDataTsv")
  public void getNormalizedDataTsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    List<NormalizedData> ex = dao.getNormalizedDataByRunId(runId, Utils.getCurrentUsername(), function);

    String headers = "Plate Name\tGene\t";
    Map<String, String> rowmap = new TreeMap<String, String>();

    for (NormalizedData dt : ex) {
      if (!headers.contains(dt.getTimeMarker() + "hr")) {
        headers += dt.getTimeMarker() + "hr\t";
      }

      String key = dt.getPlateName() + "_" + dt.getGeneId();
      if (!rowmap.containsKey(key)) {
        String row = dt.getPlateName() + "\t" + dt.getGeneId() + "\t";
        rowmap.put(key, row);
      }
      rowmap.put(key, rowmap.get(key) + dt.getNormalized() + "\t");
    }

    String tsv = headers + "\n";
    for (String s : rowmap.keySet()) {
      tsv += rowmap.get(s) + "\n";
    }
    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_normalized.tsv\"");
    response.getOutputStream().write(tsv.getBytes());
  }

  @RequestMapping(value = "/getZFactorExcel")
  public void getZFactorExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    List<ZFactor> ex = dao.getZFactorsByRunId(runId, Utils.getCurrentUsername(), function);
    @SuppressWarnings("serial")
    List<String> headers = new ArrayList<String>() {
      {
        add("Plate Name");
      }
    };
    Map<String, Row> rowmap = new HashMap<String, Row>();

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet();
    Row headerRow = sheet.createRow(0);

    for (ZFactor dt : ex) {
      if (!headers.contains(dt.getTimeMarker() + "hr")) {
        headers.add(dt.getTimeMarker() + "hr");
      }

      String key = dt.getPlateName();
      if (!rowmap.containsKey(key)) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(0).setCellValue(dt.getPlateName());
        rowmap.put(key, row);
      }
      Row row = rowmap.get(key);
      row.createCell(row.getLastCellNum()).setCellValue(dt.getzFactor());
    }

    for (String h : headers) {
      int in = headerRow.getLastCellNum();
      headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
    }

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_zfactor.xlsx\"");
    wb.write(response.getOutputStream());
  }

  @RequestMapping(value = "/getZFactorTsv")
  public void getZFactorTsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    List<ZFactor> ex = dao.getZFactorsByRunId(runId, Utils.getCurrentUsername(), function);

    String headers = "Plate Name\t";
    Map<String, String> rowmap = new TreeMap<String, String>();

    for (ZFactor dt : ex) {
      if (!headers.contains(dt.getTimeMarker() + "hr")) {
        headers += dt.getTimeMarker() + "hr\t";
      }

      String key = dt.getPlateName();
      if (!rowmap.containsKey(key)) {
        String row = dt.getPlateName() + "\t";
        rowmap.put(key, row);
      }
      rowmap.put(key, rowmap.get(key) + dt.getzFactor() + "\t");
    }

    String tsv = headers + "\n";
    for (String s : rowmap.keySet()) {
      tsv += rowmap.get(s) + "\n";
    }
    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_zfactor.tsv\"");
    response.getOutputStream().write(tsv.getBytes());
  }

  @RequestMapping(value = "/getViabilityDataExcel")
  public void getViabilityExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    List<NormalizedData> ex = dao.getViabilityByRunId(runId, Utils.getCurrentUsername(), function);

    @SuppressWarnings("serial")
    List<String> headers = new ArrayList<String>() {
      {
        add("Plate Name");
        add("Gene");
        add("Viability");
      }
    };

    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.createSheet();
    Row headerRow = sheet.createRow(0);

    for (NormalizedData dt : ex) {
      Row row = sheet.createRow(sheet.getLastRowNum() + 1);
      row.createCell(0).setCellValue(dt.getPlateName());
      row.createCell(1).setCellValue(dt.getGeneId());
      row.createCell(2).setCellValue(dt.getNormalized());
    }

    for (String h : headers) {
      int in = headerRow.getLastCellNum();
      headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
    }

    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_viability.xlsx\"");
    wb.write(response.getOutputStream());
  }

  @RequestMapping(value = "/getViabilityDataTsv")
  public void getViabilityDataTsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long runId = Long.parseLong(request.getParameter("runId"));
    String function = request.getParameter("func");
    List<NormalizedData> ex = dao.getViabilityByRunId(runId, Utils.getCurrentUsername(), function);

    String headers = "Plate Name\tGene\tViability";
    Map<String, String> rowmap = new TreeMap<String, String>();

    for (NormalizedData dt : ex) {
      String key = dt.getPlateName() + "_" + dt.getGeneId();
      if (!rowmap.containsKey(key)) {
        String row = dt.getPlateName() + "\t" + dt.getGeneId() + "\t";
        rowmap.put(key, row);
      }
      rowmap.put(key, rowmap.get(key) + dt.getNormalized() + "\t");
    }

    String tsv = headers + "\n";
    for (String s : rowmap.keySet()) {
      tsv += rowmap.get(s) + "\n";
    }
    response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId, Utils.getCurrentUsername()).getRunName() + "_viability.tsv\"");
    response.getOutputStream().write(tsv.getBytes());
  }
}
