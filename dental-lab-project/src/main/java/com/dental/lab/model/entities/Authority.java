package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dental.lab.model.enums.EAuthority;

@Entity
@Table(name = "Authorities")
public class Authority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "authority")
	@Enumerated(EnumType.STRING)
	private EAuthority authority;
	
	@Column(name = "description")
	private String description;
	
	@ManyToMany(mappedBy = "authorities")
	Set<User> users;

	public Authority() {
		super();
	}

	public Authority(EAuthority authority) {
		super();
		this.authority = authority;
	}

	public EAuthority getAuthority() {
		return authority;
	}

	public void setAuthority(EAuthority authority) {
		this.authority = authority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
