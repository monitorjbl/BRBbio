package com.thundermoose.bio.model;

import java.util.Date;

public class Run {

	private long id;
	private String runName;
	private boolean viabilityOnly;
	private Date createDate;

	public Run() {

	}

	public Run(long id, String runName, Date createDate) {
		this.id = id;
		this.runName = runName;
		this.createDate = createDate;
	}

	public Run(String runName, boolean viabilityOnly) {
		this.runName = runName;
		this.setViabilityOnly(viabilityOnly);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRunName() {
		return runName;
	}

	public void setRunName(String runName) {
		this.runName = runName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public boolean getViabilityOnly() {
		return viabilityOnly;
	}

	public void setViabilityOnly(boolean viabilityOnly) {
		this.viabilityOnly = viabilityOnly;
	}

}
