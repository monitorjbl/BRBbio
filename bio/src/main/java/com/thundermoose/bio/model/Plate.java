package com.thundermoose.bio.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Plate {
	private long					id;
	private long					runId;
	private String				plateName;
	private Date					createDate;
	private List<Control>	controls	= new ArrayList<Control>();

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

	public List<Control> getControls() {
		return controls;
	}

	public void setControls(List<Control> controls) {
		this.controls = controls;
	}

}
