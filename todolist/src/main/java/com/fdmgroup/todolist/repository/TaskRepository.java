/**
 * Task repository, extends JPA repository to enable querying of the database.
 * Defines derived queries for the application.
 */
package com.fdmgroup.todolist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdmgroup.todolist.model.Task;
import com.fdmgroup.todolist.model.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Optional<Task> findByTaskName(String taskNAme);
	List<Task> findByUserIs(User user);
	List<Task> findByDoneTrueAndUserIs(User user);
	List<Task> findByDoneFalseAndUserIs(User user);
	List<Task> findByDoneFalseAndUrgentTrueAndUserIs(User user);
	List<Task> findByDoneFalseAndImportantTrueAndUserIs(User user);
	List<Task> findByDoneFalseAndUrgentTrueAndImportantTrueAndUserIs(User user);
	List<Task> findByUserIsAndDoneIsAndUrgentIsAndImportantIs(User user, boolean done, boolean urgent, boolean important);
}
