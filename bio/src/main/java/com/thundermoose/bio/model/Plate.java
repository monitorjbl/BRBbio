package com.thundermoose.bio.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "plates")
public class Plate {
	@Id
	@Generated(GenerationTime.INSERT)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long					id;
	@Column(name = "run_id")
	private long					runId;
	@Column(name = "plate_name")
	private String				plateName;
	@Column(name = "create_date")
	@Generated(GenerationTime.INSERT)
	private Date					createDate;
	@OneToMany(targetEntity = Control.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "plate_id", insertable = false, updatable = false)
	private Set<Control>	controls	= new HashSet<Control>();
	@OneToMany(targetEntity = RawData.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "plate_id", insertable = false, updatable = false)
	private Set<RawData>	rawData		= new HashSet<RawData>();

	public Plate() {
	}

	public Plate(long runId, String plateName) {
		this.runId = runId;
		this.plateName = plateName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRunId() {
		return runId;
	}

	public void setRunId(long runId) {
		this.runId = runId;
	}

	public String getPlateName() {
		return plateName;
	}

	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set<Control> getControls() {
		return controls;
	}

	public void setControls(Set<Control> controls) {
		this.controls = controls;
	}

	public Set<RawData> getRawData() {
		return rawData;
	}

	public void setRawData(Set<RawData> rawData) {
		this.rawData = rawData;
	}

}
