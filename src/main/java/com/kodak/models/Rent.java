package com.kodak.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
public class Rent extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private BigDecimal grandRentTotal;
	private BigDecimal grandDepositTotal;
	private BigDecimal grandTotal;
	private LocalDate returnedDate;
	private boolean itemsReturned;
	private boolean depositReturned;
	private BigDecimal lateChargers;
	private BigDecimal damageFine;
	private String status;

	@OneToMany(mappedBy = "rent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<RentCartItem> rentCartItemList;

	@OneToOne(cascade = CascadeType.ALL)
	private Employee employee;

	@OneToOne(cascade = CascadeType.ALL)
	private Customer customer;
}
