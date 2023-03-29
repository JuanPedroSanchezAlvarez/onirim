package com.misispiclix.singleplayergames.onirim.domain.card;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
