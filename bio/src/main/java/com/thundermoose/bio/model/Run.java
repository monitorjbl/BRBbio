package com.thundermoose.bio.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "runs")
public class Run {

	@Id
	@Generated(GenerationTime.INSERT)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long				id;
	@Column(name = "run_name")
	private String			runName;
	@OneToMany(targetEntity = Plate.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "run_id", insertable = false, updatable = false)
	private Set<Plate>	plates	= new HashSet<Plate>();
	@Generated(GenerationTime.INSERT)
	@Column(name = "create_date")
	private Date				createDate;

	public Run() {

	}

	public Run(String runName) {
		this.runName = runName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRunName() {
		return runName;
	}

	public void setRunName(String runName) {
		this.runName = runName;
	}

	public Set<Plate> getPlates() {
		return plates;
	}

	public void setPlates(Set<Plate> plates) {
		this.plates = plates;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
