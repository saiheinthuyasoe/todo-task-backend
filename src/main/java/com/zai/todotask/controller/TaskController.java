package com.zai.todotask.controller;

import com.zai.todotask.model.Task;
import com.zai.todotask.service.TaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing tasks.
 * Provides CRUD operations along with custom actions like toggling star and
 * completion status.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // Logger for logging messages to the console and log file (if configured).
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    /**
     * Constructor-based dependency injection.
     *
     * @param taskService Task service
     */
    // private final TaskService taskService;
    // public TaskController(TaskService taskService) {
    // this.taskService = taskService;
    // }
    // OR

    @Autowired
    private TaskService taskService;

    /**
     * Get all tasks.
     *
     * @return List of all tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("Fetching all tasks");
        List<Task> tasks = taskService.getAllTasks();
        logger.debug("Fetched {} tasks", tasks.size());
        return ResponseEntity.ok(tasks);

        // OR
        // List<Task> tasks = taskService.getAllTasks();
        // return ResponseEntity.ok(tasks);
    }

    /**
     * Get a task by its ID.
     *
     * @param id Task ID
     * @return The task if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("Fetching task with ID: {}", id);
        return taskService.getTaskById(id)
                .map(task -> {
                    logger.debug("Task found: {}", task);
                    return ResponseEntity.ok(task);
                })
                .orElseGet(() -> {
                    logger.warn("Task with ID {} not found", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });

        // OR
        // return taskService.getTaskById(id)
        // .map(ResponseEntity::ok)
        // .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
        // .body(null));
    }

    /**
     * Create a new task.
     *
     * @param task Task details
     * @return The created task
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        logger.info("Creating a new task: {}", task);
        Task createdTask = taskService.createTask(task);
        logger.debug("Task created successfully with ID: {}", createdTask.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);

        // OR
        // Task createdTask = taskService.createTask(task);
        // return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Update an existing task.
     *
     * @param id          Task ID
     * @param taskDetails Updated task details
     * @return The updated task, or 404 Not Found if the task doesn't exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        // logger.info("Updating task with ID: {}", id);
        // return taskService.getTaskById(id)
        // .map(existingTask -> {
        // Task updatedTask = taskService.updateTask(id, taskDetails);
        // logger.debug("Task updated successfully: {}", updatedTask);
        // return ResponseEntity.ok(updatedTask);
        // })
        // .orElseGet(() -> {
        // logger.warn("Task with ID {} not found for update", id);
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        // });

        // OR
        // return taskService.getTaskById(id)
        // .map(existingTask -> ResponseEntity.ok(taskService.updateTask(id,
        // taskDetails)))
        // .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // OR (Update only the fields that are present in the updatedTask object)
        
    //     Task task = taskService.getTaskById(id).orElseThrow(() -> new NoSuchElementException("Task not found"));
    //     // Update only the fields that are present in the updatedTask object
    //     if (taskDetails.getTitle() != null) {
    //         task.setTitle(taskDetails.getTitle());
    //     }
    //     if (taskDetails.getDescription() != null) {
    //         task.setDescription(taskDetails.getDescription());
    //     }
    //     // Preserve existing fields like 'starred' and 'completed'

    //     Task savedTask = taskService.updateTask(id, task);
    //     return ResponseEntity.ok(savedTask);

    // OR  (Update only the fields that are present in the taskDetails object with logger)

    logger.info("Updating task with ID: {}", id);

    return taskService.getTaskById(id)
            .map(existingTask -> {
                // Update only the fields that are present in the taskDetails object
                if (taskDetails.getTitle() != null) {
                    existingTask.setTitle(taskDetails.getTitle());
                }
                if (taskDetails.getDescription() != null) {
                    existingTask.setDescription(taskDetails.getDescription());
                }
                // Preserve existing fields like 'starred' and 'completed'

                Task updatedTask = taskService.updateTask(id, existingTask);
                logger.debug("Task updated successfully: {}", updatedTask);
                return ResponseEntity.ok(updatedTask);
            })
            .orElseGet(() -> {
                logger.warn("Task with ID {} not found for update", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            });
    }
    



    /**
     * Delete a task by its ID.
     *
     * @param id Task ID
     * @return 204 No Content if successful, or 404 Not Found if the task doesn't
     *         exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("Deleting task with ID: {}", id);
        if (taskService.getTaskById(id).isPresent()) {
            taskService.deleteTask(id);
            logger.debug("Task with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Task with ID {} not found for deletion", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // OR
        // if (taskService.getTaskById(id).isPresent()) {
        // taskService.deleteTask(id);
        // return ResponseEntity.noContent().build();
        // } else {
        // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        // }
    }

    /**
     * Delete all tasks.
     *
     * @return 204 No Content after deleting all tasks
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAllTasks() {
        logger.info("Deleting all tasks");
        taskService.deleteAllTasks();
        logger.debug("All tasks deleted successfully");
        return ResponseEntity.noContent().build();

        // OR
        // taskService.deleteAllTasks();
        // return ResponseEntity.noContent().build();
    }

    /**
     * Toggle the "star" status of a task.
     *
     * @param id Task ID
     * @return The updated task, or 404 Not Found if the task doesn't exist
     */
    @PutMapping("/{id}/star")
    public ResponseEntity<Task> toggleStar(@PathVariable Long id) {
        logger.info("Toggling star status for task with ID: {}", id);
        return taskService.getTaskById(id)
                .map(task -> {
                    Task updatedTask = taskService.toggleStar(id);
                    logger.debug("Star status toggled for task: {}", updatedTask);
                    return ResponseEntity.ok(updatedTask);
                })
                .orElseGet(() -> {
                    logger.warn("Task with ID {} not found for toggling star status", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });

        // OR
        // return taskService.getTaskById(id)
        // .map(task -> ResponseEntity.ok(taskService.toggleStar(id)))
        // .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Toggle the "complete" status of a task.
     *
     * @param id Task ID
     * @return The updated task, or 404 Not Found if the task doesn't exist
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> toggleComplete(@PathVariable Long id) {
        logger.info("Toggling complete status for task with ID: {}", id);
        return taskService.getTaskById(id)
                .map(task -> {
                    Task updatedTask = taskService.toggleComplete(id);
                    logger.debug("Complete status toggled for task: {}", updatedTask);
                    return ResponseEntity.ok(updatedTask);
                })
                .orElseGet(() -> {
                    logger.warn("Task with ID {} not found for toggling complete status", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });

        // OR
        // return taskService.getTaskById(id)
        // .map(task -> ResponseEntity.ok(taskService.toggleComplete(id)))
        // .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
