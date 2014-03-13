package com.thundermoose.bio.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tayjones on 3/12/14.
 */
public class StreamingRow implements Row {
  private int rowIndex;
  private List<Cell> cellList = new LinkedList<Cell>();

  public StreamingRow(int rowIndex) {
    this.rowIndex = rowIndex;
  }

  public List<Cell> getCellList() {
    return cellList;
  }

  public void setCellList(List<Cell> cellList) {
    this.cellList = cellList;
  }

  @Override
  public Cell createCell(int column) {
    return null;
  }

  @Override
  public Cell createCell(int column, int type) {
    return null;
  }

  @Override
  public void removeCell(Cell cell) {

  }

  @Override
  public void setRowNum(int rowNum) {

  }

  @Override
  public int getRowNum() {
    return rowIndex;
  }

  @Override
  public Cell getCell(int cellnum) {
    return cellList.get(cellnum);
  }

  @Override
  public Cell getCell(int cellnum, MissingCellPolicy policy) {
    return null;
  }

  @Override
  public short getFirstCellNum() {
    return 0;
  }

  @Override
  public short getLastCellNum() {
    return 0;
  }

  @Override
  public int getPhysicalNumberOfCells() {
    return 0;
  }

  @Override
  public void setHeight(short height) {

  }

  @Override
  public void setZeroHeight(boolean zHeight) {

  }

  @Override
  public boolean getZeroHeight() {
    return false;
  }

  @Override
  public void setHeightInPoints(float height) {

  }

  @Override
  public short getHeight() {
    return 0;
  }

  @Override
  public float getHeightInPoints() {
    return 0;
  }

  @Override
  public boolean isFormatted() {
    return false;
  }

  @Override
  public CellStyle getRowStyle() {
    return null;
  }

  @Override
  public void setRowStyle(CellStyle style) {

  }

  @Override
  public Iterator<Cell> cellIterator() {
    return cellList.iterator();
  }

  @Override
  public Sheet getSheet() {
    return null;
  }

  @Override
  public Iterator<Cell> iterator() {
    return cellList.iterator();
  }
}
