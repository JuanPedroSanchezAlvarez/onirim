package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OnirimController {

    private IOnirimService onirimService;

    public OnirimController(IOnirimService onirimService) {
        this.onirimService = onirimService;
    }

    public GameDTO createNewGame() {
        return onirimService.createNewGame();
    }

    public GameDTO playCardFromHand(GameDTO gameDTO, Integer playedCardIndex) {
        return onirimService.playCardFromHand(gameDTO, playedCardIndex);
    }

    public GameDTO discardCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        return onirimService.discardCardFromHand(gameDTO, discardedCardIndex);
    }

    public GameDTO activateProphecy(GameDTO gameDTO) {
        return onirimService.activateProphecy(gameDTO);
    }

    public GameDTO confirmProphecy(GameDTO gameDTO, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        return onirimService.confirmProphecy(gameDTO, discardedCardIndex, reorderedCardIndexes);
    }

    public GameDTO drawCardFromDeck(GameDTO gameDTO) {
        return onirimService.drawCardFromDeck(gameDTO);
    }

    public GameDTO discardKeyCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        return onirimService.discardKeyCardFromHand(gameDTO, discardedCardIndex);
    }

    public GameDTO loseDoorCard(GameDTO gameDTO, Integer doorCardIndex) {
        return onirimService.loseDoorCard(gameDTO, doorCardIndex);
    }

    public GameDTO discardTopCardsFromDeck(GameDTO gameDTO) {
        return onirimService.discardTopCardsFromDeck(gameDTO);
    }

    public GameDTO discardPlayerHand(GameDTO gameDTO) {
        return onirimService.discardPlayerHand(gameDTO);
    }

}
