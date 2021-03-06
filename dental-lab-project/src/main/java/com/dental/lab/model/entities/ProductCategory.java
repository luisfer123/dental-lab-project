package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Only one {@linkplain ProductCategory} can be assign to a {@linkplain Product}
 * at a time; thought, once one {@linkplain ProductCategory} is assigned, the
 * parent {@linkplain ProductCategory} and the parent of the parent, and so on...
 * are associated with the {@linkplain Product} as well. So once one 
 * {@linkplain ProductCategory} is assigned, so are all its predecessors in the
 * category tree. All {@linkplain ProductCategory}s associated with one 
 * {@linkplain Product} must belong to a single pat in the {@linkplain ProductCategory} 
 * tree.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
@Entity
@Table(name = "product_category")
public class ProductCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "depth")
	private int depth;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	private ProductCategory parent;
	
	@JsonIgnore
	@OneToMany(
			mappedBy = "parent",
			fetch = FetchType.EAGER)
	private Set<ProductCategory> children;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "categories")
	private Set<Product> products;

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

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public ProductCategory getParent() {
		return parent;
	}

	public void setParent(ProductCategory parent) {
		this.parent = parent;
	}

	public Set<ProductCategory> getChildren() {
		return children;
	}

	public void setChildren(Set<ProductCategory> children) {
		this.children = children;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(o == null || o.getClass() != getClass())
			return false;
		
		ProductCategory other = (ProductCategory) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return 48;
	}

}
