package com.buyani.buyaniserver.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Inheritance
@Entity
@Table(name = "[product_category]")
public class ProductCategory extends Abstract {
  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_category_id")
	private Integer productCategoryId;

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "status")
	private String status;

  @Column(name = "type")
	private String type;

	@Column(name = "item_number")
	private Integer itemNumber;

	@JsonIgnore
  @ManyToOne
  @JoinColumn(name = "store_id")
	private Store store;

	@OneToMany(mappedBy = "productCategory")
	private List<ProductItem> productItems;
}
