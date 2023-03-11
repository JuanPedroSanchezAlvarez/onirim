package com.misispiclix.singleplayergames.onirim.service.impl;

import com.misispiclix.singleplayergames.onirim.dto.Game;
import com.misispiclix.singleplayergames.onirim.dto.card.Card;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCard;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCard;
import com.misispiclix.singleplayergames.onirim.dto.card.NightmareCard;
import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class OnirimServiceImpl implements IOnirimService {

    @Override
    public Game createNewGame() {
        Game game = new Game();
        initializeCardDeck(game);
        initializePlayerHand(game);
        game.getAllowedActions().add(AllowedAction.PLAY_CARD_FROM_HAND);
        game.getAllowedActions().add(AllowedAction.DISCARD_CARD_FROM_HAND);
        return game;
    }

    @Override
    public Game playCardFromHand(Game game, Integer playedCardIndex) {
        // We check that the action is allowed.
        if (!validateAllowedAction(game, AllowedAction.PLAY_CARD_FROM_HAND)) { return game; }
        // We check that the chosen card exists in the hand.
        if (!validatePlayedCardIndex(game, playedCardIndex)) { return game; }
        // We check that the chosen card has a different symbol than the last played.
        if (!validateDifferentSymbol(game, playedCardIndex)) { return game; }
        // We play the chosen card.
        playCard(game, playedCardIndex);
        // We check that the card just played is the third consecutive card of the same color.
        if (validateThirdConsecutiveCardOfTheSameColor(game.getBoard().getPlayedCards())) {
            // TODO Si la carta es la tercera consecutiva del mismo color, busca una carta de puerta de ese mismo color y se coloca delante.
        }
        // TODO Se baraja el mazo.
        // TODO Robar.
        // TODO Barajar.
        return game;
    }

    @Override
    public Game discardCardFromHand(Game game, Integer discardedCardIndex) {
        return game;
    }

    private void initializeCardDeck(Game game) {
        for (int i = 0; i < 10; i++) { game.getBoard().getCardDeck().add(new NightmareCard()); }
        for (int i = 0; i < 3; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.RED, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.RED, Symbol.MOON)); }
        for (int i = 0; i < 9; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.RED, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.GREEN, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.GREEN, Symbol.MOON)); }
        for (int i = 0; i < 7; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.GREEN, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.BLUE, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.BLUE, Symbol.MOON)); }
        for (int i = 0; i < 8; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.BLUE, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.YELLOW, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.YELLOW, Symbol.MOON)); }
        for (int i = 0; i < 6; i++) { game.getBoard().getCardDeck().add(new LabyrinthCard(Color.YELLOW, Symbol.SUN)); }
        for (int i = 0; i < 2; i++) { game.getBoard().getCardDeck().add(new DoorCard(Color.RED)); }
        for (int i = 0; i < 2; i++) { game.getBoard().getCardDeck().add(new DoorCard(Color.GREEN)); }
        for (int i = 0; i < 2; i++) { game.getBoard().getCardDeck().add(new DoorCard(Color.BLUE)); }
        for (int i = 0; i < 2; i++) { game.getBoard().getCardDeck().add(new DoorCard(Color.YELLOW)); }
        shuffleCardDeck(game);
    }

    private void shuffleCardDeck(Game game) {
        game.getBoard().getLimboStack().forEach(card -> { game.getBoard().getCardDeck().add(card); });
        game.getBoard().getLimboStack().clear();
        Collections.shuffle(game.getBoard().getCardDeck());
    }

    private void initializePlayerHand(Game game) {
        while (game.getBoard().getPlayerHand().size() < 5) {
            Card card = game.getBoard().getCardDeck().get(game.getBoard().getCardDeck().size() - 1);
            game.getBoard().getCardDeck().remove(game.getBoard().getCardDeck().size() - 1);
            if (card instanceof LabyrinthCard) {
                game.getBoard().getPlayerHand().add(card);
            } else {
                game.getBoard().getLimboStack().add(card);
            }
        }
        shuffleCardDeck(game);
    }

    private void playCard(Game game, Integer playedCardIndex) {
        game.getBoard().getPlayedCards().add((LabyrinthCard) game.getBoard().getPlayerHand().get(playedCardIndex));
        game.getBoard().getPlayerHand().remove(playedCardIndex.intValue());
    }

    private boolean validateAllowedAction(Game game, AllowedAction allowedAction) {
        game.setMessageToDisplay(game.getAllowedActions().contains(allowedAction) ? "" : "Action not allowed.");
        return game.getMessageToDisplay().isEmpty();
    }

    private boolean validatePlayedCardIndex(Game game, Integer playedCardIndex) {
        game.setMessageToDisplay(playedCardIndex > -1 && playedCardIndex < 5 ? "" : "Selected card is not in hand.");
        return game.getMessageToDisplay().isEmpty();
    }

    private boolean validateDifferentSymbol(Game game, Integer playedCardIndex) {
        if (game.getBoard().getPlayedCards().isEmpty()) { return true; }
        if (game.getBoard().getPlayerHand().get(playedCardIndex) instanceof LabyrinthCard) {
            LabyrinthCard selectedCard = (LabyrinthCard) game.getBoard().getPlayerHand().get(playedCardIndex);
            LabyrinthCard lastCardPlayed = game.getBoard().getPlayedCards().get(game.getBoard().getPlayedCards().size() -1);
            game.setMessageToDisplay(selectedCard.getSymbol().equals(lastCardPlayed.getSymbol()) ? "The chosen card must have a different symbol than the last card played." : "");
        } else {
            game.setMessageToDisplay("Selected card is not a Labyrinth Card.");
        }
        return game.getMessageToDisplay().isEmpty();
    }

    private boolean validateThirdConsecutiveCardOfTheSameColor(List<LabyrinthCard> playedCards) {
        if (playedCards.size() < 3) { return false; }
        LabyrinthCard lastCard = playedCards.get(playedCards.size() -1);
        LabyrinthCard penultimateCard = playedCards.get(playedCards.size() -2);
        LabyrinthCard beforePenultimateCard = playedCards.get(playedCards.size() -3);
        boolean lastThreeCardsOfTheSameColor = lastCard.getColor().equals(penultimateCard.getColor()) && lastCard.getColor().equals(beforePenultimateCard.getColor());
        if (playedCards.size() == 3) { return lastThreeCardsOfTheSameColor; }
        LabyrinthCard fourthCard = playedCards.get(playedCards.size() -4);
        boolean fourthCardOfTheSameColor = lastCard.getColor().equals(fourthCard.getColor());
        return lastThreeCardsOfTheSameColor && !fourthCardOfTheSameColor;
    }

}
