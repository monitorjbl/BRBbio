package com.thundermoose.bio.model;

import java.util.Date;

public class Plate {
	private long		id;
	private String	plateId;
	private float		negativeControl;
	private float		positiveControl;
	private int			timeMarker;
	private Date		createDate;

	public Plate() {
	}

	public Plate(String plateId, int timeMarker, float negativeControl, float positiveControl) {
		this.plateId = plateId;
		this.negativeControl = negativeControl;
		this.positiveControl = positiveControl;
		this.timeMarker = timeMarker;
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

	public float getNegativeControl() {
		return negativeControl;
	}

	public void setNegativeControl(float negativeControl) {
		this.negativeControl = negativeControl;
	}

	public float getPositiveControl() {
		return positiveControl;
	}

	public void setPositiveControl(float positiveControl) {
		this.positiveControl = positiveControl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getTimeMarker() {
		return timeMarker;
	}

	public void setTimeMarker(int timeMarker) {
		this.timeMarker = timeMarker;
	}

}
