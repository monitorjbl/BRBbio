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
import com.thundermoose.bio.model.Control;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.RawData;
import com.thundermoose.bio.model.Run;

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

	public void readExcel(String runName, InputStream file) throws IOException {
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
		
		//create new run
		long runId = (Long) session.save(new Run(runName));

		//map external to internal id
		Map<String, Long> plates = new HashMap<String, Long>();
		
		Map<String, Control> controls = new HashMap<String, Control>();
		Map<String, Integer> dupCheck = new HashMap<String, Integer>();
		Map<String, Integer> negCount = new HashMap<String, Integer>();
		Map<String, Integer> posCount = new HashMap<String, Integer>();

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			String plateName = row.getCell(head.get(PLATE_ID)).getStringCellValue();

			//get plate, or create if necessary
			if (!plates.containsKey(plateName)) {
				plates.put(plateName, (Long)session.save(new Plate(runId, plateName)));
			}
			long plateId = plates.get(plateName);

			int time;
			if (row.getCell(head.get(TIME_MARKER)).getCellType() == Cell.CELL_TYPE_STRING) {
				time = Integer.parseInt(row.getCell(head.get(TIME_MARKER)).getStringCellValue());
			} else {
				time = (int) row.getCell(head.get(TIME_MARKER)).getNumericCellValue();
			}
			
			//get control, or create if necessary
			String controlKey = plateName+time;
			if(!controls.containsKey(controlKey)){
				controls.put(controlKey, new Control(plateId, time, 0,0));
			}
			Control ctrl = controls.get(controlKey);
			
			String ident = row.getCell(head.get(IDENTIFIER)).getStringCellValue();
			float data = (float) row.getCell(head.get(DATA)).getNumericCellValue();

			// track neg/pos
			if (neg.equals(ident)) {
				ctrl.setNegativeControl(ctrl.getNegativeControl() + data);
				negCount.put(controlKey, (negCount.containsKey(controlKey) ? negCount.get(controlKey) : 0) + 1);
			} else if (pos.equals(ident)) {
				ctrl.setPositiveControl(ctrl.getPositiveControl() + data);
				posCount.put(controlKey, (posCount.containsKey(controlKey) ? posCount.get(controlKey) : 0) + 1);
			} else if (ignored.containsKey(ident)) {
				// do nothing
			} else {
				String d = plateId + "_" + ident+"_"+time;
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

		for (String key : controls.keySet()) {
			Control ctrl = controls.get(key);
			ctrl.setNegativeControl(ctrl.getNegativeControl() / negCount.get(key));
			ctrl.setPositiveControl(ctrl.getPositiveControl() / posCount.get(key));
			session.save(ctrl);
		}

		session.getTransaction().commit();
		session.close();
	}

}
