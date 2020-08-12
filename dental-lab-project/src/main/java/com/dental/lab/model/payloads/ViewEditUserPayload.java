package com.dental.lab.model.payloads;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;

public class ViewEditUserPayload {
	
	private Long id;
	
	private String username;
	
	private String email;
	
	private String firstName;
	
	private String firstLastName;
	
	private String secondLastName;
	
	List<String> authorities;
	
	List<String> authoritiesComplement;
	
	private String profilePicture;
	
	private ViewEditUserPayload() { }
	
	public static ViewEditUserPayload build(User user) {
		
		ViewEditUserPayload userPayload = new ViewEditUserPayload();
		
		userPayload.id = user.getId();
		userPayload.username = user.getUsername();
		userPayload.email = user.getEmail();
		userPayload.firstName = user.getFirstName();
		userPayload.firstLastName = user.getFirstLastName();
		userPayload.secondLastName = user.getSecondLastName();
		
		List<String> authorities = user.getAuthorities()
				.stream()
				.map(authority -> authority.getAuthority().toString())
				.collect(Collectors.toList());
		userPayload.authorities = authorities;
		
		List<String> authoritiesCompl = new ArrayList<String>();
		for(EAuthority authority: EAuthority.values()) {
			if(!authorities.contains(authority.toString())) {
				authoritiesCompl.add(authority.toString());
			}
		}
		userPayload.authoritiesComplement = authoritiesCompl;
		
		if(user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
			userPayload.profilePicture = 
					Base64.getEncoder()
					.encodeToString(user.getProfilePicture());
		} else {
			userPayload.profilePicture = null;
		}
		
		return userPayload;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getFirstLastName() {
		return firstLastName;
	}

	public String getSecondLastName() {
		return secondLastName;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public List<String> getAuthoritiesComplement() {
		return authoritiesComplement;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

}
