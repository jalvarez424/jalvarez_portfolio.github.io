package com.fracturedmind.HeroRPG.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fracturedmind.HeroRPG.Enums.TaskType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "character_stats")
public class CharacterStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int strength;
    private int intelligence;
    private int dexterity;
    private int charisma;
    private int vitality;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore // prevent infinite recursion when making requests
    // This prevents user from being serialized inside CharacterStats, breaking the infinite loop.
    // The User entity will still contain CharacterStats, but CharacterStats will no longer contain User in JSON responses.
    private User user;

    public CharacterStats(User user) {
        this.user = user;
        this.strength = 1;
        this.intelligence = 1;
        this.dexterity = 1;
        this.charisma = 1;
        this.vitality = 1;
    }

    // method to increase stats dynamically
    public void increaseStat(TaskType taskType, int xpReward) {
        switch (taskType) {
            case STRENGTH -> this.strength += xpReward;
            case INTELLIGENCE -> this.intelligence += xpReward;
            case DEXTERITY -> this.dexterity += xpReward;
            case CHARISMA -> this.charisma += xpReward;
            case VITALITY -> this.vitality += xpReward;
        }
    }
}
