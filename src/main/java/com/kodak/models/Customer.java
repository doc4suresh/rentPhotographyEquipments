package com.kodak.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.kodak.utility.FilePath;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Customer extends Auditable<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	@Column(name = "firstName", nullable = false)
	private String firstName;
	private String lastName;
	private String nic;
	private String mobile;
	private String mobile2;
	private String address;
	private String billingAddress;
	private boolean active = true;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
	private Rent rent;

	@Transient
	public String getNicFaceImg() {
		return "/" + FilePath.getNicFilePath() + getNic() + "_face.png";
	}

	@Transient
	public String getNicBackImg() {
		return "/" + FilePath.getNicFilePath() + getNic() + "_back.png";
	}
}
