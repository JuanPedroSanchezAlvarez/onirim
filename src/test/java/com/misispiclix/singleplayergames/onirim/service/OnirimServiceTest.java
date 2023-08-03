package com.misispiclix.singleplayergames.onirim.service;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCardDTO;
import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.GameStatus;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import com.misispiclix.singleplayergames.onirim.exception.ActionNotAllowedException;
import com.misispiclix.singleplayergames.onirim.exception.EqualCardSymbolException;
import com.misispiclix.singleplayergames.onirim.exception.InvalidCardIndexException;
import com.misispiclix.singleplayergames.onirim.exception.NotALabyrinthCardException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OnirimServiceTest {

    @InjectMocks
    IOnirimService onirimService;

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
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = optionalOfGameDto.get();
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
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = onirimService.getGameById(id).get();
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
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = onirimService.getGameById(id).get();
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
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = onirimService.getGameById(id).get();
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
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = onirimService.getGameById(id).get();
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
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = onirimService.getGameById(id).get();
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
        Optional<GameDTO> optionalOfGameDto = onirimService.getGameById(id);
        assertThat(optionalOfGameDto).isNotEmpty();
        GameDTO gameDTO = onirimService.getGameById(id).get();
        // The cards to show zone must have 5 cards.
        assertThat(gameDTO.getBoard().getCardsToShow().size()).isEqualTo(5);
        // The next allowed action must be to confirm the prophecy.
        assertThat(gameDTO.getAllowedActions().size()).isEqualTo(1);
        assertThat(gameDTO.getAllowedActions().get(0)).isEqualTo(AllowedAction.CONFIRM_PROPHECY);
    }

    @Test
    void confirmProphecy() {
    }

    @Test
    void drawCardFromDeck() {
    }

    @Test
    void discardKeyCardFromHand() {
    }

    @Test
    void loseDoorCard() {
    }

    @Test
    void discardTopCardsFromDeck() {
    }

    @Test
    void discardPlayerHand() {
    }

}