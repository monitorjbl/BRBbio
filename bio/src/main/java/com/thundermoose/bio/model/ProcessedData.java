package com.thundermoose.bio.model;

public class ProcessedData {

	private long		id;
	private String	plateName;
	private int			timeMarker;
	private float		positiveControl;
	private float		negativeControl;

	public ProcessedData() {

	}

	public ProcessedData(long id, String plateName, int timeMarker, float positiveControl, float negativeControl) {
		this.id = id;
		this.plateName = plateName;
		this.timeMarker = timeMarker;
		this.positiveControl = positiveControl;
		this.negativeControl = negativeControl;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlateName() {
		return plateName;
	}

	public void setPlateName(String plateName) {
		this.plateName = plateName;
	}

	public int getTimeMarker() {
		return timeMarker;
	}

	public void setTimeMarker(int timeMarker) {
		this.timeMarker = timeMarker;
	}

	public float getPositiveControl() {
		return positiveControl;
	}

	public void setPositiveControl(float positiveControl) {
		this.positiveControl = positiveControl;
	}

	public float getNegativeControl() {
		return negativeControl;
	}

	public void setNegativeControl(float negativeControl) {
		this.negativeControl = negativeControl;
	}

}
