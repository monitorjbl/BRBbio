package com.thundermoose.bio.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.exceptions.DatabaseException;
import com.thundermoose.bio.model.Control;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.RawData;
import com.thundermoose.bio.model.Run;
import com.thundermoose.bio.model.ViabilityData;

public class ExcelDataReader {

	private static final String PLATE_ID = "AssayPlate";
	private static final String DATA = "Data";
	private static final String IDENTIFIER = "Identifier";
	private static final String TIME_MARKER = "TimeMarker";

	@SuppressWarnings("serial")
	private static final Map<String, String> controls = new HashMap<String, String>() {
		{
			put("negativecontrol", "negative_control");
			put("Copb1_indi", "positive_control");
			put("Rab2_indi", "Rab2_indi");
		}
	};

	@SuppressWarnings("serial")
	private static final Map<String, String> ignored = new HashMap<String, String>() {
		{
		}
	};

	private DataDao dao;

	public ExcelDataReader(DataDao dao) {
		this.dao = dao;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void readRawData(String runName, InputStream file) throws IOException {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(file);
		} catch (InvalidFormatException e1) {
			throw new RuntimeException(e1);
		}

		Sheet sheet = wb.getSheetAt(0);

		Map<String, Integer> head = new HashMap<String, Integer>();
		for (Cell cell : sheet.getRow(0)) {
			head.put(cell.getStringCellValue(), cell.getColumnIndex());
		}
		if (!head.containsKey(PLATE_ID) || !head.containsKey(DATA) || !head.containsKey(IDENTIFIER) || !head.containsKey(TIME_MARKER)) {
			throw new RuntimeException("Missing required column");
		}

		// create new run
		long runId = dao.addRun(new Run(runName));

		// map external to internal id
		Map<String, Long> plates = new HashMap<String, Long>();
		Map<String, Integer> dupCheck = new HashMap<String, Integer>();

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			String plateName = row.getCell(head.get(PLATE_ID)).getStringCellValue();

			// get plate, or create if necessary
			if (!plates.containsKey(plateName)) {
				plates.put(plateName, dao.addPlate(new Plate(runId, plateName)));
			}
			long plateId = plates.get(plateName);

			int time;
			if (row.getCell(head.get(TIME_MARKER)).getCellType() == Cell.CELL_TYPE_STRING) {
				time = Integer.parseInt(row.getCell(head.get(TIME_MARKER)).getStringCellValue());
			} else {
				time = (int) row.getCell(head.get(TIME_MARKER)).getNumericCellValue();
			}

			String ident = row.getCell(head.get(IDENTIFIER)).getStringCellValue();
			float data = (float) row.getCell(head.get(DATA)).getNumericCellValue();

			// track neg/pos
			if (controls.containsKey(ident)) {
				dao.addControl(new Control(-1, plateId, controls.get(ident), time, data, new Date()));
			} else if (ignored.containsKey(ident)) {
				// do nothing
			} else {
				String d = plateId + "_" + ident + "_" + time;
				if (!dupCheck.containsKey(d)) {
					dupCheck.put(d, 1);
					try {
						dao.addRawData(new RawData(plateId, ident, time, data));
					} catch (Exception e) {
						throw new DatabaseException("Duplicate data found");
					}
				}
			}
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void readViability(long runId, InputStream file) throws IOException {
		Workbook wb;
		try {
			wb = WorkbookFactory.create(file);
		} catch (InvalidFormatException e1) {
			throw new RuntimeException(e1);
		}

		Sheet sheet = wb.getSheetAt(0);

		Map<String, Integer> head = new HashMap<String, Integer>();
		for (Cell cell : sheet.getRow(0)) {
			head.put(cell.getStringCellValue(), cell.getColumnIndex());
		}
		if (!head.containsKey(PLATE_ID) || !head.containsKey(DATA) || !head.containsKey(IDENTIFIER)) {
			throw new RuntimeException("Missing required column");
		}

		// map external to internal id
		Map<String, Long> plates = new HashMap<String, Long>();
		Map<String, Integer> dupCheck = new HashMap<String, Integer>();

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			String plateName = row.getCell(head.get(PLATE_ID)).getStringCellValue();

			// get plate, or create if necessary
			if (!plates.containsKey(plateName)) {
				try {
					plates.put(plateName, dao.getPlateByName(runId, plateName).getId());
				} catch (EmptyResultDataAccessException e) {
					//create plate if does not exist
					System.out.println("Creating plate "+plateName+" for run "+runId);
					plates.put(plateName, dao.addPlate(new Plate(runId, plateName)));
					
				}
			}
			long plateId = plates.get(plateName);

			String ident = row.getCell(head.get(IDENTIFIER)).getStringCellValue();
			float data = (float) row.getCell(head.get(DATA)).getNumericCellValue();

			// track neg/pos
			if (controls.containsKey(ident)) {
				// do nothing
			} else if (ignored.containsKey(ident)) {
				// do nothing
			} else {
				String d = plateId + "_" + ident;
				if (!dupCheck.containsKey(d)) {
					dupCheck.put(d, 1);
					try {
						dao.addViabilityData(new ViabilityData(plateId, ident, data));
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println(d);
						throw new DatabaseException("Duplicate data found");
					}
				}
			}
		}

	}

}
