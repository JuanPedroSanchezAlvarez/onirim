package com.misispiclix.singleplayergames.onirim.service.impl;

import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.CardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.NightmareCardDTO;
import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import com.misispiclix.singleplayergames.onirim.repository.IOnirimRepository;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OnirimServiceImpl implements IOnirimService {

    private final IOnirimRepository onirimRepository;

    public OnirimServiceImpl(IOnirimRepository onirimRepository) {
        this.onirimRepository = onirimRepository;
    }

    @Override
    public GameDTO createNewGame() {
        GameDTO gameDTO = new GameDTO();
        initializeCardDeck(gameDTO);
        initializePlayerHand(gameDTO);
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        return gameDTO;
    }

    @Override
    public GameDTO playCardFromHand(GameDTO gameDTO, Integer playedCardIndex) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.PLAY_CARD_FROM_HAND)) { return gameDTO; }
        // We check that the chosen card exists in the hand.
        if (!validatePlayedCardIndex(gameDTO, playedCardIndex)) { return gameDTO; }
        // We check that the chosen card has a different symbol than the last played.
        if (!validateDifferentSymbol(gameDTO, playedCardIndex)) { return gameDTO; }
        // We play the chosen card.
        playCard(gameDTO, playedCardIndex);
        // We check that the card just played is the third consecutive card of the same color.
        if (validateThirdConsecutiveCardOfTheSameColor(gameDTO.getBoardDTO().getPlayedCards())) {
            // We look for a door card of that same color and play it.
            discoverDoor(gameDTO);
        }
        // We check if all the door cards have been discovered.
        if (validateAllDoorsDiscovered(gameDTO)) { return gameDTO; }
        // We must draw a card.
        gameDTO.getAllowedActions().clear();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        return gameDTO;
    }

    @Override
    public GameDTO discardCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.DISCARD_CARD_FROM_HAND)) { return gameDTO; }
        // We check that the chosen card exists in the hand.
        if (!validatePlayedCardIndex(gameDTO, discardedCardIndex)) { return gameDTO; }
        // We discard the chosen card.
        discardCard(gameDTO, discardedCardIndex);
        // We check that the discarded card has the key symbol.
        gameDTO.getAllowedActions().clear();
        if (validateDiscardedCardHasKeySymbol(gameDTO.getBoardDTO().getDiscardedCardDTOS().get(gameDTO.getBoardDTO().getDiscardedCardDTOS().size() - 1))
                && !gameDTO.getBoardDTO().getCardDTODeck().isEmpty()) {
            // We must activate a prophecy.
            gameDTO.getAllowedActions().add(AllowedAction.ACTIVATE_PROPHECY);
        } else {
            // We must draw a card.
            gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        }
        return gameDTO;
    }

    @Override
    public GameDTO activateProphecy(GameDTO gameDTO) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.ACTIVATE_PROPHECY)) { return gameDTO; }
        // We show the prophecy cards.
        showProphecyCards(gameDTO);
        // We must confirm the prophecy.
        gameDTO.getAllowedActions().clear();
        gameDTO.getAllowedActions().add(AllowedAction.CONFIRM_PROPHECY);
        return gameDTO;
    }

    @Override
    public GameDTO confirmProphecy(GameDTO gameDTO, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.CONFIRM_PROPHECY)) { return gameDTO; }
        // We discard the chosen card.
        gameDTO.getBoardDTO().getDiscardedCardDTOS().add(gameDTO.getBoardDTO().getCardsToShow().get(discardedCardIndex));
        // We rearrange the top cards of the main deck in the chosen order.
        rearrangeTopCardsOfTheCardDeck(gameDTO, reorderedCardIndexes);
        // We must draw a card.
        gameDTO.getAllowedActions().clear();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        return gameDTO;
    }

    @Override
    public GameDTO drawCardFromDeck(GameDTO gameDTO) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.DRAW_CARD_FROM_DECK)) { return gameDTO; }
        // We check that the main deck is not empty.
        if (!validateCardDeckNotEmpty(gameDTO)) { return gameDTO; }
        // We draw a card from the main deck.
        drawCard(gameDTO);
        // We check the type of card that has been drawn and act accordingly.
        checkTypeOfCardDrawn(gameDTO);
        // We check if all the door cards have been discovered.
        validateAllDoorsDiscovered(gameDTO);
        return gameDTO;
    }

    @Override
    public GameDTO discardKeyCardFromHand(GameDTO gameDTO, Integer discardedCardIndex) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.DISCARD_KEY_CARD_FROM_HAND)) { return gameDTO; }
        // We check that the chosen card exists in the hand.
        if (!validatePlayedCardIndex(gameDTO, discardedCardIndex)) { return gameDTO; }
        // We check that the chosen card is a key card.
        if (!validateChosenCardIsKeyCard(gameDTO, discardedCardIndex)) { return gameDTO; }
        // We discard the chosen key card from hand.
        gameDTO.getBoardDTO().getDiscardedCardDTOS().add(gameDTO.getBoardDTO().getPlayerHand().remove(discardedCardIndex.intValue()));
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        return gameDTO;
    }

    @Override
    public GameDTO loseDoorCard(GameDTO gameDTO, Integer doorCardIndex) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.LOSE_DOOR_CARD)) { return gameDTO; }
        // We check that the chosen door card exists in the discovered doors zone.
        if (!validateDiscardedDoorIndex(gameDTO, doorCardIndex)) { return gameDTO; }
        // We move the chosen door card to the limbo stack.
        gameDTO.getBoardDTO().getLimboStack().add(gameDTO.getBoardDTO().getDiscoveredDoors().remove(doorCardIndex.intValue()));
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        return gameDTO;
    }

    @Override
    public GameDTO discardTopCardsFromDeck(GameDTO gameDTO) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.DISCARD_TOP_CARDS_FROM_DECK)) { return gameDTO; }
        // We discard the top cards from the main deck.
        int numberOfCardsToDiscard = Math.min(gameDTO.getBoardDTO().getCardDTODeck().size(), 5);
        for (int i = 0; i < numberOfCardsToDiscard; i++) {
            if (gameDTO.getBoardDTO().getCardDTODeck().get(gameDTO.getBoardDTO().getCardDTODeck().size() - 1) instanceof LabyrinthCardDTO) {
                gameDTO.getBoardDTO().getDiscardedCardDTOS().add(gameDTO.getBoardDTO().getCardDTODeck().remove(gameDTO.getBoardDTO().getCardDTODeck().size() - 1));
            } else {
                gameDTO.getBoardDTO().getLimboStack().add(gameDTO.getBoardDTO().getCardDTODeck().remove(gameDTO.getBoardDTO().getCardDTODeck().size() - 1));
            }
        }
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        return gameDTO;
    }

    @Override
    public GameDTO discardPlayerHand(GameDTO gameDTO) {
        // We check that the action is allowed.
        if (!validateAllowedAction(gameDTO, AllowedAction.DISCARD_PLAYER_HAND)) { return gameDTO; }
        // We discard the entire player hand.
        while (!gameDTO.getBoardDTO().getPlayerHand().isEmpty()) {
            gameDTO.getBoardDTO().getDiscardedCardDTOS().add(gameDTO.getBoardDTO().getPlayerHand().remove(gameDTO.getBoardDTO().getPlayerHand().size() - 1));
        }
        // We draw a new set of five cards.
        initializePlayerHand(gameDTO);
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        return gameDTO;
    }

    @Override
    public Iterable<Game> getExample() {
        return onirimRepository.findAll();
    }

    private void initializeCardDeck(GameDTO gameDTO) {
        for (int i = 0; i < 10; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new NightmareCardDTO()); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.RED, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.RED, Symbol.MOON)); }
        for (int i = 0; i < 9; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.GREEN, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON)); }
        for (int i = 0; i < 7; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.GREEN, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON)); }
        for (int i = 0; i < 8; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.MOON)); }
        for (int i = 0; i < 6; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.SUN)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new DoorCardDTO(Color.RED)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new DoorCardDTO(Color.GREEN)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new DoorCardDTO(Color.BLUE)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoardDTO().getCardDTODeck().add(new DoorCardDTO(Color.YELLOW)); }
        shuffleCardDeck(gameDTO);
    }

    private void shuffleCardDeck(GameDTO gameDTO) {
        gameDTO.getBoardDTO().getLimboStack().forEach(card -> { gameDTO.getBoardDTO().getCardDTODeck().add(card); });
        gameDTO.getBoardDTO().getLimboStack().clear();
        Collections.shuffle(gameDTO.getBoardDTO().getCardDTODeck());
    }

    private void initializePlayerHand(GameDTO gameDTO) {
        while (gameDTO.getBoardDTO().getPlayerHand().size() < 5) {
            if (gameDTO.getBoardDTO().getCardDTODeck().get(gameDTO.getBoardDTO().getCardDTODeck().size() - 1) instanceof LabyrinthCardDTO) {
                gameDTO.getBoardDTO().getPlayerHand().add(gameDTO.getBoardDTO().getCardDTODeck().remove(gameDTO.getBoardDTO().getCardDTODeck().size() - 1));
            } else {
                gameDTO.getBoardDTO().getLimboStack().add(gameDTO.getBoardDTO().getCardDTODeck().remove(gameDTO.getBoardDTO().getCardDTODeck().size() - 1));
            }
        }
    }

    private void playCard(GameDTO gameDTO, Integer playedCardIndex) {
        gameDTO.getBoardDTO().getPlayedCards().add((LabyrinthCardDTO) gameDTO.getBoardDTO().getPlayerHand().remove(playedCardIndex.intValue()));
    }

    private void discardCard(GameDTO gameDTO, Integer discardedCardIndex) {
        gameDTO.getBoardDTO().getDiscardedCardDTOS().add(gameDTO.getBoardDTO().getPlayerHand().remove(discardedCardIndex.intValue()));
    }

    private void drawCard(GameDTO gameDTO) {
        gameDTO.getBoardDTO().getPlayerHand().add(gameDTO.getBoardDTO().getCardDTODeck().remove(gameDTO.getBoardDTO().getCardDTODeck().size() - 1));
    }

    private void discoverDoor(GameDTO gameDTO) {
        for (int i = 0; i < gameDTO.getBoardDTO().getCardDTODeck().size(); i++) {
            if (gameDTO.getBoardDTO().getCardDTODeck().get(i) instanceof DoorCardDTO doorCard) {
                if (doorCard.getColor().equals(gameDTO.getBoardDTO().getPlayedCards().get(gameDTO.getBoardDTO().getPlayedCards().size() - 1).getColor())) {
                    gameDTO.getBoardDTO().getDiscoveredDoors().add((DoorCardDTO) gameDTO.getBoardDTO().getCardDTODeck().remove(i));
                    break;
                }
            }
        }
        shuffleCardDeck(gameDTO);
    }

    private void showProphecyCards(GameDTO gameDTO) {
        int numberOfCardsToShow = Math.min(gameDTO.getBoardDTO().getCardDTODeck().size(), 5);
        for (int i = 0; i < numberOfCardsToShow; i++) {
            gameDTO.getBoardDTO().getCardsToShow().add(gameDTO.getBoardDTO().getCardDTODeck().remove(gameDTO.getBoardDTO().getCardDTODeck().size() - 1));
        }
    }

    private void rearrangeTopCardsOfTheCardDeck(GameDTO gameDTO, List<Integer> reorderedCardIndexes) {
        List<CardDTO> reorderedCardListDTO = new ArrayList<>();
        for (int i = 0; i < reorderedCardIndexes.size(); i++) {
            int j = 0;
            for (Integer index : reorderedCardIndexes) {
                if (i == index) {
                    reorderedCardListDTO.add(gameDTO.getBoardDTO().getCardsToShow().get(j));
                }
                j++;
            }
        }
        while (!reorderedCardListDTO.isEmpty()) {
            gameDTO.getBoardDTO().getCardDTODeck().add(reorderedCardListDTO.remove(reorderedCardListDTO.size() - 1));
        }
        gameDTO.getBoardDTO().getCardsToShow().clear();
    }

    private void checkTypeOfCardDrawn(GameDTO gameDTO) {
        switch (gameDTO.getBoardDTO().getPlayerHand().get(gameDTO.getBoardDTO().getPlayerHand().size() - 1)) {
            case DoorCardDTO doorCard -> { doorCardDrawnAction(gameDTO, doorCard); }
            case NightmareCardDTO ignored -> { nightmareCardDrawnAction(gameDTO); }
            default -> { checkPlayerHandSizeAndSetAllowedActions(gameDTO); }
        };
    }

    private void checkPlayerHandSizeAndSetAllowedActions(GameDTO gameDTO) {
        gameDTO.getAllowedActions().clear();
        if (gameDTO.getBoardDTO().getPlayerHand().size() >= 5) {
            gameDTO.getAllowedActions().add(AllowedAction.PLAY_CARD_FROM_HAND);
            gameDTO.getAllowedActions().add(AllowedAction.DISCARD_CARD_FROM_HAND);
            shuffleCardDeck(gameDTO);
        } else {
            gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        }
    }

    private void doorCardDrawnAction(GameDTO gameDTO, DoorCardDTO doorCard) {
        // We check if we have a labyrinth card in hand with the key symbol and the same color as the drawn door card.
        boolean cardWithKeySymbolAndSameColorFound = false;
        for (int i = 0; i < gameDTO.getBoardDTO().getPlayerHand().size(); i++) {
            if (gameDTO.getBoardDTO().getPlayerHand().get(i) instanceof LabyrinthCardDTO labyrinthCard) {
                // If we have it, we discover the door and discard the key card.
                if (labyrinthCard.getSymbol().equals(Symbol.KEY) && labyrinthCard.getColor().equals(doorCard.getColor())) {
                    cardWithKeySymbolAndSameColorFound = true;
                    gameDTO.getBoardDTO().getDiscoveredDoors().add((DoorCardDTO) gameDTO.getBoardDTO().getPlayerHand().remove(gameDTO.getBoardDTO().getPlayerHand().size() - 1));
                    gameDTO.getBoardDTO().getDiscardedCardDTOS().add(gameDTO.getBoardDTO().getPlayerHand().remove(i));
                    break;
                }
            }
        }
        // If we don't have it, we put the door card in the limbo stack.
        if (!cardWithKeySymbolAndSameColorFound) {
            gameDTO.getBoardDTO().getLimboStack().add(gameDTO.getBoardDTO().getPlayerHand().remove(gameDTO.getBoardDTO().getPlayerHand().size() - 1));
        }
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
    }

    private void nightmareCardDrawnAction(GameDTO gameDTO) {
        gameDTO.getAllowedActions().clear();
        for (CardDTO cardDTO : gameDTO.getBoardDTO().getPlayerHand()) {
            if (cardDTO instanceof LabyrinthCardDTO labyrinthCard && labyrinthCard.getSymbol().equals(Symbol.KEY)) {
                gameDTO.getAllowedActions().add(AllowedAction.DISCARD_KEY_CARD_FROM_HAND);
                break;
            }
        }
        if (!gameDTO.getBoardDTO().getDiscoveredDoors().isEmpty()) {
            gameDTO.getAllowedActions().add(AllowedAction.LOSE_DOOR_CARD);
        }
        if (!gameDTO.getBoardDTO().getCardDTODeck().isEmpty()) {
            gameDTO.getAllowedActions().add(AllowedAction.DISCARD_TOP_CARDS_FROM_DECK);
            if (gameDTO.getBoardDTO().getCardDTODeck().size() >= 5) {
                gameDTO.getAllowedActions().add(AllowedAction.DISCARD_PLAYER_HAND);
            }
        }
        if (gameDTO.getAllowedActions().isEmpty()) {
            gameDTO.setMessageToDisplay("Game Over. YOU LOSE.");
        }
    }

    private boolean validateAllowedAction(GameDTO gameDTO, AllowedAction allowedAction) {
        gameDTO.setMessageToDisplay(gameDTO.getAllowedActions().contains(allowedAction) ? "" : "Action not allowed.");
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private boolean validatePlayedCardIndex(GameDTO gameDTO, Integer playedCardIndex) {
        gameDTO.setMessageToDisplay(playedCardIndex > -1 && playedCardIndex < gameDTO.getBoardDTO().getPlayerHand().size() ? "" : "Selected card is not in hand.");
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private boolean validateDiscardedDoorIndex(GameDTO gameDTO, Integer doorCardIndex) {
        gameDTO.setMessageToDisplay(doorCardIndex > -1 && doorCardIndex < gameDTO.getBoardDTO().getDiscoveredDoors().size() ? "" : "Selected door is not discovered.");
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private boolean validateDifferentSymbol(GameDTO gameDTO, Integer playedCardIndex) {
        if (gameDTO.getBoardDTO().getPlayedCards().isEmpty()) { return true; }
        if (gameDTO.getBoardDTO().getPlayerHand().get(playedCardIndex) instanceof LabyrinthCardDTO selectedCard) {
            LabyrinthCardDTO lastCardPlayed = gameDTO.getBoardDTO().getPlayedCards().get(gameDTO.getBoardDTO().getPlayedCards().size() -1);
            gameDTO.setMessageToDisplay(selectedCard.getSymbol().equals(lastCardPlayed.getSymbol()) ? "The chosen card must have a different symbol than the last card played." : "");
        } else {
            gameDTO.setMessageToDisplay("Selected card is not a Labyrinth Card.");
        }
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private boolean validateThirdConsecutiveCardOfTheSameColor(List<LabyrinthCardDTO> playedCards) {
        if (playedCards.size() < 3) { return false; }
        LabyrinthCardDTO lastCard = playedCards.get(playedCards.size() -1);
        LabyrinthCardDTO penultimateCard = playedCards.get(playedCards.size() -2);
        LabyrinthCardDTO beforePenultimateCard = playedCards.get(playedCards.size() -3);
        boolean lastThreeCardsOfTheSameColor = lastCard.getColor().equals(penultimateCard.getColor()) && lastCard.getColor().equals(beforePenultimateCard.getColor());
        if (playedCards.size() == 3) { return lastThreeCardsOfTheSameColor; }
        LabyrinthCardDTO fourthCard = playedCards.get(playedCards.size() -4);
        boolean fourthCardOfTheSameColor = lastCard.getColor().equals(fourthCard.getColor());
        return lastThreeCardsOfTheSameColor && !fourthCardOfTheSameColor;
    }

    private boolean validateDiscardedCardHasKeySymbol(CardDTO discardedCardDTO) {
        return discardedCardDTO instanceof LabyrinthCardDTO discardedLabyrinthCard && discardedLabyrinthCard.getSymbol().equals(Symbol.KEY);
    }

    private boolean validateCardDeckNotEmpty(GameDTO gameDTO) {
        gameDTO.setMessageToDisplay(gameDTO.getBoardDTO().getCardDTODeck().isEmpty() ? "Game Over. YOU LOSE." : "");
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private boolean validateChosenCardIsKeyCard(GameDTO gameDTO, Integer discardedCardIndex) {
        if (gameDTO.getBoardDTO().getPlayerHand().get(discardedCardIndex) instanceof LabyrinthCardDTO discardedLabyrinthCard
                && discardedLabyrinthCard.getSymbol().equals(Symbol.KEY)) {
            gameDTO.setMessageToDisplay("");
        } else {
            gameDTO.setMessageToDisplay("Selected card is not a KEY card.");
        }
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private boolean validateAllDoorsDiscovered(GameDTO gameDTO) {
        gameDTO.setMessageToDisplay(gameDTO.getBoardDTO().getDiscoveredDoors().size() == 8 ? "Game Over. YOU WIN." : "");
        return !gameDTO.getMessageToDisplay().isEmpty();
    }

}
