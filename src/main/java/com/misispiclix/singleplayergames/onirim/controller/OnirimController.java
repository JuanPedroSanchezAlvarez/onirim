package com.misispiclix.singleplayergames.onirim.controller;

import com.misispiclix.singleplayergames.onirim.dto.Game;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Controller;

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

}
