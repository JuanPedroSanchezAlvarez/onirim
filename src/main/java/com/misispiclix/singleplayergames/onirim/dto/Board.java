package com.misispiclix.singleplayergames.onirim.dto;

import com.misispiclix.singleplayergames.onirim.dto.card.Card;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCard;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCard;
import com.misispiclix.singleplayergames.onirim.dto.card.NightmareCard;
import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Board {
    private List<Card> cardDeck;
    private List<Card> limboStack;
    private List<LabyrinthCard> playedCards;
    private List<Card> playerHand;
    public Board() {
        this.cardDeck = new ArrayList<>();
        this.limboStack = new ArrayList<>();
        this.playedCards = new ArrayList<>();
        this.playerHand = new ArrayList<>();
        this.initializeCardDeck();
    }
    private void initializeCardDeck() {
        for (int i = 0; i < 10; i++) { this.getCardDeck().add(new NightmareCard()); }
        for (int i = 0; i < 3; i++) { this.getCardDeck().add(new LabyrinthCard(Color.RED, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { this.getCardDeck().add(new LabyrinthCard(Color.RED, Symbol.MOON)); }
        for (int i = 0; i < 9; i++) { this.getCardDeck().add(new LabyrinthCard(Color.RED, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { this.getCardDeck().add(new LabyrinthCard(Color.GREEN, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { this.getCardDeck().add(new LabyrinthCard(Color.GREEN, Symbol.MOON)); }
        for (int i = 0; i < 7; i++) { this.getCardDeck().add(new LabyrinthCard(Color.GREEN, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { this.getCardDeck().add(new LabyrinthCard(Color.BLUE, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { this.getCardDeck().add(new LabyrinthCard(Color.BLUE, Symbol.MOON)); }
        for (int i = 0; i < 8; i++) { this.getCardDeck().add(new LabyrinthCard(Color.BLUE, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { this.getCardDeck().add(new LabyrinthCard(Color.YELLOW, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { this.getCardDeck().add(new LabyrinthCard(Color.YELLOW, Symbol.MOON)); }
        for (int i = 0; i < 6; i++) { this.getCardDeck().add(new LabyrinthCard(Color.YELLOW, Symbol.SUN)); }
        for (int i = 0; i < 2; i++) { this.getCardDeck().add(new DoorCard(Color.RED)); }
        for (int i = 0; i < 2; i++) { this.getCardDeck().add(new DoorCard(Color.GREEN)); }
        for (int i = 0; i < 2; i++) { this.getCardDeck().add(new DoorCard(Color.BLUE)); }
        for (int i = 0; i < 2; i++) { this.getCardDeck().add(new DoorCard(Color.YELLOW)); }
        this.shuffleCardDeck();
    }
    private void shuffleCardDeck() {
        this.getLimboStack().forEach(card -> { this.getCardDeck().add(card); });
        this.getLimboStack().clear();
        Collections.shuffle(this.getCardDeck());
    }
}
