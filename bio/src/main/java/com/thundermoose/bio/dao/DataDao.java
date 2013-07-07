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

import com.google.common.io.Resources;
import com.thundermoose.bio.excel.RawDataReader;
import com.thundermoose.bio.model.Control;
import com.thundermoose.bio.model.Plate;
import com.thundermoose.bio.model.ProcessedData;
import com.thundermoose.bio.model.RawData;
import com.thundermoose.bio.model.Run;

public class DataDao {

	private static final String	RUN_SQL					= "sql/run.sql";
	private static final String	PLATE_SQL				= "sql/plate.sql";
	private static final String	REPORT_SQL			= "sql/report.sql";
	private static final String	INSERT_RUN			= "INSERT INTO runs (run_name) VALUES(?)";
	private static final String	INSERT_PLATE		= "INSERT INTO plates (run_id,plate_name) VALUES(?,?)";
	private static final String	INSERT_CONTROL	= "INSERT INTO controls (plate_id,control_type,time_marker,data) VALUES(?,?,?,?)";
	private static final String	INSERT_RAW_DATA	= "INSERT INTO raw_data (plate_id,identifier,time_marker,data) VALUES(?,?,?,?)";

	@Autowired
	private JdbcTemplate				jdbc;

	public List<Plate> getPlates() {
		return jdbc.query(read(PLATE_SQL), new PlateRowMapper());
	}

	public List<Run> getRuns() {
		return jdbc.query(read(RUN_SQL), new RunRowMapper());
	}

	public Plate getPlateById(long plateId) {
		return jdbc.queryForObject(read(PLATE_SQL), new PlateRowMapper());
	}

	public Run getRunById(long runId) {
		return jdbc.queryForObject(read(RUN_SQL), new RunRowMapper());
	}

	public List<ProcessedData> getProcessedDataByRunId(long runId) {
		return jdbc.query(read(REPORT_SQL), new Object[] { runId }, new ProcessedDataRowMapper());
	}

	public long addRun(final Run run) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(INSERT_RUN, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, run.getRunName());
				return ps;
			}

		},keyHolder);
		return keyHolder.getKey().longValue();
	}

	public long addPlate(final Plate plate) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(INSERT_PLATE, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, plate.getRunId());
				ps.setString(2, plate.getPlateName());
				return ps;
			}

		},keyHolder);
		return keyHolder.getKey().longValue();
	}

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

		},keyHolder);
		return keyHolder.getKey().longValue();
	}

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

	public void loadExcel(String runName, InputStream is) {
		try {
			new RawDataReader(this).readExcel(runName, is);
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

	private class ProcessedDataRowMapper implements RowMapper<ProcessedData> {

		public ProcessedData mapRow(ResultSet rs, int rownum) throws SQLException {
			return new ProcessedData(rs.getString("plate_name"), rs.getString("identifier"), rs.getInt("time_marker"), rs.getFloat("norm"));
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

	/*private class RawDataRowMapper implements RowMapper<RawData> {

		public RawData mapRow(ResultSet rs, int rownum) throws SQLException {
			return new RawData(rs.getLong("id"), rs.getLong("plate_id"), rs.getString("identifier"), rs.getInt("time_marker"), rs.getFloat("data"), rs.getDate("create_date"));
		}

	}*/

}
