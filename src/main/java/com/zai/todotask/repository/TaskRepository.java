package com.zai.todotask.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zai.todotask.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @SuppressWarnings("null")
    Optional<Task> findById(Long id);
}
