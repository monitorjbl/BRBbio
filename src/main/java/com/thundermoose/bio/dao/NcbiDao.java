package com.thundermoose.bio.dao;

import com.thundermoose.bio.model.Homologue;
import com.thundermoose.bio.model.HomologueData;
import com.thundermoose.bio.model.Taxonomy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.thundermoose.bio.util.Utils.read;

/**
 * Created by Thundermoose on 4/6/2014.
 */
@Component
public class NcbiDao {
  public static final Logger log = LoggerFactory.getLogger(NcbiDao.class);

  public static final String DELETE_TAXONOMY = "DELETE FROM ncbi_taxonomy";
  public static final String DELETE_HOMOLOGUES = "DELETE FROM ncbi_homologue";
  public static final String INSERT_TAXONOMY = "INSERT INTO ncbi_taxonomy (id,name) VALUES(?,?)";
  public static final String INSERT_HOMOLOGUE = "INSERT INTO ncbi_homologue (homologue_id,taxonomy_id,gene_id,gene_symbol) VALUES(?,?,?,?)";
  public static final String GET_ALL_TAXONOMY = "SELECT * FROM ncbi_taxonomy #CLAUSE# ORDER BY name";
  public static final String GET_HOMOLOGUES = "SELECT * FROM ncbi_homologue";

  @Autowired
  JdbcTemplate jdbc;

  @Transactional(propagation = Propagation.REQUIRED)
  public void clearTaxonomyData() {
    jdbc.update(DELETE_TAXONOMY);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void addTaxonomyData(String id, String name) {
    jdbc.update(INSERT_TAXONOMY, id, name);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void clearHomologueData() {
    jdbc.update(DELETE_HOMOLOGUES);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void addHomologueData(String homologueId, String taxId, String geneId, String geneSymbol) {
    try {
      jdbc.update(INSERT_HOMOLOGUE, homologueId, taxId, geneId, geneSymbol);
    } catch (DuplicateKeyException e) {
      log.debug("Ignoring duplicate homologue [" + homologueId + "]");
    }
  }

  public List<Taxonomy> getAllTaxonomies() {
    return jdbc.query(GET_ALL_TAXONOMY.replaceAll("#CLAUSE#", ""), new TaxonomyRowMapper());
  }

  public void getHomologues(final Map<String, String> map) {
    jdbc.query(GET_HOMOLOGUES, new ResultSetExtractor<Void>() {
      @Override
      public Void extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
          map.put(rs.getString("gene_id"), rs.getString("homologue_id"));
        }
        return null;
      }
    });
  }

  public Taxonomy getTaxonomy(String taxonomyId) {
    return jdbc.queryForObject(GET_ALL_TAXONOMY.replaceAll("#CLAUSE#", "WHERE id = ?"), new TaxonomyRowMapper(), taxonomyId);
  }

  private class TaxonomyRowMapper implements RowMapper<Taxonomy> {
    @Override
    public Taxonomy mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Taxonomy(rs.getString("id"), rs.getString("name"));
    }
  }

  private class HomologueRowMapper implements RowMapper<Homologue> {
    @Override
    public Homologue mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new Homologue(rs.getString("gene_id"), rs.getString("gene_symbol"), rs.getString("h_gene_id"), rs.getString("h_gene_symbol"));
    }
  }
}
