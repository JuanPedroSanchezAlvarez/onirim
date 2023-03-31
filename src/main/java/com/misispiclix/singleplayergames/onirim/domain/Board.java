package com.misispiclix.singleplayergames.onirim.domain;

import com.misispiclix.singleplayergames.onirim.domain.card.Card;
import com.misispiclix.singleplayergames.onirim.domain.card.DoorCard;
import com.misispiclix.singleplayergames.onirim.domain.card.LabyrinthCard;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Board extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "card_deck_board_id")
    private List<Card> cardDeck;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "limbo_stack_board_id")
    private List<Card> limboStack;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "played_cards_board_id")
    private List<LabyrinthCard> playedCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_hand_board_id")
    private List<Card> playerHand;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discovered_doors_board_id")
    private List<DoorCard> discoveredDoors;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discarded_cards_board_id")
    private List<Card> discardedCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cards_to_show_board_id")
    private List<Card> cardsToShow;

}
