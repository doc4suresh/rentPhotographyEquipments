package com.kodak.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Branch extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;
	private String name;
	private String address;
	private String phoneNo;

	@JsonIgnore
	@OneToMany(mappedBy = "branch", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private List<Employee> employees;

	@OneToOne(cascade = CascadeType.ALL)
	private Employee manager;

}
