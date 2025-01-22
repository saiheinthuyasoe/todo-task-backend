package com.zai.todotask.service;

import com.zai.todotask.model.Task;
import com.zai.todotask.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    /**
     * Constructor-based dependency injection.
     *
     * @param taskService Task service
     */
    // private final taskRepository taskRepository;
    // public TaskRepository(TaskRepository taskRepository) {
    //     this.taskRepository = taskRepository;
    // }
    // OR

    @Autowired
    private TaskRepository taskRepository;

    // GET get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // GET get task by id
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // POST create task
    public Task createTask(@Valid @RequestBody Task task) {
        return taskRepository.save(task);
    }

    // PUT update task
    public Task updateTask(Long id, @Valid @RequestBody Task taskDetails) {
        // Task task = taskRepository.findById(id).orElsethrow(() -> new RuntimeException("Task not found"));
        // task.setTitle(taskDetails.getTitle());
        // task.setDescription(taskDetails.getDescription());
        // task.setStarred(taskDetails.isStarred());
        // task.setCompleted(taskDetails.isCompleted());
        // return taskRepository.save(task);
        // OR
        return taskRepository.findById(id).map(task -> {
            task.setTitle(taskDetails.getTitle());
            task.setDescription(taskDetails.getDescription());
            task.setStarred(taskDetails.isStarred());
            task.setCompleted(taskDetails.isCompleted());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // DELETE delete task
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // DELETE delete all tasks
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    // PATCH toggle star
    public Task toggleStar(Long id) {
        // Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        // task.setStarred(!task.isStarred());
        // return taskRepository.save(task);
        // OR
        return taskRepository.findById(id).map(task -> {
            task.setStarred(!task.isStarred());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // PATCH toggle complete
    public Task toggleComplete(Long id) {
        // Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        // task.setCompleted(!task.isCompleted());
        // return taskRepository.save(task);
        // OR
        return taskRepository.findById(id).map(task -> {
            task.setCompleted(!task.isCompleted());
            return taskRepository.save(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
