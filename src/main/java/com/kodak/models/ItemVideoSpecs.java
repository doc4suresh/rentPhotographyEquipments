package com.kodak.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
public class ItemVideoSpecs extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(columnDefinition = "text", length = 1020)
	private String recordingModes;
	@Column(columnDefinition = "text", length = 1020)
	private String externalRecordingModes;
	private String recordingLimit;
	private String videoEncoding;
	private String audioRecording;
	private String audioFileFormat;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
