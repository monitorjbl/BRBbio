package com.thundermoose.bio.model;

import java.util.Date;

public class Control {

	private long	id;
	private long	plateId;
	private float	negativeControl;
	private float	positiveControl;
	private int		timeMarker;
	private Date	createDate;

	public Control() {

	}

	public Control(long plateId, int timeMarker, float negativeControl, float positiveControl) {
		super();
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

	public long getPlateId() {
		return plateId;
	}

	public void setPlateId(long plateId) {
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

}
