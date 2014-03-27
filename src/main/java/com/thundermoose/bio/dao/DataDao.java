package com.thundermoose.bio.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.thundermoose.bio.model.NormalizedRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.io.Resources;
import com.thundermoose.bio.excel.ExcelDataReader;
import com.thundermoose.bio.model.Control;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.NormalizedData;
import com.thundermoose.bio.model.RawData;
import com.thundermoose.bio.model.Run;
import com.thundermoose.bio.model.ViabilityData;
import com.thundermoose.bio.model.ZFactor;
import com.thundermoose.bio.util.Utils;

public class DataDao {

  private static final String RUN_SQL = "sql/run.sql";
  private static final String PLATE_SQL = "sql/plate.sql";
  private static final String NORMALIZE_SQL = "sql/normalize.sql";
  private static final String ZFACTOR_SQL = "sql/zfactor.sql";
  private static final String VIABILITY_SQL = "sql/viability.sql";

  private static final String GET_RAW_DATA_CONTROLS = "sql/rawControls.sql";
  private static final String GET_VIABILITY_CONTROLS = "sql/viabilityControls.sql";
  private static final String GET_TIME_MARKERS = "sql/getTimeMarkers.sql";

  private static final String INSERT_RUN = "INSERT INTO runs (run_name, viability_only) VALUES(?,?)";
  private static final String INSERT_PLATE = "INSERT INTO plates (run_id,plate_name) VALUES(?,?)";
  private static final String INSERT_RAW_DATA_CONTROL = "INSERT INTO raw_data_controls (plate_id,identifier,time_marker,data) VALUES(?,?,?,?)";
  private static final String INSERT_RAW_DATA = "INSERT INTO raw_data (plate_id,identifier,time_marker,data) VALUES(?,?,?,?)";
  private static final String INSERT_VIABILITY_CONTROL = "INSERT INTO cell_viability_controls (plate_id,identifier,data) VALUES(?,?,?)";
  private static final String INSERT_VIABILITY_DATA = "INSERT INTO cell_viability (plate_id,identifier,data) VALUES(?,?,?)";
  private static final String INSERT_SECURITY = "INSERT INTO run_security (run_id,user_id) VALUES(?,(SELECT id FROM users WHERE user_name = ?))";

  private static final String DELETE_VIABILITY_DATA = "DELETE FROM cell_viability WHERE plate_id IN (SELECT id FROM plates WHERE run_id = ?)";
  private static final String DELETE_VIABILITY_CONTROLS = "DELETE FROM cell_viability_controls WHERE plate_id IN (SELECT id FROM plates WHERE run_id = ?)";
  private static final String DELETE_RAW_DATA = "DELETE FROM raw_data WHERE plate_id IN (SELECT id FROM plates WHERE run_id = ?)";
  private static final String DELETE_RAW_DATA_CONTROLS = "DELETE FROM raw_data_controls WHERE plate_id IN (SELECT id FROM plates WHERE run_id = ?)";
  private static final String DELETE_PLATES = "DELETE FROM plates WHERE run_id = ?";
  private static final String DELETE_RUN = "DELETE FROM runs WHERE id = ?";
  private static final String DELETE_SECURITY = "DELETE FROM run_security WHERE run_id = ?";

  @Autowired
  private JdbcTemplate jdbc;

  public List<Run> getRuns(boolean includeViability, String username) {
    String sql = read(RUN_SQL);
    if (!includeViability) {
      sql += " and viability_only = false";
    }
    return jdbc.query(sql, new Object[]{username}, new RunRowMapper());
  }

  public Plate getPlateByName(long runId, String plateName) {
    return jdbc.queryForObject(read(PLATE_SQL) + " WHERE run_id=? AND plate_name=?", new Object[]{runId, plateName}, new PlateRowMapper());
  }

  public Run getRunById(long runId, String username) {
    return jdbc.queryForObject(read(RUN_SQL) + " AND id = ?", new Object[]{username, runId}, new RunRowMapper());
  }

  public List<Double> getTimeMarkers(long runId, String username) {
    return jdbc.queryForList(read(GET_TIME_MARKERS), Double.class, runId, username, runId, username);
  }

  public List<NormalizedRow> getNormalizedRowDataByRunId(long runId, String username, String function) {
    return jdbc.query(read(NORMALIZE_SQL).replaceAll("#function#", convertRawDataFunction(runId, username, function)), new Object[]{runId, username}, new ProcessedDataExtractor());
  }

  public List<NormalizedData> getNormalizedDataByRunId(long runId, String username, String function) {
    return jdbc.query(read(NORMALIZE_SQL).replaceAll("#function#", convertRawDataFunction(runId, username, function)), new Object[]{runId, username}, new ProcessedDataRowMapper());
  }

  public List<ZFactor> getZFactorsByRunId(long runId, String username, String function) {
    return jdbc.query(read(ZFACTOR_SQL).replaceAll("#function#", convertRawDataFunction(runId, username, function)), new Object[]{runId, username}, new ZFactorRowMapper());
  }

  public List<NormalizedData> getViabilityByRunId(long runId, String username, String function) {
    return jdbc.query(read(VIABILITY_SQL).replaceAll("#function#", convertViabilityFunction(runId, username, function)), new Object[]{runId, username}, new ProcessedDataRowMapper());
  }

  public List<String> getRawDataControlsForRun(long runId, String username) {
    return jdbc.queryForList(read(GET_RAW_DATA_CONTROLS), new Object[]{runId, username}, String.class);
  }

  public List<String> getViabilityControlsForRun(long runId, String username) {
    return jdbc.queryForList(read(GET_VIABILITY_CONTROLS), new Object[]{runId, username}, String.class);
  }

  private String convertRawDataFunction(long runId, String username, String function) {
    String func = function.replaceAll("STD\\(", " STDDEV_SAMP(").replaceAll("rawData", "CASE WHEN a.type='raw' THEN a.data END");
    for (String r : getRawDataControlsForRun(runId, username)) {
      func = func.replaceAll(r, "CASE WHEN a.type='" + r + "' THEN a.data END");
    }

    return func;
  }

  private String convertViabilityFunction(long runId, String username, String function) {
    String func = function.replaceAll("STD\\(", " STDDEV_SAMP(").replaceAll("rawData", "CASE WHEN a.type='raw' THEN a.data END");
    for (String r : getViabilityControlsForRun(runId, username)) {
      func = func.replaceAll(r, "CASE WHEN a.type='" + r + "' THEN a.data END");
    }

    return func;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void deleteRun(long runId, String username) {
    // is this run visibile to the user deleting it?
    getRunById(runId, username);

    // would have died on getting an invisible run, continue with delete
    jdbc.update(DELETE_VIABILITY_DATA, runId);
    jdbc.update(DELETE_VIABILITY_CONTROLS, runId);
    jdbc.update(DELETE_RAW_DATA, runId);
    jdbc.update(DELETE_RAW_DATA_CONTROLS, runId);
    jdbc.update(DELETE_PLATES, runId);
    jdbc.update(DELETE_SECURITY, runId);
    jdbc.update(DELETE_RUN, runId);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public long addRun(final Run run) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    // add new run
    jdbc.update(new PreparedStatementCreator() {

      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_RUN, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, run.getRunName());
        ps.setBoolean(2, run.getViabilityOnly());
        return ps;
      }

    }, keyHolder);
    long id = keyHolder.getKey().longValue();

    // set security
    jdbc.update(INSERT_SECURITY, id, Utils.getCurrentUsername());

    return id;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public long addPlate(final Plate plate) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(new PreparedStatementCreator() {

      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_PLATE, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, plate.getRunId());
        ps.setString(2, plate.getPlateName());
        return ps;
      }

    }, keyHolder);
    return keyHolder.getKey().longValue();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public long addRawDataControl(final Control control) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(new PreparedStatementCreator() {

      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_RAW_DATA_CONTROL, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, control.getPlateId());
        ps.setString(2, control.getIdentifier());
        ps.setDouble(3, control.getTimeMarker());
        ps.setFloat(4, control.getData());
        return ps;
      }

    }, keyHolder);
    return keyHolder.getKey().longValue();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public long addRawData(final RawData rawData) {
    return jdbc.update(new PreparedStatementCreator() {

      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_RAW_DATA, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, rawData.getPlateId());
        ps.setString(2, rawData.getIdentifier());
        ps.setDouble(3, rawData.getTimeMarker());
        ps.setFloat(4, rawData.getData());
        return ps;
      }

    });
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public long addViabilityData(final ViabilityData rawData) {
    return jdbc.update(new PreparedStatementCreator() {

      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_VIABILITY_DATA, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, rawData.getPlateId());
        ps.setString(2, rawData.getIdentifier());
        ps.setFloat(3, rawData.getData());
        return ps;
      }

    });
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public long addViabilityControl(final Control control) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbc.update(new PreparedStatementCreator() {

      public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(INSERT_VIABILITY_CONTROL, Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, control.getPlateId());
        ps.setString(2, control.getIdentifier());
        ps.setFloat(3, control.getData());
        return ps;
      }

    }, keyHolder);
    return keyHolder.getKey().longValue();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void loadRawDataExcel(String runName, List<String> controls, InputStream is) {
    try {
      new ExcelDataReader(this, controls).readRawData(runName, is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void loadLinkedViabilityExcel(long runId, List<String> controls, InputStream is) {
    try {
      new ExcelDataReader(this, controls).readLinkedViability(runId, is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void loadIndependentViabilityExcel(String runName, List<String> controls, InputStream is) {
    try {
      new ExcelDataReader(this, controls).readIndependentViability(runName, is);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public JdbcTemplate getJdbc() {
    return jdbc;
  }

  public void setJdbc(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  private String read(String file) {
    try {
      return Resources.toString(Resources.getResource(file), Charset.defaultCharset());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private class ProcessedDataRowMapper implements RowMapper<NormalizedData> {
    @Override
    public NormalizedData mapRow(ResultSet rs, int rownum) throws SQLException {
      return new NormalizedData(rs.getString("plate_name"), rs.getString("identifier"), rs.getDouble("time_marker"), rs.getFloat("norm"));
    }
  }

  private class ProcessedDataExtractor implements ResultSetExtractor<List<NormalizedRow>> {

    ProcessedDataRowMapper mapper = new ProcessedDataRowMapper();

    @Override
    public List<NormalizedRow> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
      Map<String, NormalizedRow> rows = new TreeMap<String, NormalizedRow>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
          return o1.compareTo(o2);
        }
      });

      while (resultSet.next()) {
        NormalizedData d = mapper.mapRow(resultSet, resultSet.getRow());
        String keyName = d.getPlateName() + "-" + d.getGeneId();
        if (!rows.containsKey(keyName)) {
          rows.put(keyName, new NormalizedRow(d.getPlateName(), d.getGeneId()));
        }
        rows.get(keyName).getData().put(d.getTimeMarker(), d.getNormalized());
      }

      return new LinkedList<NormalizedRow>(rows.values());
    }
  }

  private class RunRowMapper implements RowMapper<Run> {

    public Run mapRow(ResultSet rs, int rownum) throws SQLException {
      return new Run(rs.getLong("id"), rs.getString("run_name"), rs.getDate("create_date"));
    }

  }

  private class PlateRowMapper implements RowMapper<Plate> {

    public Plate mapRow(ResultSet rs, int rownum) throws SQLException {
      return new Plate(rs.getLong("id"), rs.getLong("run_id"), rs.getString("plate_name"), rs.getDate("create_date"));
    }

  }

  private class ZFactorRowMapper implements RowMapper<ZFactor> {

    public ZFactor mapRow(ResultSet rs, int rownum) throws SQLException {
      return new ZFactor(rs.getString("plate_name"), rs.getDouble("time_marker"), rs.getFloat("z_factor"));
    }

  }

}
