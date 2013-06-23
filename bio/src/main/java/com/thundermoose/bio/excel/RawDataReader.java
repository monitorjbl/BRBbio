package com.thundermoose.bio.excel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import com.thundermoose.bio.exceptions.DatabaseException;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.RawData;

public class RawDataReader {

	private static final String								PLATE_ID		= "AssayPlate";
	private static final String								DATA				= "Data";
	private static final String								IDENTIFIER	= "Identifier";
	private static final String								TIME_MARKER	= "TimeMarker";

	private static final Map<String, String>	ignored			= new HashMap<String, String>() {
																													{
																														put("Rab2_indi", "");
																													}
																												};

	private String														neg;
	private String														pos;
	private Session														session;

	public RawDataReader(String neg, String pos, Session session) {
		this.neg = neg;
		this.pos = pos;
		this.session = session;
	}

	public void readExcel(InputStream file) throws IOException {
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

		session.beginTransaction();

		Map<String, Plate> plates = new HashMap<String, Plate>();
		Map<String, Integer> dupCheck = new HashMap<String, Integer>();
		Map<String, Integer> negCount = new HashMap<String, Integer>();
		Map<String, Integer> posCount = new HashMap<String, Integer>();

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			String plateId = row.getCell(head.get(PLATE_ID)).getStringCellValue();
			int time;
			if (row.getCell(head.get(TIME_MARKER)).getCellType() == Cell.CELL_TYPE_STRING) {
				time = Integer.parseInt(row.getCell(head.get(TIME_MARKER)).getStringCellValue());
			} else {
				time = (int) row.getCell(head.get(TIME_MARKER)).getNumericCellValue();
			}

			String key = plateId + "_" + time;
			if (!plates.containsKey(key)) {
				plates.put(key, new Plate(plateId, time, 0, 0));
			}
			Plate plate = plates.get(key);

			String ident = row.getCell(head.get(IDENTIFIER)).getStringCellValue();
			float data = (float) row.getCell(head.get(DATA)).getNumericCellValue();

			// track neg/pos
			if (neg.equals(ident)) {
				plate.setNegativeControl(plate.getNegativeControl() + data);
				negCount.put(key, (negCount.containsKey(key) ? negCount.get(key) : 0) + 1);
			} else if (pos.equals(ident)) {
				plate.setPositiveControl(plate.getPositiveControl() + data);
				posCount.put(key, (posCount.containsKey(key) ? posCount.get(key) : 0) + 1);
			} else if (ignored.containsKey(ident)) {
				// do nothing
			} else {
				String d = plateId + "_" + ident;
				if (!dupCheck.containsKey(d)) {
					dupCheck.put(d, 1);
					try {
						session.save(new RawData(plateId, ident, time, data));
					} catch (ConstraintViolationException e) {
						throw new DatabaseException("Duplicate data found");
					}
				}
			}
		}

		for (String key : plates.keySet()) {
			Plate plate = plates.get(key);
			plate.setNegativeControl(plate.getNegativeControl() / negCount.get(key));
			plate.setPositiveControl(plate.getPositiveControl() / posCount.get(key));
			session.save(plate);
		}

		session.getTransaction().commit();
		session.close();
	}

}
