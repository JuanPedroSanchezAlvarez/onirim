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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "card_deck_id")
    private List<Card> cardDeck;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "limbo_stack_id")
    private List<Card> limboStack;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "played_cards_id")
    private List<LabyrinthCard> playedCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_hand_id")
    private List<Card> playerHand;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discovered_doors_id")
    private List<DoorCard> discoveredDoors;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discarded_cards_id")
    private List<Card> discardedCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cards_to_show_id")
    private List<Card> cardsToShow;

}
