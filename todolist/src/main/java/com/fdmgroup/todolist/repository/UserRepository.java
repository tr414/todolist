/**
 * User repository, extends JPA repository to enable querying of the database.
 * Defines derived queries for the application.
 */
package com.fdmgroup.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.todolist.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
}
