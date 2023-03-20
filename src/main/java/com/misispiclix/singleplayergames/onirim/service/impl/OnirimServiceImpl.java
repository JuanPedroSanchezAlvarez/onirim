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

import java.util.ArrayList;
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
            // We look for a door card of that same color and play it.
            discoverDoor(game);
        }
        // We must draw a card.
        game.getAllowedActions().clear();
        game.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        return game;
    }

    @Override
    public Game discardCardFromHand(Game game, Integer discardedCardIndex) {
        // We check that the action is allowed.
        if (!validateAllowedAction(game, AllowedAction.DISCARD_CARD_FROM_HAND)) { return game; }
        // We check that the chosen card exists in the hand.
        if (!validatePlayedCardIndex(game, discardedCardIndex)) { return game; }
        // We discard the chosen card.
        discardCard(game, discardedCardIndex);
        // We check that the discarded card has the key symbol.
        game.getAllowedActions().clear();
        if (validateDiscardedCardHasKeySymbol(game.getBoard().getDiscardedCards()) && !game.getBoard().getCardDeck().isEmpty()) {
            // We must activate a prophecy.
            game.getAllowedActions().add(AllowedAction.ACTIVATE_PROPHECY);
        } else {
            // We must draw a card.
            game.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        }
        return game;
    }

    @Override
    public Game activateProphecy(Game game) {
        // We check that the action is allowed.
        if (!validateAllowedAction(game, AllowedAction.ACTIVATE_PROPHECY)) { return game; }
        // We show the prophecy cards.
        showProphecyCards(game);
        // We must confirm the prophecy.
        game.getAllowedActions().clear();
        game.getAllowedActions().add(AllowedAction.CONFIRM_PROPHECY);
        return game;
    }

    @Override
    public Game confirmProphecy(Game game, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        // We check that the action is allowed.
        if (!validateAllowedAction(game, AllowedAction.CONFIRM_PROPHECY)) { return game; }
        // We discard the chosen card.
        game.getBoard().getDiscardedCards().add(game.getBoard().getCardsToShow().get(discardedCardIndex));
        // We rearrange the top cards of the main deck in the chosen order.
        rearrangeTopCardsOfTheCardDeck(game, reorderedCardIndexes);
        // We must draw a card.
        game.getAllowedActions().clear();
        game.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        return game;
    }

    @Override
    public Game drawCardFromDeck(Game game) {
        // We check that the action is allowed.
        if (!validateAllowedAction(game, AllowedAction.DRAW_CARD_FROM_DECK)) { return game; }
        // We check that the main deck is not empty.
        if (!validateCardDeckNotEmpty(game)) { return game; }
        // We draw a card from the main deck.
        drawCard(game);
        // We check the type of card that has been drawn and act accordingly.
        checkTypeOfCardDrawn(game);
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

    private void discardCard(Game game, Integer discardedCardIndex) {
        game.getBoard().getDiscardedCards().add(game.getBoard().getPlayerHand().get(discardedCardIndex));
        game.getBoard().getPlayerHand().remove(discardedCardIndex.intValue());
    }

    private void drawCard(Game game) {
        game.getBoard().getPlayerHand().add(game.getBoard().getCardDeck().remove(game.getBoard().getCardDeck().size() - 1));
    }

    private void discoverDoor(Game game) {
        Card doorCardFound = null;
        for (Card card : game.getBoard().getCardDeck()) {
            if (card instanceof DoorCard) {
                DoorCard doorCard = (DoorCard) card;
                if (doorCard.getColor().equals(game.getBoard().getPlayedCards().get(game.getBoard().getCardDeck().size() - 1).getColor())) {
                    game.getBoard().getDiscoveredDoors().add(doorCard);
                    doorCardFound = card;
                    break;
                }
            }
        }
        if (null != doorCardFound) {
            game.getBoard().getCardDeck().remove(doorCardFound);
        }
        shuffleCardDeck(game);
    }

    private void showProphecyCards(Game game) {
        int numberOfCardsToShow = Math.min(game.getBoard().getCardDeck().size(), 5);
        for (int i = 0; i < numberOfCardsToShow; i++) {
            game.getBoard().getCardsToShow().add(game.getBoard().getCardDeck().get(game.getBoard().getCardDeck().size() - 1));
            game.getBoard().getCardDeck().remove(game.getBoard().getCardDeck().size() - 1);
        }
    }

    private void rearrangeTopCardsOfTheCardDeck(Game game, List<Integer> reorderedCardIndexes) {
        List<Card> reorderedCardList = new ArrayList<>();
        for (int i = 0; i < reorderedCardIndexes.size(); i++) {
            int j = 0;
            for (Integer index : reorderedCardIndexes) {
                if (i == index) {
                    reorderedCardList.add(game.getBoard().getCardsToShow().get(j));
                }
                j++;
            }
        }
        while (!reorderedCardList.isEmpty()) {
            game.getBoard().getCardDeck().add(reorderedCardList.get(reorderedCardList.size() - 1));
            reorderedCardList.remove(reorderedCardList.size() - 1);
        }
        game.getBoard().getCardsToShow().clear();
    }

    private void checkTypeOfCardDrawn(Game game) {
        switch (game.getBoard().getPlayerHand().get(game.getBoard().getPlayerHand().size() - 1)) {
            case LabyrinthCard labyrinthCard -> { labyrinthCardDrawnAction(game); }
            case DoorCard doorCard -> { doorCardDrawnAction(game, doorCard); }
            case NightmareCard nightmareCard -> { nightmareCardDrawnAction(game); }
            default -> { game.setMessageToDisplay("ERROR: Card type not found."); }
        };
    }

    private void labyrinthCardDrawnAction(Game game) {
        game.getAllowedActions().clear();
        if (game.getBoard().getPlayerHand().size() >= 5) {
            game.getAllowedActions().add(AllowedAction.PLAY_CARD_FROM_HAND);
            game.getAllowedActions().add(AllowedAction.DISCARD_CARD_FROM_HAND);
        } else {
            game.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        }
    }

    private void doorCardDrawnAction(Game game, DoorCard doorCard) {
        // We check if we have a labyrinth card in hand with the key symbol and the same color as the drawn door card.
        boolean cardWithKeySymbolAndSameColorFound = false;
        for (int i = 0; i < game.getBoard().getPlayerHand().size(); i++) {
            if (game.getBoard().getPlayerHand().get(i) instanceof LabyrinthCard labyrinthCard) {
                // If we have it, we discover the door and discard the key card.
                if (labyrinthCard.getSymbol().equals(Symbol.KEY) && labyrinthCard.getColor().equals(doorCard.getColor())) {
                    cardWithKeySymbolAndSameColorFound = true;
                    game.getBoard().getDiscoveredDoors().add((DoorCard) game.getBoard().getPlayerHand().remove(game.getBoard().getPlayerHand().size() - 1));
                    game.getBoard().getDiscardedCards().add(game.getBoard().getPlayerHand().remove(i));
                    break;
                }
            }
        }
        // If we don't have it, we put the door card in the limbo stack.
        if (!cardWithKeySymbolAndSameColorFound) {
            game.getBoard().getLimboStack().add(game.getBoard().getPlayerHand().remove(game.getBoard().getPlayerHand().size() - 1));
        }
        labyrinthCardDrawnAction(game);
    }

    private void nightmareCardDrawnAction(Game game) {
        // TODO
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

    private boolean validateDiscardedCardHasKeySymbol(List<Card> discardedCards) {
        Card discardedCard = discardedCards.get(discardedCards.size() -1);
        if (discardedCard instanceof LabyrinthCard discardedLabyrinthCard) {
            return discardedLabyrinthCard.getSymbol().equals(Symbol.KEY);
        }
        return false;
    }

    private boolean validateCardDeckNotEmpty(Game game) {
        game.setMessageToDisplay(game.getBoard().getCardDeck().isEmpty() ? "Game Over." : "");
        return game.getMessageToDisplay().isEmpty();
    }

}
