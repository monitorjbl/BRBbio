package com.thundermoose.bio.model;

import java.util.Date;

public class Control {

	private long id;
	private long plateId;
	private String identifier;
	private double timeMarker;
	private float data;

	private Date createDate;

	public Control() {

	}

	public Control(long id, long plateId, String identifier, double timeMarker, float data, Date createDate) {
		this.id = id;
		this.plateId = plateId;
		this.identifier = identifier;
		this.timeMarker = timeMarker;
		this.data = data;
		this.createDate = createDate;
	}
	
	public Control(long plateId, String identifier, float data, Date createDate) {
		this.plateId = plateId;
		this.identifier = identifier;
		this.data = data;
		this.createDate = createDate;
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

	public double getTimeMarker() {
		return timeMarker;
	}

	public void setTimeMarker(double timeMarker) {
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
