package com.misispiclix.singleplayergames.onirim.repository;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOnirimRepository extends JpaRepository<Game, UUID> {
}
