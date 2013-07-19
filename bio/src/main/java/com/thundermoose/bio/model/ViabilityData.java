package com.thundermoose.bio.model;

import java.util.Date;

public class ViabilityData {


	private long		id;
	private long		plateId;
	private String	identifier;
	private float		data;
	private Date		createDate;

	public ViabilityData() {

	}

	public ViabilityData(long id, long plateId, String identifier, float data, Date createDate) {
		this.id = id;
		this.plateId = plateId;
		this.identifier = identifier;
		this.data = data;
		this.createDate = createDate;
	}

	public ViabilityData(long plateId, String identifier, float data) {
		this.plateId = plateId;
		this.identifier = identifier;
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
