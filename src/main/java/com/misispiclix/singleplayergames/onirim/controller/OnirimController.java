package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.dto.Game;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OnirimController {

    private IOnirimService onirimService;

    public OnirimController(IOnirimService onirimService) {
        this.onirimService = onirimService;
    }

    public Game createNewGame() {
        return onirimService.createNewGame();
    }

    public Game playCardFromHand(Game game, Integer playedCardIndex) {
        return onirimService.playCardFromHand(game, playedCardIndex);
    }

    public Game discardCardFromHand(Game game, Integer discardedCardIndex) {
        return onirimService.discardCardFromHand(game, discardedCardIndex);
    }

    public Game activateProphecy(Game game) {
        return onirimService.activateProphecy(game);
    }

    public Game confirmProphecy(Game game, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        return onirimService.confirmProphecy(game, discardedCardIndex, reorderedCardIndexes);
    }

    public Game drawCardFromDeck(Game game) {
        return onirimService.drawCardFromDeck(game);
    }

    public Game discardKeyCardFromHand(Game game, Integer discardedCardIndex) {
        return onirimService.discardKeyCardFromHand(game, discardedCardIndex);
    }

    public Game loseDoorCard(Game game, Integer doorCardIndex) {
        return onirimService.loseDoorCard(game, doorCardIndex);
    }

    public Game discardTopCardsFromDeck(Game game) {
        return onirimService.discardTopCardsFromDeck(game);
    }

    public Game discardPlayerHand(Game game) {
        return onirimService.discardPlayerHand(game);
    }

}
