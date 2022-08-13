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
public class ItemFlash extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String builtInFlash;
	private String dedicatedFlashSystem;
	private String externalFlashConnection;
	private String flashModes;
	private String maximumSyncSpeed;
	private String flashCompensation;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
