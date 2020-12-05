package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "notes")
	private String notes;
	
	@Lob
	@Column(name = "product_image")
	private byte[] productImage;
	
	@ManyToMany
	@JoinTable(
			name = "product_has_category",
			joinColumns = @JoinColumn(name = "product_id"),
			inverseJoinColumns = @JoinColumn(name = "product_category_id"))
	private Set<ProductCategory> categories;
	
	@OneToMany(mappedBy = "product")
	private Set<ProductPricing> productPricings;
	
	public void addCategory(ProductCategory category) {
		categories.add(category);
		category.getProducts().add(this);
	}
	
	public void removeCategory(ProductCategory category) {
		categories.remove(category);
		category.getProducts().remove(this);
	}
	
	/* ============================ Getters and Setters =============================== */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Set<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(Set<ProductCategory> categories) {
		this.categories = categories;
	}
	
	public byte[] getProductImage() {
		return productImage;
	}

	public void setProductImage(byte[] productImage) {
		this.productImage = productImage;
	}

	public Set<ProductPricing> getProductPricings() {
		return productPricings;
	}

	public void setProductPricings(Set<ProductPricing> productPricings) {
		this.productPricings = productPricings;
	}

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(o == null || o.getClass() != getClass())
			return false;
		
		Product other = (Product) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return 98;
	}

}
