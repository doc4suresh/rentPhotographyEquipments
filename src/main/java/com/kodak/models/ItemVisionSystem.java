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
public class ItemVisionSystem  extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String visionSystem;
	private String obstacleSensoryRange;
	private String forwardFieldOfView;
	private String downwardFieldOfView;
	private String backwardFieldOfView;
	private String operatingEnvironment;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
