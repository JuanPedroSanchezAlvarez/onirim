package com.misispiclix.singleplayergames.onirim.domain;

import com.misispiclix.singleplayergames.onirim.domain.card.Card;
import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.enums.GameStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Game extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board;

    @ElementCollection(targetClass = AllowedAction.class, fetch = FetchType.EAGER)
    @JoinTable(name = "allowed_actions", joinColumns = @JoinColumn(name = "game_id"))
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "allowed_actions")
    private List<AllowedAction> allowedActions;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "game_status")
    private GameStatus gameStatus;

    @Column(name = "message_to_display")
    private String messageToDisplay;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @Version
    private Integer version;

    @PrePersist
    @PreUpdate
    private void preSave() {
        // We must set the position of each card of the game on their respective deck before saving into the database.
        for (int i = 0; i < this.getBoard().getCardDeck().size(); i++) { this.getBoard().getCardDeck().get(i).setPositionInDeck(i); }
        for (int i = 0; i < this.getBoard().getLimboStack().size(); i++) { this.getBoard().getLimboStack().get(i).setPositionInDeck(i); }
        for (int i = 0; i < this.getBoard().getPlayedCards().size(); i++) { this.getBoard().getPlayedCards().get(i).setPositionInDeck(i); }
        for (int i = 0; i < this.getBoard().getPlayerHand().size(); i++) { this.getBoard().getPlayerHand().get(i).setPositionInDeck(i); }
        for (int i = 0; i < this.getBoard().getDiscoveredDoors().size(); i++) { this.getBoard().getDiscoveredDoors().get(i).setPositionInDeck(i); }
        for (int i = 0; i < this.getBoard().getDiscardedCards().size(); i++) { this.getBoard().getDiscardedCards().get(i).setPositionInDeck(i); }
        for (int i = 0; i < this.getBoard().getCardsToShow().size(); i++) { this.getBoard().getCardsToShow().get(i).setPositionInDeck(i); }
    }

    @PostLoad
    private void postLoad() {
        // We must sort all the cards of the game based on their position attribute.
        this.getBoard().getCardDeck().sort(Comparator.comparing(Card::getPositionInDeck));
        this.getBoard().getLimboStack().sort(Comparator.comparing(Card::getPositionInDeck));
        this.getBoard().getPlayedCards().sort(Comparator.comparing(Card::getPositionInDeck));
        this.getBoard().getPlayerHand().sort(Comparator.comparing(Card::getPositionInDeck));
        this.getBoard().getDiscoveredDoors().sort(Comparator.comparing(Card::getPositionInDeck));
        this.getBoard().getDiscardedCards().sort(Comparator.comparing(Card::getPositionInDeck));
        this.getBoard().getCardsToShow().sort(Comparator.comparing(Card::getPositionInDeck));
    }

}
