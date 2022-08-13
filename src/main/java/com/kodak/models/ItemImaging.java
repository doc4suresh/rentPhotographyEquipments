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
public class ItemImaging extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String lensMount;
	private String cameraFormat;
	private String pixels;
	private String maximumResolution;
	private String aspectRatio;
	private String sensorType;
	private String sensorSize;
	private String imageFileFormat;
	private String bitDepth;
	private String imageStabilization;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;

}
