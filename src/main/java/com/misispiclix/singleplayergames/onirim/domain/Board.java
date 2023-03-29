package com.misispiclix.singleplayergames.onirim.domain;

import com.misispiclix.singleplayergames.onirim.domain.card.Card;
import com.misispiclix.singleplayergames.onirim.domain.card.DoorCard;
import com.misispiclix.singleplayergames.onirim.domain.card.LabyrinthCard;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private List<Card> cardDeck;

    private List<Card> limboStack;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private List<LabyrinthCard> playedCards;

    private List<Card> playerHand;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private List<DoorCard> discoveredDoors;

    private List<Card> discardedCards;

    private List<Card> cardsToShow;

}
