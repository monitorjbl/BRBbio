package com.thundermoose.bio.dao;

import com.thundermoose.bio.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class SystemDao {

  private static final String GET_KEY = "SELECT property_key FROM hts_system_info WHERE property_key = ?";
  private static final String GET_PROPERTY = "SELECT property_value FROM hts_system_info WHERE property_key = ?";
  private static final String CREATE_PROPERTY = "INSERT INTO hts_system_info (property_key,property_value) VALUES(?,?)";
  private static final String SET_PROPERTY = "UPDATE hts_system_info SET property_value=? WHERE property_key = ?";

  @Autowired
  private JdbcTemplate jdbc;

  public boolean doesKeyExist(String key) {
    return jdbc.queryForList(GET_KEY, new Object[]{key}, String.class).size() > 0;
  }

  public String getSystemProperty(String key) {
    List<String> list = jdbc.queryForList(GET_PROPERTY, new Object[]{key}, String.class);
    return list.size() == 0 ? null : list.get(0);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void setSystemProperty(String key, String value) {
    try {
      jdbc.update(CREATE_PROPERTY, key, value);
    } catch (Exception e) {
      jdbc.update(SET_PROPERTY, value, key);
    }
  }
}
