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
public class ItemFocus extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String focusType;
	private String focusMode;
	private String autofocusPoints;
	private String autofocusSensitivity;

	@OneToOne(cascade = CascadeType.ALL)
	private Item item;
}
