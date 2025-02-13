package com.fracturedmind.HeroRPG.Services;

import com.fracturedmind.HeroRPG.Enums.TaskType;
import com.fracturedmind.HeroRPG.Models.CharacterStats;
import com.fracturedmind.HeroRPG.Models.Task;
import com.fracturedmind.HeroRPG.Models.User;
import com.fracturedmind.HeroRPG.Repos.CharacterStatsRepository;
import com.fracturedmind.HeroRPG.Repos.TaskRepository;
import com.fracturedmind.HeroRPG.Repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CharacterStatsRepository characterStatsRepository;

    @Transactional
    public Task createTask(Long userId, String title, String description, TaskType taskType, int xpReward) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
        Task task = new Task(title, description, taskType, user, xpReward);
        return taskRepository.save(task);
    }

    @Transactional
    public Task completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found!"));

        if (task.isCompleted()) {
            throw new RuntimeException("Task is already completed!");
        }

        task.setCompleted(true);
        taskRepository.save(task);

        // Increase user's character stats
        CharacterStats stats = task.getUser().getCharacterStats();
        stats.increaseStat(task.getTaskType(), task.getXpReward());
        characterStatsRepository.save(stats);

        return task;
    }
}
