package com.thundermoose.bio.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@Controller
public class ViewData {

	@Autowired
	private DataDao dao;

	@RequestMapping(value = "viewNormalizedData")
	public ModelAndView normalizedDataUi() {
		ModelAndView mv = new ModelAndView("viewNormalizedData");
		mv.addObject("runs", getRuns());
		return mv;
	}

	@RequestMapping(value = "viewViability")
	public ModelAndView viabilityUi() {
		ModelAndView mv = new ModelAndView("viewViability");
		mv.addObject("runs", getRuns());
		return mv;
	}

	@RequestMapping(value = "viewZFactor")
	public ModelAndView zFactorUi() {
		ModelAndView mv = new ModelAndView("viewZFactor");
		mv.addObject("runs", getRuns());
		return mv;
	}

	@RequestMapping(value = "deleteRun")
	public ModelAndView deleteRunUi() {
		ModelAndView mv = new ModelAndView("deleteRun");
		mv.addObject("runs", getRuns());
		return mv;
	}

	@RequestMapping(value = "getRuns")
	public @ResponseBody
	List<Run> getRuns() {
		return dao.getRuns();
	}

	@RequestMapping(value = "deleteRunById")
	public @ResponseBody
	void deleteRun(@RequestParam long runId) {
		dao.deleteRun(runId);
	}

	@RequestMapping(value = "getNormalizedData")
	public @ResponseBody
	List<NormalizedData> getNormalizedData(@RequestParam long runId) {
		return dao.getNormalizedDataByRunId(runId);
	}

	@RequestMapping(value = "getViabilityData")
	public @ResponseBody
	List<NormalizedData> getViabilityData(@RequestParam long runId) {
		return dao.getViabilityByRunId(runId);
	}

	@RequestMapping(value = "getZFactorData")
	public @ResponseBody
	List<ZFactor> getZFactors(@RequestParam long runId) {
		return dao.getZFactorsByRunId(runId);
	}

	@RequestMapping(value = "getNormalizedDataExcel")
	public void getNormalizedDataExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		long runId = Long.parseLong(request.getParameter("runId"));
		List<NormalizedData> ex = dao.getNormalizedDataByRunId(runId);
		@SuppressWarnings("serial")
		List<String> headers = new ArrayList<String>() {
			{
				add("Plate Name");
				add("Gene");
			}
		};
		Map<String, Row> rowmap = new HashMap<String, Row>();

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		Row headerRow = sheet.createRow(0);

		for (NormalizedData dt : ex) {
			if (!headers.contains(dt.getTimeMarker() + "hr")) {
				headers.add(dt.getTimeMarker() + "hr");
			}

			String key = dt.getPlateName() + "_" + dt.getGeneId();
			if (!rowmap.containsKey(key)) {
				Row row = sheet.createRow(sheet.getLastRowNum() + 1);
				row.createCell(0).setCellValue(dt.getPlateName());
				row.createCell(1).setCellValue(dt.getGeneId());
				rowmap.put(key, row);
			}
			Row row = rowmap.get(key);
			row.createCell(row.getLastCellNum()).setCellValue(dt.getNormalized());
		}

		for (String h : headers) {
			int in = headerRow.getLastCellNum();
			headerRow.createCell(in >= 0 ? in : 0).setCellValue(h);
		}

		response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId).getRunName() + "_normalized.xlsx\"");
		wb.write(response.getOutputStream());
	}

	@RequestMapping(value = "getZFactorExcel")
	public void getZFactorExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		long runId = Long.parseLong(request.getParameter("runId"));
		List<ZFactor> ex = dao.getZFactorsByRunId(runId);
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

		response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId).getRunName() + "_zfactor.xlsx\"");
		wb.write(response.getOutputStream());
	}

	@RequestMapping(value = "getViabilityDataExcel")
	public void getViabilityExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		long runId = Long.parseLong(request.getParameter("runId"));
		List<NormalizedData> ex = dao.getViabilityByRunId(runId);
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

		response.setHeader("Content-Disposition", "attachment; filename=\"" + dao.getRunById(runId).getRunName() + "_viability.xlsx\"");
		wb.write(response.getOutputStream());
	}
}
