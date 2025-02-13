package com.fracturedmind.HeroRPG.Repos;

import com.fracturedmind.HeroRPG.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);  // allows for fetching all tasks of a given user
}
