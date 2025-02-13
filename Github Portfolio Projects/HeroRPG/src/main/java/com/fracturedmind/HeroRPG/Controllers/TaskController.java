package com.fracturedmind.HeroRPG.Controllers;

import com.fracturedmind.HeroRPG.DTO.TaskRequest;
import com.fracturedmind.HeroRPG.Models.Task;
import com.fracturedmind.HeroRPG.Repos.TaskRepository;
import com.fracturedmind.HeroRPG.Services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        Task task = taskService.createTask(request.getUserId(), request.getTitle(), request.getDescription(),
                request.getTaskType(), request.getXpReward());
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long taskId) {
        Task completedTask = taskService.completeTask(taskId);
        return ResponseEntity.ok(completedTask);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long userId) {
        return ResponseEntity.ok(taskRepository.findByUserId(userId));
    }
}
