package com.thundermoose.bio.excel;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import com.thundermoose.bio.dao.DataDao;
import com.thundermoose.bio.model.Control;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.RawData;
import com.thundermoose.bio.model.Run;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Thundermoose on 4/23/2014.
 */
public class ExcelDataReaderTest {
  @InjectMocks
  ExcelDataReader sut = new ExcelDataReader();
  @Mock
  DataDao mockDao;


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testRawRead_simple() throws IOException {
    sut.controls = ImmutableMap.of("Copb1_indi", "", "negativecontrol", "");

    Mocks plateMock = new Mocks();
    Mocks runMock = new Mocks();
    Mocks rawMock = new Mocks();
    Mocks ctrlMock = new Mocks();

    when(mockDao.addPlate(any(Plate.class))).then(plateMock.incrementingMock);
    when(mockDao.addRun(any(Run.class), anyString())).then(runMock.incrementingMock);
    when(mockDao.addRawData(any(RawData.class))).then(rawMock.incrementingMock);
    when(mockDao.addRawDataControl(any(Control.class))).then(ctrlMock.incrementingMock);

    InputStream is = Resources.getResource("test_raw.xlsx").openStream();
    sut.readRawData("Test", "testuser", is);

    assertEquals(1, runMock.values.size());
    assertEquals(1, plateMock.values.size());
    assertEquals(368, rawMock.values.size());
    assertEquals(32, ctrlMock.values.size());
  }

  @Test
  public void testRawRead_duplicates() throws IOException {
    sut.controls = ImmutableMap.of("Copb1_indi", "", "negativecontrol", "");

    Mocks plateMock = new Mocks();
    Mocks runMock = new Mocks();
    Mocks rawMock = new Mocks();
    Mocks ctrlMock = new Mocks();

    when(mockDao.addPlate(any(Plate.class))).then(plateMock.incrementingMock);
    when(mockDao.addRun(any(Run.class), anyString())).then(runMock.incrementingMock);
    when(mockDao.addRawData(any(RawData.class))).then(rawMock.incrementingMock);
    when(mockDao.addRawDataControl(any(Control.class))).then(ctrlMock.incrementingMock);

    InputStream is = Resources.getResource("test_raw_dupes.xlsx").openStream();
    sut.readRawData("Test", "testuser", is);

    assertEquals(1, runMock.values.size());
    assertEquals(1, plateMock.values.size());
    assertEquals(21, rawMock.values.size());
    assertEquals(3, ctrlMock.values.size());
  }

  @Test
  public void testViabilityRead_simple() throws IOException {
    sut.controls = ImmutableMap.of("Copb1_indi", "", "negativecontrol", "");

    Mocks plateMock = new Mocks();
    Mocks runMock = new Mocks();
    Mocks rawMock = new Mocks();
    Mocks ctrlMock = new Mocks();

    when(mockDao.addPlate(any(Plate.class))).then(plateMock.incrementingMock);
    when(mockDao.addRun(any(Run.class), anyString())).then(runMock.incrementingMock);
    when(mockDao.addRawData(any(RawData.class))).then(rawMock.incrementingMock);
    when(mockDao.addRawDataControl(any(Control.class))).then(ctrlMock.incrementingMock);

    InputStream is = Resources.getResource("test_viability.xlsx").openStream();
    sut.readRawData("Test", "testuser", is);

    assertEquals(1, runMock.values.size());
    assertEquals(1, plateMock.values.size());
    assertEquals(368, rawMock.values.size());
    assertEquals(32, ctrlMock.values.size());
  }


  private class Mocks {
    long counter = 0;
    List<Object> values = new ArrayList<>();

    Answer<Long> incrementingMock = new Answer<Long>() {
      @Override
      public Long answer(InvocationOnMock invocationOnMock) throws Throwable {
        values.add(invocationOnMock.getArguments()[0]);
        return counter++;
      }
    };
  }
}
