package com.thundermoose.bio.model;

import java.util.Date;

public class RawData {

	private long		id;
	private String	plateId;
	private String	identifier;
	private int			timeMarker;
	private float		data;
	private Date		createDate;

	public RawData() {

	}

	public RawData(String plateId, String identifier, int timeMarker, float data) {
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

	public String getPlateId() {
		return plateId;
	}

	public void setPlateId(String plateId) {
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public float getData() {
		return data;
	}

	public void setData(float data) {
		this.data = data;
	}

}
