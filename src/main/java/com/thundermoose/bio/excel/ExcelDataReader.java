package com.thundermoose.bio.excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;

import com.monitorjbl.xlsx.StreamingReader;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.mapdb.DBMaker;
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
  private static final Logger logger = Logger.getLogger(ExcelDataReader.class);

  public static final String PLATE_ID = "AssayPlate";
  public static final String GENE_SYMBOL = "GeneSymbol";
  public static final String GENE_ID = "EntrezGeneID";
  public static final String TIME_MARKER = "TimeMarker";
  public static final String DATA = "Data";

  private static final int BUFFER_SIZE = 4096;
  private static final int ROW_CACHE_SIZE = 100;

  @SuppressWarnings("serial")
  private final Map<String, String> ignored = new HashMap<String, String>() {
    {
    }
  };

  DataDao dao;
  Map<String, String> controls;

  ExcelDataReader() {
  }

  public ExcelDataReader(DataDao dao, List<String> controls) {
    this.dao = dao;
    this.controls = new HashMap<String, String>();
    for (String c : controls) {
      this.controls.put(c, null);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void readRawData(String runName, String username, InputStream file) throws IOException {

    StreamingReader reader = StreamingReader.builder()
            .bufferSize(BUFFER_SIZE)
            .rowCacheSize(ROW_CACHE_SIZE)
            .read(file);

    Map<String, Integer> head = new HashMap<String, Integer>();
    // create new run
    long runId = dao.addRun(new Run(runName, false), username);

    // map external to internal id
    Map<String, Long> plates = new HashMap<String, Long>();
    Map<String, Integer> dupCheck = new HashMap<String, Integer>();

    for (Row row : reader) {
      if (row.getRowNum() == 0 || row.getCell(0) == null) {
        for (Cell cell : row) {
          head.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        if (findMissingColumns(head, true) != null) {
          throw new RuntimeException("Missing required column [" + findMissingColumns(head, true) + "]");
        }
        continue;
      }

      int index = -1;
      try {
        index = head.get(PLATE_ID);
        String plateName = row.getCell(index).getStringCellValue();

        // get plate, or create if necessary
        if (!plates.containsKey(plateName)) {
          plates.put(plateName, dao.addPlate(new Plate(runId, plateName)));
        }
        long plateId = plates.get(plateName);

        double time;
        index = head.get(TIME_MARKER);
        if (row.getCell(index).getCellType() == Cell.CELL_TYPE_STRING) {
          time = Double.parseDouble(row.getCell(index).getStringCellValue());
        } else {
          time = row.getCell(index).getNumericCellValue();
        }

        index = head.get(GENE_ID);
        String geneId = getStringFromCell(row.getCell(index));

        index = head.get(GENE_SYMBOL);
        String geneSymbol = getStringFromCell(row.getCell(index));

        index = head.get(DATA);
        float data = (float) row.getCell(index).getNumericCellValue();

        // track neg/pos
        if (controls.containsKey(geneSymbol)) {
          dao.addRawDataControl(new Control(-1, plateId, geneId, geneSymbol, time, data, new Date()));
        } else if (ignored.containsKey(geneSymbol)) {
          // do nothing
        } else {
          String d = plateId + "_" + geneSymbol + "_" + time;
          if (!dupCheck.containsKey(d)) {
            dupCheck.put(d, 1);
            try {
              dao.addRawData(new RawData(plateId, geneId, geneSymbol, time, data));
            } catch (Exception e) {
              throw new DatabaseException("Duplicate data found in database at row " + (row.getRowNum() + 1) + ", unable to ignore");
            }
          } else {
            logger.debug("Ignoring duplicate at row " + (row.getRowNum() + 1));
          }
        }
      } catch (Exception e) {
        throw new RuntimeException("Error at cell " + CellReference.convertNumToColString(index) +
                (row.getRowNum() + 1) + " (" + e.getMessage() + "). Please check the data and try again.", e);
      }
    }

  }

  public void readLinkedViability(long runId, String username, InputStream file) throws IOException {
    readViability(file, runId, null, username);
  }

  public void readIndependentViability(String runName, String username, InputStream file) throws IOException {
    readViability(file, null, runName, username);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  private void readViability(InputStream file, Long runId, String runName, String username) throws IOException {
    StreamingReader reader = StreamingReader.builder()
            .bufferSize(BUFFER_SIZE)
            .rowCacheSize(ROW_CACHE_SIZE)
            .read(file);

    Map<String, Integer> head = new HashMap<String, Integer>();
    // if this is an independent load, need to create a run
    if (runId == null) {
      runId = dao.addRun(new Run(runName, true), username);
    }

    // map external to internal id
    Map<String, Long> plates = new HashMap<String, Long>();
    Map<String, Integer> dupCheck = new HashMap<String, Integer>();

    for (Row row : reader) {
      if (row.getRowNum() == 0 || row.getCell(0) == null) {
        for (Cell cell : row) {
          head.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        if (findMissingColumns(head, false) != null) {
          throw new RuntimeException("Missing required column  [" + findMissingColumns(head, false) + "]");
        }
        continue;
      }

      int index = -1;
      try {
        index = head.get(PLATE_ID);
        String plateName = row.getCell(index).getStringCellValue();

        // get plate, or create if necessary
        if (!plates.containsKey(plateName)) {
          if (runName != null) {
            plates.put(plateName, dao.addPlate(new Plate(runId, plateName)));
          } else {
            plates.put(plateName, dao.getPlateByName(runId, plateName).getId());
          }
        }
        long plateId = plates.get(plateName);

        index = head.get(GENE_ID);
        String geneId = getStringFromCell(row.getCell(index));

        index = head.get(GENE_SYMBOL);
        String geneSymbol = getStringFromCell(row.getCell(index));

        index = head.get(DATA);
        float data = (float) row.getCell(index).getNumericCellValue();

        // track neg/pos
        if (controls.containsKey(geneSymbol)) {
          dao.addViabilityControl(new Control(plateId, geneId, geneSymbol, data, new Date()));
        } else if (ignored.containsKey(geneSymbol)) {
          // do nothing
        } else {
          String d = plateId + "_" + geneSymbol;
          if (!dupCheck.containsKey(d)) {
            dupCheck.put(d, 1);
            try {
              dao.addViabilityData(new ViabilityData(plateId, geneId, geneSymbol, data));
            } catch (Exception e) {
              logger.error(e);
              throw new DatabaseException("Duplicate data found");
            }
          }
        }
      } catch (Exception e) {
        throw new RuntimeException("Error at cell " + CellReference.convertNumToColString(index) +
                (row.getRowNum() + 1) + " (" + e.getMessage() + "). Please check the data and try again.", e);
      }
    }
  }

  String getStringFromCell(Cell c) {
    if (c.getCellType() == Cell.CELL_TYPE_STRING) {
      return c.getStringCellValue();
    }
    return Long.toString((long) c.getNumericCellValue());
  }

  String findMissingColumns(Map<String, Integer> head, boolean includeTime) {
    String missing = "";
    if (!head.containsKey(PLATE_ID)) {
      missing += "," + PLATE_ID;
    } else if (!head.containsKey(DATA)) {
      missing += "," + DATA;
    } else if (!head.containsKey(GENE_SYMBOL)) {
      missing += "," + GENE_SYMBOL;
    } else if (!head.containsKey(GENE_ID)) {
      missing += "," + GENE_ID;
    } else if (!head.containsKey(TIME_MARKER) && includeTime) {
      missing += "," + TIME_MARKER;
    }

    if (missing.length() > 0) {
      return missing.substring(1);
    }
    return null;
  }
}
