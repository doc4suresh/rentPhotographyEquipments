package com.kodak.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RentCartItem extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private long id;
	private int qty;
	private int days;
	private BigDecimal subRentTotal;
	private BigDecimal subDepositTotal;
	private BigDecimal subTotal;
	private String status;

	@OneToOne
	private Item item;

	@ManyToOne
	@JoinColumn(name = "rent_id")
	private Rent rent;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;
}
