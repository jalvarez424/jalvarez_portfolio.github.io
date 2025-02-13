package com.fracturedmind.HeroRPG.Repos;

import com.fracturedmind.HeroRPG.Models.CharacterStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterStatsRepository extends JpaRepository<CharacterStats, Long> {
}
