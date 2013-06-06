package com.thundermoose.bio.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.RawData;

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
		if(!head.containsKey(PLATE_ID) || !head.containsKey(DATA) || !head.containsKey(IDENTIFIER) || !head.containsKey(TIME_MARKER)){
			throw new RuntimeException("Missing required column");
		}

		Session session = new Configuration().configure().buildSessionFactory().openSession();
		session.beginTransaction();

		Map<String, Plate> plates = new HashMap<String, Plate>();
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
			
			String key = plateId+"_"+time;
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
			} else {
				session.save(new RawData(plateId, ident, time, data));
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

	public static void main(String[] args) throws FileNotFoundException, IOException {
		new RawDataReader("negativecontrol", "Copb1_indi").readExcel(new FileInputStream(new File("src/test/resources/test_data.xlsx")));
	}
}
