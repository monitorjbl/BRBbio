package com.thundermoose.bio.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name="raw_data")
public class RawData {

	@Id
	@Generated(GenerationTime.INSERT)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long		id;
	@Column(name="plate_id")
	private long		plateId;
	@Column(name="identifier")
	private String	identifier;
	@Column(name="time_marker")
	private int			timeMarker;
	private float		data;
	@Generated(GenerationTime.INSERT)
	@Column(name="create_date")
	private Date		createDate;

	public RawData() {

	}

	public RawData(long plateId, String identifier, int timeMarker, float data) {
		this.plateId = plateId;
		this.identifier = identifier;
		this.timeMarker = timeMarker;
		this.data = data;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPlateId() {
		return plateId;
	}

	public void setPlateId(long plateId) {
		this.plateId = plateId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getTimeMarker() {
		return timeMarker;
	}

	public void setTimeMarker(int timeMarker) {
		this.timeMarker = timeMarker;
	}

	public float getData() {
		return data;
	}

	public void setData(float data) {
		this.data = data;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	

}
