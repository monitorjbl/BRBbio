package com.thundermoose.bio.excel;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tayjones on 3/12/14.
 */
public class StreamingReader implements Iterable<Row> {
  private SharedStringsTable sst;
  XMLEventReader parser;
  private String lastContents;
  private boolean nextIsString;

  private List<Row> rowCache = new ArrayList<Row>();
  private Iterator<Row> rowCacheIterator;
  private StreamingRow currentRow;
  private StreamingCell currentCell;

  private StreamingReader(SharedStringsTable sst, XMLEventReader parser) {
    this.sst = sst;
    this.parser = parser;
  }

  private boolean getRow() {
    try {
      int iters = 0;
      rowCache = new ArrayList<Row>();
      while (rowCache.size() < 10 && parser.hasNext()) {
        handleEvent(parser.nextEvent());
        iters++;
      }
      rowCacheIterator = rowCache.iterator();
      return iters > 0;
    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
    return false;
  }

  public void handleEvent(XMLEvent event) throws SAXException {
    if (event.getEventType() == XMLStreamConstants.CHARACTERS) {
      Characters c = event.asCharacters();
      lastContents += c.getData();
    } else if (event.getEventType() == XMLStreamConstants.START_ELEMENT) {
      StartElement startElement = event.asStartElement();
      // c => cell
      if (startElement.getName().getLocalPart().equals("c")) {
        Attribute ref = startElement.getAttributeByName(new QName("r"));

        String[] coord = ref.getValue().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        StreamingCell cc = new StreamingCell(Integer.parseInt(coord[1]), CellReference.convertColStringToIndex(coord[0]));

        if (currentCell == null || currentCell.getRowIndex() != cc.getRowIndex()) {
          if (currentRow != null) {
            rowCache.add(currentRow);
          }
          currentRow = new StreamingRow(cc.getRowIndex());
        }
        currentCell = cc;

        // Print the cell reference
        // Figure out if the value is an index in the SST
        Attribute type = startElement.getAttributeByName(new QName("t"));
        String cellType = type == null ? null : type.getValue();
        if (cellType != null && cellType.equals("s")) {
          nextIsString = true;
        } else {
          nextIsString = false;
        }
      }
      // Clear contents cache
      lastContents = "";
    } else if (event.getEventType() == XMLStreamConstants.END_ELEMENT) {
      EndElement endElement = event.asEndElement();
      // Process the last contents as required.
      // Do now, as characters() may be called more than once
      if (nextIsString) {
        int idx = Integer.parseInt(lastContents);
        lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
        nextIsString = false;
      }

      // v => contents of a cell
      // Output after we've seen the string contents
      if (endElement.getName().getLocalPart().equals("v")) {
        currentCell.setContents(lastContents);
        currentRow.getCellList().add(currentCell);
      }

    }
  }

  @Override
  public Iterator<Row> iterator() {
    return new RowIterator();
  }

  class RowIterator implements Iterator<Row> {
    @Override
    public boolean hasNext() {
      return (rowCacheIterator != null && rowCacheIterator.hasNext()) || getRow();
    }

    @Override
    public Row next() {
      return rowCacheIterator.next();
    }

    @Override
    public void remove() {
      throw new RuntimeException("NotSupported");
    }
  }

  public static StreamingReader createReader(InputStream is) throws IOException, OpenXML4JException, XMLStreamException {
    //have to read inputstream to disk
    File f = null;
    FileOutputStream fos = null;
    try {
      f = Files.createTempFile("tmp-", ".xls").toFile();
      fos = new FileOutputStream(f);

      int read = 0;
      byte[] bytes = new byte[1024];
      while ((read = is.read(bytes)) != -1) {
        fos.write(bytes, 0, read);
      }
      is.close();

      OPCPackage pkg = OPCPackage.open(f);
      XSSFReader reader = new XSSFReader(pkg);
      SharedStringsTable sst = reader.getSharedStringsTable();

      InputStream sheet = reader.getSheetsData().next();

      XMLEventReader parser = XMLInputFactory.newInstance().createXMLEventReader(sheet);
      return new StreamingReader(sst, parser);
    } finally {
      fos.close();
      f.delete();
    }
  }

  public static void main(String[] args) throws Exception {
    StreamingReader handler = createReader(new FileInputStream("/Users/tayjones/Downloads/All_Data.xlsx"));
    for (Row r : handler) {
      for (Cell c : r) {
        System.out.println(CellReference.convertNumToColString(r.getRowNum()) + c.getColumnIndex() + ": " + c.getStringCellValue());
      }
    }
  }

}
