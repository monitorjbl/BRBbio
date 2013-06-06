package com.thudermoose.bio.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.thudermoose.bio.model.Plate;
import com.thudermoose.bio.model.RawData;

public class RawDataReader {

	private static final String	PLATE_ID		= "AssayPlate";
	private static final String	DATA				= "Data";
	private static final String	IDENTIFIER	= "Identifier";
	private static final String	TIME_MARKER	= "TimeMarker";

	private String							neg;
	private String							pos;

	public RawDataReader(String neg, String pos) {
		this.neg = neg;
		this.pos = pos;
	}

	public void readExcel(InputStream is) throws IOException {
		Workbook wb = new XSSFWorkbook(is);
		Sheet sheet = wb.getSheetAt(0);

		Map<String, Integer> head = new HashMap<String, Integer>();
		for (Cell cell : sheet.getRow(0)) {
			head.put(cell.getStringCellValue(), cell.getColumnIndex());
		}
		// check for proper heads

		Map<String, Plate> plates = new HashMap<String, Plate>();
		Map<String, Integer> negCount = new HashMap<String, Integer>();
		Map<String, Integer> posCount = new HashMap<String, Integer>();

		for (Row row : sheet) {
			if (row.getRowNum() == 0) {
				continue;
			}
			String plateId = row.getCell(head.get(PLATE_ID)).getStringCellValue();
			if (!plates.containsKey(plateId)) {
				plates.put(plateId, new Plate(plateId, 0, 0));
			}
			Plate plate = plates.get(plateId);

			String ident = row.getCell(head.get(IDENTIFIER)).getStringCellValue();
			int time;
			if (row.getCell(head.get(TIME_MARKER)).getCellType() == Cell.CELL_TYPE_STRING) {
				time = Integer.parseInt(row.getCell(head.get(TIME_MARKER)).getStringCellValue());
			} else {
				time = (int) row.getCell(head.get(TIME_MARKER)).getNumericCellValue();
			}
			float data = (float) row.getCell(head.get(DATA)).getNumericCellValue();

			// track neg/pos
			if (neg.equals(ident)) {
				plate.setNegativeControl(plate.getNegativeControl() + data);
				negCount.put(plateId, (negCount.containsKey(plateId) ? negCount.get(plateId) : 0) + 1);
			} else if (pos.equals(ident)) {
				plate.setPositiveControl(plate.getPositiveControl() + data);
				posCount.put(plateId, (posCount.containsKey(plateId) ? posCount.get(plateId) : 0) + 1);
			} else {
				new RawData(plateId, ident, time, data);
			}
		}
		
		for(String key : plates.keySet()){
			
		}
	}
}
