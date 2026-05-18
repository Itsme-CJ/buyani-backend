package com.buyani.buyaniserver.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[product_item]")
public class ProductItem extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_item_id")
	private Integer productItemId;

	@Column(name = "name")
	private String name;

	@Column(name = "generic_name")
	private String genericName;

	@Column(name = "description")
	private String description;

	@Lob
	@Column(name = "image", columnDefinition = "LONGTEXT")
	private String image;

	@Column(name = "product_number")
	private String productNumber;

	@Column(name = "price")
	private Float price;

	@Column(name = "status")
	private String status;

  @Column(name = "type")
	private String type;

	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "stock")
	private Integer stock;

	@Column(name = "critical_level")
	private Integer criticalLevel;

	@Column(name = "with_prescription")
	private Integer withPrescription;

	@JsonIgnore
  @ManyToOne(optional = true)
  @JoinColumn(name = "product_category_id")
	private ProductCategory productCategory;
}
