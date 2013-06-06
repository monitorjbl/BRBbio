package com.thudermoose.bio.model;

import java.util.Date;

public class Plate {
	private String	id;
	private float		negativeControl;
	private float		positiveControl;
	private Date		createDate;

	public Plate() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Plate(String id, float negativeControl, float positiveControl) {
		this.id = id;
		this.negativeControl = negativeControl;
		this.positiveControl = positiveControl;
	}

}
