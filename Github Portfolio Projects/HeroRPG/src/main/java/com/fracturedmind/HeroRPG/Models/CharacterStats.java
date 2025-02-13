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

    private int level;

    @Column(name = "current_xp")
    private int currentXP;

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
        this.level = 1;
        this.currentXP = 0;
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

    // method to gain experience points after a task is done
    public void gainXP(int xp)
    {
        this.currentXP += xp;
        while (this.currentXP >= xpToNextLevel()) {
            levelUp();
        }
    }

    // private method to level up when you have enough xp
    private void levelUp()
    {
        int xpNeeded = xpToNextLevel();

        this.level++;
        this.currentXP -= xpNeeded; // subtract after checking what is needed, keeping extra XP

        // increase all stats by one when leveling up
        this.strength++;
        this.intelligence++;
        this.dexterity++;
        this.charisma++;
        this.vitality++;
    }

    // private method to define and return xp needed to level up
    private int xpToNextLevel() {
        return level * 100; // just an example: this means xp needed to level up increases by 100 each level
    }
}
