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
public class ItemViewfinderMonitor extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String viewfinderType;
	private String viewfinderSize;
	private String viewfinderResolution;
	private String viewfinderEyePoint;
	private String viewfinderCoverage;
	private String viewfinderMagnification;
	private String diopterAdjustment;
	private String monitorSize;
	private String monitorResolution;
	private String monitorType;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
