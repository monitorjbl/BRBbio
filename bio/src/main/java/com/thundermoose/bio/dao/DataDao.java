package com.thundermoose.bio.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
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

public class DataDao {

	private static final String RUN_SQL = "sql/run.sql";
	private static final String PLATE_SQL = "sql/plate.sql";
	private static final String NORMALIZE_SQL = "sql/normalize.sql";
	private static final String ZFACTOR_SQL = "sql/zfactor.sql";
	private static final String VIABILITY_SQL = "sql/viability.sql";

	private static final String INSERT_RUN = "INSERT INTO runs (run_name) VALUES(?)";
	private static final String INSERT_PLATE = "INSERT INTO plates (run_id,plate_name) VALUES(?,?)";
	private static final String INSERT_CONTROL = "INSERT INTO controls (plate_id,control_type,time_marker,data) VALUES(?,?,?,?)";
	private static final String INSERT_RAW_DATA = "INSERT INTO raw_data (plate_id,identifier,time_marker,data) VALUES(?,?,?,?)";
	private static final String INSERT_VIABILITY_DATA = "INSERT INTO cell_viability (plate_id,identifier,data) VALUES(?,?,?)";

	private static final String DELETE_VIABILITY_DATA = "DELETE FROM cell_viability WHERE plate_id IN (SELECT id FROM plates WHERE run_id = ?)";
	private static final String DELETE_RAW_DATA = "DELETE FROM raw_data WHERE plate_id IN (SELECT id FROM plates WHERE run_id = ?)";
	private static final String DELETE_CONTROLS = "DELETE FROM controls WHERE plate_id IN (SELECT id FROM plates WHERE run_id = ?)";
	private static final String DELETE_PLATES = "DELETE FROM plates WHERE run_id = ?";
	private static final String DELETE_RUN = "DELETE FROM runs WHERE id = ?";

	@Autowired
	private JdbcTemplate jdbc;

	public List<Plate> getPlates() {
		return jdbc.query(read(PLATE_SQL), new PlateRowMapper());
	}

	public List<Run> getRuns() {
		return jdbc.query(read(RUN_SQL), new RunRowMapper());
	}

	public Plate getPlateById(long plateId) {
		return jdbc.queryForObject(read(PLATE_SQL) + " WHERE id=?", new PlateRowMapper());
	}

	public Plate getPlateByName(long runId, String plateName) {
		return jdbc.queryForObject(read(PLATE_SQL) + " WHERE run_id=? AND plate_name=?", new Object[] { runId, plateName }, new PlateRowMapper());
	}

	public Run getRunById(long runId) {
		return jdbc.queryForObject(read(RUN_SQL) + " WHERE id = ?", new Object[] { runId }, new RunRowMapper());
	}

	public List<NormalizedData> getNormalizedDataByRunId(long runId) {
		return jdbc.query(read(NORMALIZE_SQL), new Object[] { runId }, new ProcessedDataRowMapper());
	}

	public List<ZFactor> getZFactorsByRunId(long runId) {
		return jdbc.query(read(ZFACTOR_SQL), new Object[] { runId }, new ZFactorRowMapper());
	}

	public List<NormalizedData> getViabilityByRunId(long runId) {
		return jdbc.query(read(VIABILITY_SQL), new Object[] { runId }, new ProcessedDataRowMapper());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRun(long runId) {
		jdbc.update(DELETE_VIABILITY_DATA, new Object[] { runId });
		jdbc.update(DELETE_RAW_DATA, new Object[] { runId });
		jdbc.update(DELETE_CONTROLS, new Object[] { runId });
		jdbc.update(DELETE_PLATES, new Object[] { runId });
		jdbc.update(DELETE_RUN, new Object[] { runId });
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public long addRun(final Run run) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(INSERT_RUN, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, run.getRunName());
				return ps;
			}

		}, keyHolder);
		return keyHolder.getKey().longValue();
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
	public long addControl(final Control control) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(INSERT_CONTROL, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, control.getPlateId());
				ps.setString(2, control.getControlType());
				ps.setInt(3, control.getTimeMarker());
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
				ps.setInt(3, rawData.getTimeMarker());
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
	public void loadRawDataExcel(String runName, InputStream is) {
		try {
			new ExcelDataReader(this).readRawData(runName, is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void loadViabilityExcel(long runId, InputStream is) {
		try {
			new ExcelDataReader(this).readViability(runId, is);
		} catch (Exception e) {
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

		public NormalizedData mapRow(ResultSet rs, int rownum) throws SQLException {
			return new NormalizedData(rs.getString("plate_name"), rs.getString("identifier"), rs.getInt("time_marker"), rs.getFloat("norm"));
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
			return new ZFactor(rs.getString("plate_name"), rs.getInt("time_marker"), rs.getFloat("z_factor"));
		}

	}

	/*
	 * private class RawDataRowMapper implements RowMapper<RawData> {
	 * 
	 * public RawData mapRow(ResultSet rs, int rownum) throws SQLException {
	 * return new RawData(rs.getLong("id"), rs.getLong("plate_id"),
	 * rs.getString("identifier"), rs.getInt("time_marker"), rs.getFloat("data"),
	 * rs.getDate("create_date")); }
	 * 
	 * }
	 */

}
