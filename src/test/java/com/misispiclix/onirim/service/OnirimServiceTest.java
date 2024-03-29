package com.misispiclix.onirim.service;

import com.misispiclix.onirim.dto.GameDTO;
import com.misispiclix.onirim.dto.card.DoorCardDTO;
import com.misispiclix.onirim.dto.card.LabyrinthCardDTO;
import com.misispiclix.onirim.dto.card.NightmareCardDTO;
import com.misispiclix.onirim.enums.AllowedAction;
import com.misispiclix.onirim.enums.Color;
import com.misispiclix.onirim.enums.GameStatus;
import com.misispiclix.onirim.enums.Symbol;
import com.misispiclix.onirim.exception.EqualCardSymbolException;
import com.misispiclix.onirim.exception.InvalidCardIndexException;
import com.misispiclix.onirim.exception.NotAKeyCardException;
import com.misispiclix.onirim.exception.NotALabyrinthCardException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OnirimServiceTest {

    @Autowired
    IOnirimService onirimService;

    private GameDTO getGameForTest(UUID id) {
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        return optionalOfGameDto.get();
    }

    @Test
    void getGames() {
        // PREPARATION
        onirimService.createNewGame();
        onirimService.createNewGame();
        onirimService.createNewGame();
        // EXECUTION
        Page<GameDTO> pageOfGameDto = onirimService.getGames(0, 10);
        // VERIFICATION
        assertThat(pageOfGameDto.getNumberOfElements()).isEqualTo(3);
    }

    @Test
    void getGameById() {
        // PREPARATION
        UUID id = onirimService.createNewGame();
        // EXECUTION
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        // VERIFICATION
        assertThat(optionalOfGameDto).isNotEmpty();
    }

    @Test
    void createNewGame() {
        // PREPARATION
        // EXECUTION
        UUID id = onirimService.createNewGame();
        GameDTO gameDTO = getGameForTest(id);
        // VERIFICATION
        // The card deck must have 71 cards.
        assertThat(gameDTO.getBoard().getCardDeck().size()).isEqualTo(71);
        // The player hand must have 5 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(5);
        // The 5 cards of the player hand must be Labyrinth cards.
        gameDTO.getBoard().getPlayerHand().forEach(card -> {
            assertEquals(card.getClass(), LabyrinthCardDTO.class);
        });
        // The other zones must be empty.
        assertThat(gameDTO.getBoard().getCardsToShow()).isEmpty();
        assertThat(gameDTO.getBoard().getDiscardedCards()).isEmpty();
        assertThat(gameDTO.getBoard().getDiscoveredDoors()).isEmpty();
        assertThat(gameDTO.getBoard().getLimboStack()).isEmpty();
        assertThat(gameDTO.getBoard().getPlayedCards()).isEmpty();
        // The game must be in play.
        assertThat(gameDTO.getGameStatus()).isEqualTo(GameStatus.PLAYING);
    }

    private UUID playCardFromHand_Preparation(boolean thirdConsecutiveCardOfTheSameColor, boolean allDoorsDiscovered) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.KEY));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        if (thirdConsecutiveCardOfTheSameColor) {
            gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.SUN));
            gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.YELLOW));
        } else {
            gameDTO.getBoard().getPlayerHand().add(new DoorCardDTO(Color.GREEN));
        }
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.KEY));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.MOON));
        gameDTO.getAllowedActions().add(AllowedAction.PLAY_CARD_FROM_HAND);
        if (allDoorsDiscovered) {
            gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.YELLOW));
            gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.GREEN));
            gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.GREEN));
            gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.BLUE));
            gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.BLUE));
            gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.RED));
            gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.RED));
        }
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void playCardFromHand_InvalidCardIndexException() {
        // PREPARATION
        UUID id = playCardFromHand_Preparation(false, false);
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.playCardFromHand(id, 5); } );
    }

    @Test
    void playCardFromHand_NotALabyrinthCardException() {
        // PREPARATION
        UUID id = playCardFromHand_Preparation(false, false);
        // EXECUTION
        // VERIFICATION
        assertThrows(NotALabyrinthCardException.class, () -> { onirimService.playCardFromHand(id, 4); } );
    }

    @Test
    void playCardFromHand_EqualCardSymbolException() {
        // PREPARATION
        UUID id = playCardFromHand_Preparation(false, false);
        // EXECUTION
        // VERIFICATION
        assertThrows(EqualCardSymbolException.class, () -> { onirimService.playCardFromHand(id, 1); } );
    }

    @Test
    void playCardFromHand_NotThirdConsecutiveCardOfTheSameColor_NotAllDoorsDiscovered() {
        // PREPARATION
        UUID id = playCardFromHand_Preparation(false, false);
        // EXECUTION
        onirimService.playCardFromHand(id, 2);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The player hand must have 4 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(4);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    @Test
    void playCardFromHand_ThirdConsecutiveCardOfTheSameColor_NotAllDoorsDiscovered() {
        // PREPARATION
        UUID id = playCardFromHand_Preparation(true, false);
        // EXECUTION
        onirimService.playCardFromHand(id, 4);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The player hand must have 4 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(4);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
        // We must have one discovered door.
        assertThat(gameDTO.getBoard().getDiscoveredDoors().size()).isEqualTo(1);
    }

    @Test
    void playCardFromHand_ThirdConsecutiveCardOfTheSameColor_AllDoorsDiscovered() {
        // PREPARATION
        UUID id = playCardFromHand_Preparation(true, true);
        // EXECUTION
        onirimService.playCardFromHand(id, 4);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The player hand must have 4 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(4);
        // There must be no actions allowed.
        assertThat(gameDTO.getAllowedActions()).isEmpty();
        // We must have eight discovered doors.
        assertThat(gameDTO.getBoard().getDiscoveredDoors().size()).isEqualTo(8);
        // The game must be finished.
        assertThat(gameDTO.getGameStatus()).isEqualTo(GameStatus.FINISHED);
    }

    private UUID discardCardFromHand_Preparation() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.KEY));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new DoorCardDTO(Color.GREEN));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.KEY));
        gameDTO.getBoard().getPlayedCards().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.MOON));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.RED));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN));
        gameDTO.getAllowedActions().add(AllowedAction.DISCARD_CARD_FROM_HAND);
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void discardCardFromHand_InvalidCardIndexException() {
        // PREPARATION
        UUID id = discardCardFromHand_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.discardCardFromHand(id, 5); } );
    }

    @Test
    void discardCardFromHand_DiscardedCardHasKeySymbol() {
        // PREPARATION
        UUID id = discardCardFromHand_Preparation();
        // EXECUTION
        onirimService.discardCardFromHand(id, 0);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The player hand must have 4 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(4);
        // The next allowed action must be to activate a prophecy.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.ACTIVATE_PROPHECY);
        // We must have one discarded card.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(1);
    }

    @Test
    void discardCardFromHand_DiscardedCardHasNotKeySymbol() {
        // PREPARATION
        UUID id = discardCardFromHand_Preparation();
        // EXECUTION
        onirimService.discardCardFromHand(id, 1);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The player hand must have 4 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(4);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
        // We must have one discarded card.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(1);
    }

    private UUID activateProphecy_Preparation() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.SUN));
        gameDTO.getAllowedActions().add(AllowedAction.ACTIVATE_PROPHECY);
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void activateProphecy() {
        // PREPARATION
        UUID id = activateProphecy_Preparation();
        // EXECUTION
        onirimService.activateProphecy(id);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The cards to show zone must have 5 cards.
        assertThat(gameDTO.getBoard().getCardsToShow().size()).isEqualTo(5);
        // The next allowed action must be to confirm the prophecy.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.CONFIRM_PROPHECY);
    }

    private UUID confirmProphecy_Preparation() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getCardsToShow().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getCardsToShow().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getCardsToShow().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardsToShow().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.SUN));
        gameDTO.getBoard().getCardsToShow().add(new LabyrinthCardDTO(Color.RED, Symbol.MOON));
        gameDTO.getAllowedActions().add(AllowedAction.CONFIRM_PROPHECY);
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void confirmProphecy_InvalidCardIndexException_1() {
        // PREPARATION
        UUID id = confirmProphecy_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.confirmProphecy(id, 5, List.of(1, 0, 3, 2)); } );
    }

    @Test
    void confirmProphecy_InvalidCardIndexException_2() {
        // PREPARATION
        UUID id = confirmProphecy_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.confirmProphecy(id, 2, List.of(1, 0, 3, 2, 4)); } );
    }

    @Test
    void confirmProphecy_InvalidCardIndexException_3() {
        // PREPARATION
        UUID id = confirmProphecy_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.confirmProphecy(id, 2, List.of(1, 0, 4, 2)); } );
    }

    @Test
    void confirmProphecy_InvalidCardIndexException_4() {
        // PREPARATION
        UUID id = confirmProphecy_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.confirmProphecy(id, 2, List.of(1, 0, 3, 0)); } );
    }

    @Test
    void confirmProphecy() {
        // PREPARATION
        UUID id = confirmProphecy_Preparation();
        // EXECUTION
        onirimService.confirmProphecy(id, 2, List.of(1, 0, 3, 2));
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The card deck must have 4 cards.
        assertThat(gameDTO.getBoard().getCardDeck().size()).isEqualTo(4);
        // The discarded cards zone must have 1 card.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(1);
        // The cards to show zone must be empty.
        assertThat(gameDTO.getBoard().getCardsToShow()).isEmpty();
        // The reordered cards must be in correct order.
        assertThat(gameDTO.getBoard().getCardDeck().get(0)).isEqualTo(new LabyrinthCardDTO(Color.YELLOW, Symbol.SUN));
        assertThat(gameDTO.getBoard().getCardDeck().get(1)).isEqualTo(new LabyrinthCardDTO(Color.RED, Symbol.MOON));
        assertThat(gameDTO.getBoard().getCardDeck().get(2)).isEqualTo(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        assertThat(gameDTO.getBoard().getCardDeck().get(3)).isEqualTo(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        // The discarded card must be correct.
        assertThat(gameDTO.getBoard().getDiscardedCards().get(0)).isEqualTo(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    @Test
    void drawCardFromDeck_StartWithCardDeckEmpty() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The game must be finished.
        assertThat(gameDTO.getGameStatus()).isEqualTo(GameStatus.FINISHED);
    }

    @Test
    void drawCardFromDeck_DrawLabyrinthCard_HandSizeLowerThanFive() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN));
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    @Test
    void drawCardFromDeck_DrawLabyrinthCard_HandSizeOfFive() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN));
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The next allowed actions must be to play or discard a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(2);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.PLAY_CARD_FROM_HAND);
        assertThat(gameDTO.getAllowedActions().get(1)).isEqualTo(AllowedAction.DISCARD_CARD_FROM_HAND);
    }

    @Test
    void drawCardFromDeck_DrawDoorCard_DiscoverDoor() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON));
        gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.BLUE));
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // We must have one discovered door.
        assertThat(gameDTO.getBoard().getDiscoveredDoors().size()).isEqualTo(1);
        // We must have one discarded card.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(1);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    @Test
    void drawCardFromDeck_DrawDoorCard_NoDiscoverDoor() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON));
        gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.GREEN));
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // We must have one card in the limbo stack.
        assertThat(gameDTO.getBoard().getLimboStack().size()).isEqualTo(1);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    @Test
    void drawCardFromDeck_DrawNightmareCard_DiscardKeyCardFromHand() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getCardDeck().add(new NightmareCardDTO());
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The next allowed action must be to discard a key card from hand.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DISCARD_KEY_CARD_FROM_HAND);
    }

    @Test
    void drawCardFromDeck_DrawNightmareCard_LoseDoorCard() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getCardDeck().add(new NightmareCardDTO());
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.BLUE));
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The next allowed action must be to lose a discovered door card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.LOSE_DOOR_CARD);
    }

    @Test
    void drawCardFromDeck_DrawNightmareCard_DiscardTopCardsFromDeck() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new NightmareCardDTO());
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The next allowed action must be to discard the top cards from the deck.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DISCARD_TOP_CARDS_FROM_DECK);
    }

    @Test
    void drawCardFromDeck_DrawNightmareCard_DiscardPlayerHand() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getCardDeck().add(new NightmareCardDTO());
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The next allowed action must be to discard the top cards from the deck or to discard the entire player hand.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(2);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DISCARD_TOP_CARDS_FROM_DECK);
        assertThat(gameDTO.getAllowedActions().get(1)).isEqualTo(AllowedAction.DISCARD_PLAYER_HAND);
    }

    @Test
    void drawCardFromDeck_AllDoorsDiscovered() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON));
        gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.BLUE));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.BLUE));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.RED));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.RED));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.GREEN));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.GREEN));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.YELLOW));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.YELLOW));
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // The game must be finished.
        assertThat(gameDTO.getGameStatus()).isEqualTo(GameStatus.FINISHED);
    }

    @Test
    void drawCardFromDeck_NoNextAllowedActions() {
        // PREPARATION
        GameDTO gameDTO = new GameDTO();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        gameDTO.getBoard().getCardDeck().add(new NightmareCardDTO());
        UUID id = onirimService.saveGame(gameDTO).getId();
        // EXECUTION
        onirimService.drawCardFromDeck(id);
        // VERIFICATION
        gameDTO = getGameForTest(id);
        // There must be no actions allowed.
        assertThat(gameDTO.getAllowedActions()).isEmpty();
        // The game must be finished.
        assertThat(gameDTO.getGameStatus()).isEqualTo(GameStatus.FINISHED);
    }

    private UUID discardKeyCardFromHand_Preparation() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getBoard().getPlayerHand().add(new DoorCardDTO(Color.YELLOW));
        gameDTO.getAllowedActions().add(AllowedAction.DISCARD_KEY_CARD_FROM_HAND);
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void discardKeyCardFromHand_InvalidCardIndexException() {
        // PREPARATION
        UUID id = discardKeyCardFromHand_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.discardKeyCardFromHand(id, 5); } );
    }

    @Test
    void discardKeyCardFromHand_NotAKeyCardException() {
        // PREPARATION
        UUID id = discardKeyCardFromHand_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(NotAKeyCardException.class, () -> { onirimService.discardKeyCardFromHand(id, 0); } );
    }

    @Test
    void discardKeyCardFromHand_NotALabyrinthCardException() {
        // PREPARATION
        UUID id = discardKeyCardFromHand_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(NotALabyrinthCardException.class, () -> { onirimService.discardKeyCardFromHand(id, 3); } );
    }

    @Test
    void discardKeyCardFromHand() {
        // PREPARATION
        UUID id = discardKeyCardFromHand_Preparation();
        // EXECUTION
        onirimService.discardKeyCardFromHand(id, 2);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // The player hand must have 3 cards.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(3);
        // We must have 1 discarded card.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(1);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    private UUID loseDoorCard_Preparation() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.BLUE));
        gameDTO.getBoard().getDiscoveredDoors().add(new DoorCardDTO(Color.YELLOW));
        gameDTO.getAllowedActions().add(AllowedAction.LOSE_DOOR_CARD);
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void loseDoorCard_InvalidCardIndexException() {
        // PREPARATION
        UUID id = loseDoorCard_Preparation();
        // EXECUTION
        // VERIFICATION
        assertThrows(InvalidCardIndexException.class, () -> { onirimService.loseDoorCard(id, 2); } );
    }

    @Test
    void loseDoorCard() {
        // PREPARATION
        UUID id = loseDoorCard_Preparation();
        // EXECUTION
        onirimService.loseDoorCard(id, 0);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // We must have 1 discovered door.
        assertThat(gameDTO.getBoard().getDiscoveredDoors().size()).isEqualTo(1);
        // We must have 1 card in the limbo stack.
        assertThat(gameDTO.getBoard().getLimboStack().size()).isEqualTo(1);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    private UUID discardTopCardsFromDeck_Preparation() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.BLUE));
        gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.YELLOW));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getCardDeck().add(new NightmareCardDTO());
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getAllowedActions().add(AllowedAction.DISCARD_TOP_CARDS_FROM_DECK);
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void discardTopCardsFromDeck() {
        // PREPARATION
        UUID id = discardTopCardsFromDeck_Preparation();
        // EXECUTION
        onirimService.discardTopCardsFromDeck(id);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // We must have 2 Labyrinth cards in the discarded cards zone.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(2);
        // We must have the other 3 cards in the limbo stack.
        assertThat(gameDTO.getBoard().getLimboStack().size()).isEqualTo(3);
        // The next allowed action must be to draw a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.DRAW_CARD_FROM_DECK);
    }

    private UUID discardPlayerHand_Preparation(boolean loseGame) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        if (loseGame) {
            gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.BLUE));
            gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.BLUE));
        } else {
            gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
            gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        }
        gameDTO.getBoard().getPlayerHand().add(new DoorCardDTO(Color.BLUE));
        gameDTO.getBoard().getPlayerHand().add(new DoorCardDTO(Color.YELLOW));
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN));
        gameDTO.getBoard().getPlayerHand().add(new NightmareCardDTO());
        gameDTO.getBoard().getPlayerHand().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY));
        gameDTO.getAllowedActions().add(AllowedAction.DISCARD_PLAYER_HAND);
        return onirimService.saveGame(gameDTO).getId();
    }

    @Test
    void discardPlayerHand_loseGame() {
        // PREPARATION
        UUID id = discardPlayerHand_Preparation(true);
        // EXECUTION
        onirimService.discardPlayerHand(id);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // We must have 5 cards in the discarded cards zone.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(5);
        // The game must be finished.
        assertThat(gameDTO.getGameStatus()).isEqualTo(GameStatus.FINISHED);
    }

    @Test
    void discardPlayerHand() {
        // PREPARATION
        UUID id = discardPlayerHand_Preparation(false);
        // EXECUTION
        onirimService.discardPlayerHand(id);
        // VERIFICATION
        GameDTO gameDTO = getGameForTest(id);
        // We must have 5 cards in the discarded cards zone.
        assertThat(gameDTO.getBoard().getDiscardedCards().size()).isEqualTo(5);
        // We must have 5 cards in the player hand.
        assertThat(gameDTO.getBoard().getPlayerHand().size()).isEqualTo(5);
        // The next allowed actions must be to play or discard a card.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(2);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.PLAY_CARD_FROM_HAND);
        assertThat(gameDTO.getAllowedActions().get(1)).isEqualTo(AllowedAction.DISCARD_CARD_FROM_HAND);
    }

}