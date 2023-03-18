package com.misispiclix.singleplayergames.onirim.service;

import com.misispiclix.singleplayergames.onirim.dto.Game;

import java.util.List;

public interface IOnirimService {
    Game createNewGame();
    Game playCardFromHand(Game game, Integer playedCardIndex);
    Game discardCardFromHand(Game game, Integer discardedCardIndex);
    Game activateProphecy(Game game);
    Game confirmProphecy(Game game, Integer discardedCardIndex, List<Integer> reorderedCardIndexes);
}
