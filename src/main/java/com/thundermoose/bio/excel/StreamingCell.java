package com.thundermoose.bio.excel;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Calendar;
import java.util.Date;

public class StreamingCell implements Cell {
  private int columnIndex;
  private int rowIndex;
  private Object contents;

  public StreamingCell(int columnIndex, int rowIndex) {
    this.columnIndex = columnIndex;
    this.rowIndex = rowIndex;
  }

  public Object getContents() {
    return contents;
  }

  public void setContents(Object contents) {
    this.contents = contents;
  }

  @Override
  public int getColumnIndex() {
    return columnIndex;
  }

  @Override
  public int getRowIndex() {
    return rowIndex;
  }

  @Override
  public Sheet getSheet() {
    return null;
  }

  @Override
  public Row getRow() {
    return null;
  }

  @Override
  public void setCellType(int cellType) {

  }

  @Override
  public int getCellType() {
    if (contents == null) {
      return Cell.CELL_TYPE_BLANK;
    } else if (NumberUtils.isNumber(contents.toString())) {
      return Cell.CELL_TYPE_NUMERIC;
    } else {
      return Cell.CELL_TYPE_STRING;
    }


  }

  @Override
  public int getCachedFormulaResultType() {
    return 0;
  }

  @Override
  public void setCellValue(double value) {

  }

  @Override
  public void setCellValue(Date value) {

  }

  @Override
  public void setCellValue(Calendar value) {

  }

  @Override
  public void setCellValue(RichTextString value) {

  }

  @Override
  public void setCellValue(String value) {

  }

  @Override
  public void setCellFormula(String formula) throws FormulaParseException {

  }

  @Override
  public String getCellFormula() {
    return null;
  }

  @Override
  public double getNumericCellValue() {
    return Double.parseDouble((String) contents);
  }

  @Override
  public Date getDateCellValue() {
    return null;
  }

  @Override
  public RichTextString getRichStringCellValue() {
    return null;
  }

  @Override
  public String getStringCellValue() {
    return (String) contents;
  }

  @Override
  public void setCellValue(boolean value) {

  }

  @Override
  public void setCellErrorValue(byte value) {

  }

  @Override
  public boolean getBooleanCellValue() {
    return false;
  }

  @Override
  public byte getErrorCellValue() {
    return 0;
  }

  @Override
  public void setCellStyle(CellStyle style) {

  }

  @Override
  public CellStyle getCellStyle() {
    return null;
  }

  @Override
  public void setAsActiveCell() {

  }

  @Override
  public void setCellComment(Comment comment) {

  }

  @Override
  public Comment getCellComment() {
    return null;
  }

  @Override
  public void removeCellComment() {

  }

  @Override
  public Hyperlink getHyperlink() {
    return null;
  }

  @Override
  public void setHyperlink(Hyperlink link) {

  }

  @Override
  public CellRangeAddress getArrayFormulaRange() {
    return null;
  }

  @Override
  public boolean isPartOfArrayFormulaGroup() {
    return false;
  }
}
