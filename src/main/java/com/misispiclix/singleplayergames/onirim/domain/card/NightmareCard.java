package com.misispiclix.singleplayergames.onirim.domain.card;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("3")
public class NightmareCard extends Card {
}
