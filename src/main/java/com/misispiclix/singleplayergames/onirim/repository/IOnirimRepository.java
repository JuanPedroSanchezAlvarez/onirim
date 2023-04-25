package com.misispiclix.singleplayergames.onirim.repository;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IOnirimRepository extends CrudRepository<Game, UUID> {
}
