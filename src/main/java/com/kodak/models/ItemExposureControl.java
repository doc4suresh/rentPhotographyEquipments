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
public class ItemExposureControl extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String exposureControl;
	private String exposureFlashModes;
	private String isoSensitivity;
	private String meteringMethod;
	private String exposureModes;
	private String exposureCompensation;
	private String meteringRange;
	private String whiteBalance;
	private String selfTimer;
	private String shutterSpeed;
	private String continuousShooting;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
