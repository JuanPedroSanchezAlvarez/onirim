package com.misispiclix.singleplayergames.onirim.service.impl;

import com.misispiclix.singleplayergames.onirim.dto.Game;
import com.misispiclix.singleplayergames.onirim.dto.card.Card;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCard;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCard;
import com.misispiclix.singleplayergames.onirim.dto.card.NightmareCard;
import com.misispiclix.singleplayergames.onirim.enums.ActionAllowed;
import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class OnirimServiceImpl implements IOnirimService {

    @Override
    public Game createNewGame() {
        Game game = new Game();
        initializeCardDeck(game);
        initializePlayerHand(game);
        game.getActionsAllowed().add(ActionAllowed.PLAY_CARD_FROM_HAND);
        game.getActionsAllowed().add(ActionAllowed.DISCARD_CARD_FROM_HAND);
        return game;
    }

    @Override
    public Game playCardFromHand(Game game, Integer playedCardIndex) {
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

}
