package com.misispiclix.onirim.repository;

import com.misispiclix.onirim.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOnirimRepository extends JpaRepository<Game, UUID> {
}
