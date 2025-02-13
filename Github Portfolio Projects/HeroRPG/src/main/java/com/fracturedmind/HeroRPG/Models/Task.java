package com.fracturedmind.HeroRPG.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fracturedmind.HeroRPG.Enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    private boolean completed = false;

    private int xpReward;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Task(String title, String description, TaskType taskType, User user, int xpReward) {
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.user = user;
        this.xpReward = xpReward;
    }
}
