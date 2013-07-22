package com.thundermoose.bio.model;

public class NormalizedData {

	private String	plateName;
	private int			timeMarker;
	private String	geneId;
	private float		normalized;

	public NormalizedData() {

	}

	public NormalizedData(String plateName, String geneId, int timeMarker, float normalized) {
		this.plateName = plateName;
		this.geneId = geneId;
		this.timeMarker = timeMarker;
		this.normalized = normalized;
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

	public String getGeneId() {
		return geneId;
	}

	public void setGeneId(String geneId) {
		this.geneId = geneId;
	}

	public float getNormalized() {
		return normalized;
	}

	public void setNormalized(float normalized) {
		this.normalized = normalized;
	}

}
