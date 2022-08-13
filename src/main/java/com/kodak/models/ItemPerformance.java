package com.kodak.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ItemPerformance extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String maximumTakeoffWeight;
	private String maximumHorizontalSpeed;
	private String maximumAscentSpeed;
	private String maximumDescentSpeed;
	private String maximumWindResistance;
	private String flightCeiling;
	private String maximumFlightTime;
	private String maximumHoverTime;
	private String maximumTiltAngle;
	private String hoveringAccuracy;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
