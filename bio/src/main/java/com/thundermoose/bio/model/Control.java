package com.thundermoose.bio.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Table(name = "controls")
public class Control {

	@Id
	@Generated(GenerationTime.INSERT)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long	id;
	@Column(name = "plate_id")
	private long	plateId;
	@Column(name = "negative_control")
	private float	negativeControl;
	@Column(name = "positive_control")
	private float	positiveControl;
	@Column(name = "time_marker")
	private int		timeMarker;
	@Column(name = "create_date")
	@Generated(GenerationTime.INSERT)
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
