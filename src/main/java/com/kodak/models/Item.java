package com.kodak.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.kodak.utility.FilePath;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Item extends Auditable<String> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String itemCode;
	private String itemName;
	@Column(columnDefinition = "text", length = 1020)
	private String keyFeatures;
	@Column(columnDefinition = "text", length = 7650)
	private String overview;
	private int qty;
	private double rentPrice1To2Days;
	private double rentPrice3To4Days;
	private double rentPrice5Days;
	private double deposit1To2Days;
	private double deposit3To4Days;
	private double deposit5Days;
	private int available;
	private boolean active = true;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	private Brand brand;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private Item3dObjectDetection item3dObjectDetection;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemBattery itemBattery;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemCameraSpecs itemCameraSpecs;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemCharger itemCharger;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemExposureControl itemExposureControl;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemFlash itemFlash;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemFlightControlSystem itemFlightControlSystem;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemFocus itemFocus;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemGeneralSpecs itemGeneralSpecs;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemImaging itemImaging;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemInterface itemInterface;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemPackagingInfo itemPackagingInfo;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemPerformance itemPerformance;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemRemoteControllerOrTransmitter itemRemoteControllerOrTransmitter;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemVideoSpecs itemVideoSpecs;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemViewfinderMonitor itemViewfinderMonitor;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "item")
	private ItemVisionSystem itemVisionSystem;

	@Transient
	public String getItemImg() {
		return "/" + FilePath.getItemImgPath() + getId() + ".png";
	}
}
