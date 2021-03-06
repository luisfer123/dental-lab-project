package com.dental.lab.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.dental.lab.model.validation.UniqueEmail;
import com.dental.lab.model.validation.UniqueUsername;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@NotEmpty()
	@UniqueUsername
	@Column(name = "username", nullable = false, unique = true)
	private String username;
	
	@Email
	@NotNull
	@UniqueEmail
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "first_last_name")
	private String firstLastName;
	
	@Column(name = "second_last_name")
	private String secondLastName;
	
	@Column(name = "enabled", columnDefinition = "TINYINT(1)")
	private boolean enabled;
	
	@Lob
	@Column(name = "profile_picture")
	@Basic(fetch = FetchType.EAGER)
	private byte[] profilePicture;
	
	/*
	 * When it is needed to send authorities collection to the front end,
	 * ViewUserPayload class should be used instead.
	 */
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "user_has_authorities",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "authorities_id"))
	private Set<Authority> authorities = new HashSet<Authority>();
	
	@OneToOne(
			mappedBy = "user",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	private Dentist dentist;
	
	public User() {
		super();
	}

	public User(@NotEmpty String username, @Email @NotNull String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User(@NotEmpty String username, @Email @NotNull String email, String password, String firstName,
			String firstLastName, String secondLastName) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.firstLastName = firstLastName;
		this.secondLastName = secondLastName;
	}
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
		authority.getUsers().add(this);
	}
	
	public void removeAuthority(Authority authority) {
		this.authorities.remove(authority);
		authority.getUsers().remove(this);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstLastName() {
		return firstLastName;
	}

	public void setFirstLastName(String firstLastName) {
		this.firstLastName = firstLastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public void setSecondLastName(String secondLastName) {
		this.secondLastName = secondLastName;
	}

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void enable() {
		this.enabled = true;
	}
	
	public void disable() {
		this.enabled = false;
	}

	public Dentist getDentist() {
		return dentist;
	}

	public void setDentist(Dentist dentist) {
		this.dentist = dentist;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		
		if(o == null || o.getClass() != getClass())
			return false;
		
		User other = (User) o;
		return id != null &&
				id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return 33;
	}

}
