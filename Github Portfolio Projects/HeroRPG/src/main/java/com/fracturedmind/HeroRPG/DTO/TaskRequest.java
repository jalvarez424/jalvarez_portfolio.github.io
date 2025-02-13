package com.fracturedmind.HeroRPG.DTO;

import com.fracturedmind.HeroRPG.Enums.TaskType;
import lombok.Getter;
import lombok.Setter;

//This ensures clean request handling instead of exposing entity objects directly.
@Getter
@Setter
public class TaskRequest {
    private Long userId;
    private String title;
    private String description;
    private TaskType taskType;
    private int xpReward;
}
