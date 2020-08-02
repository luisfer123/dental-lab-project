package com.dental.lab.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dental.lab.model.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsername(String username);

}
