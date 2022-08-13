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
public class ItemCameraSpecs extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String sensor;
	private String effectivePixels;
	private String lensFieldofView;
	private String aperture;
	private String minimumFocusingDistance;
	private String photoISORange;
	private String electronicShutterSpeed;
	private String photoFormat;
	private String videoFormat;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
