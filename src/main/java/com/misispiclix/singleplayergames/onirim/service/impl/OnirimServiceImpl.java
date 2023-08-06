package com.misispiclix.singleplayergames.onirim.service.impl;

import com.misispiclix.singleplayergames.onirim.dto.GameDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.CardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.DoorCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.LabyrinthCardDTO;
import com.misispiclix.singleplayergames.onirim.dto.card.NightmareCardDTO;
import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.enums.Color;
import com.misispiclix.singleplayergames.onirim.enums.GameStatus;
import com.misispiclix.singleplayergames.onirim.enums.Symbol;
import com.misispiclix.singleplayergames.onirim.exception.*;
import com.misispiclix.singleplayergames.onirim.mapper.IOnirimMapper;
import com.misispiclix.singleplayergames.onirim.repository.IOnirimRepository;
import com.misispiclix.singleplayergames.onirim.service.IOnirimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service(value = "onirimServiceImpl")
public class OnirimServiceImpl implements IOnirimService {

    private final IOnirimRepository onirimRepository;
    private final IOnirimMapper onirimMapper;

    @Override
    public Page<GameDTO> getGames(Integer pageNumber, Integer pageSize) {
        //return onirimRepository.findAll().stream().map(onirimMapper::gameToGameDto).collect(Collectors.toList());
        Sort sort = Sort.by(Sort.Order.desc("created"));
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return onirimRepository.findAll(pageRequest).map(onirimMapper::gameToGameDto);
    }

    @Override
    public Optional<GameDTO> getGameById(UUID id) {
        return Optional.ofNullable(onirimMapper.gameToGameDto(onirimRepository.findById(id).orElse(null)));
    }

    @Override
    public UUID createNewGame() {
        GameDTO gameDTO = new GameDTO();
        initializeCardDeck(gameDTO);
        initializePlayerHand(gameDTO);
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        gameDTO.setGameStatus(GameStatus.PLAYING);
        return saveGame(gameDTO).getId();
    }

    @Override
    public GameDTO saveGame(GameDTO gameDTO) {
        return onirimMapper.gameToGameDto(onirimRepository.save(onirimMapper.gameDtoToGame(gameDTO)));
    }

    @Override
    public void playCardFromHand(UUID id, Integer playedCardIndex) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.PLAY_CARD_FROM_HAND);
        // We check that the chosen card exists in the hand.
        validatePlayedCardIndex(gameDTO, playedCardIndex);
        // We check that the chosen card has a different symbol than the last played.
        validateDifferentSymbol(gameDTO, playedCardIndex);
        // We remove the current allowed actions.
        gameDTO.getAllowedActions().clear();
        // We play the chosen card.
        playCard(gameDTO, playedCardIndex);
        // We check that the card just played is the third consecutive card of the same color.
        if (validateThirdConsecutiveCardOfTheSameColor(gameDTO.getBoard().getPlayedCards())) {
            // We look for a door card of that same color and play it.
            discoverDoor(gameDTO);
        }
        // We check that all the door cards have not been discovered yet.
        if (validateAllDoorsNotDiscovered(gameDTO)) {
            // We must draw a card as the next allowed action.
            gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        }
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void discardCardFromHand(UUID id, Integer discardedCardIndex) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.DISCARD_CARD_FROM_HAND);
        // We check that the chosen card exists in the hand.
        validatePlayedCardIndex(gameDTO, discardedCardIndex);
        // We discard the chosen card.
        discardCard(gameDTO, discardedCardIndex);
        // We remove the current allowed actions.
        gameDTO.getAllowedActions().clear();
        // We check that the discarded card has the key symbol.
        if (validateDiscardedCardHasKeySymbol(gameDTO.getBoard().getDiscardedCards().get(gameDTO.getBoard().getDiscardedCards().size() - 1))
                && !gameDTO.getBoard().getCardDeck().isEmpty()) {
            // We must activate a prophecy.
            gameDTO.getAllowedActions().add(AllowedAction.ACTIVATE_PROPHECY);
        } else {
            // We must draw a card.
            gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        }
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void activateProphecy(UUID id) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.ACTIVATE_PROPHECY);
        // We show the prophecy cards.
        showProphecyCards(gameDTO);
        // We must confirm the prophecy as the next action.
        gameDTO.getAllowedActions().clear();
        gameDTO.getAllowedActions().add(AllowedAction.CONFIRM_PROPHECY);
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void confirmProphecy(UUID id, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.CONFIRM_PROPHECY);
        // We check that the discarded card index and the reordered cards indexes are valid.
        validateProphecyCardsIndexes(gameDTO, discardedCardIndex, reorderedCardIndexes);
        // We discard the chosen card.
        gameDTO.getBoard().getDiscardedCards().add(gameDTO.getBoard().getCardsToShow().get(discardedCardIndex));
        // We rearrange the top cards of the main deck in the chosen order.
        rearrangeTopCardsOfTheCardDeck(gameDTO, reorderedCardIndexes);
        // We must draw a card as the next action.
        gameDTO.getAllowedActions().clear();
        gameDTO.getAllowedActions().add(AllowedAction.DRAW_CARD_FROM_DECK);
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void drawCardFromDeck(UUID id) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.DRAW_CARD_FROM_DECK);
        // We remove the current allowed actions.
        gameDTO.getAllowedActions().clear();
        // We check that the main deck is not empty.
        if (validateCardDeckNotEmpty(gameDTO)) {
            // We draw a card from the main deck.
            drawCard(gameDTO);
            // We check the type of card that has been drawn and act accordingly.
            checkTypeOfCardDrawn(gameDTO);
            // We check if all the door cards have not been discovered yet.
            if (validateAllDoorsNotDiscovered(gameDTO)) {
                // We check if there are no allowed actions available.
                if (gameDTO.getAllowedActions().isEmpty()) {
                    gameDTO.setMessageToDisplay("Game Over. YOU LOSE.");
                    gameDTO.setGameStatus(GameStatus.FINISHED);
                }
            }
        }
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void discardKeyCardFromHand(UUID id, Integer discardedCardIndex) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.DISCARD_KEY_CARD_FROM_HAND);
        // We check that the chosen card exists in the hand.
        validatePlayedCardIndex(gameDTO, discardedCardIndex);
        // We check that the chosen card is a key card.
        validateChosenCardIsKeyCard(gameDTO, discardedCardIndex);
        // We discard the chosen key card from hand.
        gameDTO.getBoard().getDiscardedCards().add(gameDTO.getBoard().getPlayerHand().remove(discardedCardIndex.intValue()));
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void loseDoorCard(UUID id, Integer doorCardIndex) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.LOSE_DOOR_CARD);
        // We check that the chosen door card exists in the discovered doors zone.
        validateDiscardedDoorIndex(gameDTO, doorCardIndex);
        // We move the chosen door card to the limbo stack.
        gameDTO.getBoard().getLimboStack().add(gameDTO.getBoard().getDiscoveredDoors().remove(doorCardIndex.intValue()));
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void discardTopCardsFromDeck(UUID id) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.DISCARD_TOP_CARDS_FROM_DECK);
        // We discard the top cards from the main deck.
        int numberOfCardsToDiscard = Math.min(gameDTO.getBoard().getCardDeck().size(), 5);
        for (int i = 0; i < numberOfCardsToDiscard; i++) {
            if (gameDTO.getBoard().getCardDeck().get(gameDTO.getBoard().getCardDeck().size() - 1) instanceof LabyrinthCardDTO) {
                gameDTO.getBoard().getDiscardedCards().add(gameDTO.getBoard().getCardDeck().remove(gameDTO.getBoard().getCardDeck().size() - 1));
            } else {
                gameDTO.getBoard().getLimboStack().add(gameDTO.getBoard().getCardDeck().remove(gameDTO.getBoard().getCardDeck().size() - 1));
            }
        }
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        // We save the game in the database.
        saveGame(gameDTO);
    }

    @Override
    public void discardPlayerHand(UUID id) {
        // We look for the game in the database.
        GameDTO gameDTO = getGameById(id).orElseThrow(GameNotFoundException::new);
        // We check that the action is allowed.
        validateAllowedAction(gameDTO, AllowedAction.DISCARD_PLAYER_HAND);
        // We discard the entire player hand.
        while (!gameDTO.getBoard().getPlayerHand().isEmpty()) {
            gameDTO.getBoard().getDiscardedCards().add(gameDTO.getBoard().getPlayerHand().remove(gameDTO.getBoard().getPlayerHand().size() - 1));
        }
        // We draw a new set of five cards.
        initializePlayerHand(gameDTO);
        // We set the next allowed actions.
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        // We save the game in the database.
        saveGame(gameDTO);
    }


    /*@Override
    public GameDTO createExample(GameDTO gameDTO) {
        return onirimMapper.gameToGameDto(onirimRepository.save(onirimMapper.gameDtoToGame(gameDTO)));
    }*/

    /*@Override
    public Optional<GameDTO> updateExample(UUID id, GameDTO gameDTO) {*/
        //onirimRepository.findById(id).ifPresent(onirimRepository::save);
        /*Game existingGame = onirimRepository.findById(id).orElse(null);
        if (null != existingGame) {
            // Map game to existingGame.
            onirimRepository.save(existingGame);
        }*/
        /*AtomicReference<Optional<GameDTO>> atomicReference = new AtomicReference<>();
        onirimRepository.findById(id).ifPresentOrElse(foundGame -> {
            foundGame.setMessageToDisplay("UPDATED");
            atomicReference.set(Optional.of(onirimMapper.gameToGameDto(onirimRepository.save(foundGame))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }*/

    /*@Override
    public void updateExamplePatch(UUID id, GameDTO gameDTO) {
        //onirimRepository.findById(id).ifPresent(onirimRepository::save);
        Game existingGame = onirimRepository.findById(id).orElse(null);
        if (null != existingGame) {
            // Map game to existingGame. Only not null properties.
            onirimRepository.save(existingGame);
        }
    }

    @Override
    public Boolean deleteExample(UUID id) {
        if (onirimRepository.existsById(id)) {
            onirimRepository.deleteById(id);
            return true;
        }
        return false;
    }*/

    private void initializeCardDeck(GameDTO gameDTO) {
        for (int i = 0; i < 10; i++) { gameDTO.getBoard().getCardDeck().add(new NightmareCardDTO()); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.MOON)); }
        for (int i = 0; i < 9; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.RED, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.GREEN, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.GREEN, Symbol.MOON)); }
        for (int i = 0; i < 7; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.GREEN, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.MOON)); }
        for (int i = 0; i < 8; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.BLUE, Symbol.SUN)); }
        for (int i = 0; i < 3; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.KEY)); }
        for (int i = 0; i < 4; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.MOON)); }
        for (int i = 0; i < 6; i++) { gameDTO.getBoard().getCardDeck().add(new LabyrinthCardDTO(Color.YELLOW, Symbol.SUN)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.RED)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.GREEN)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.BLUE)); }
        for (int i = 0; i < 2; i++) { gameDTO.getBoard().getCardDeck().add(new DoorCardDTO(Color.YELLOW)); }
        shuffleCardDeck(gameDTO);
    }

    private void shuffleCardDeck(GameDTO gameDTO) {
        gameDTO.getBoard().getLimboStack().forEach(card -> { gameDTO.getBoard().getCardDeck().add(card); });
        gameDTO.getBoard().getLimboStack().clear();
        Collections.shuffle(gameDTO.getBoard().getCardDeck());
    }

    private void initializePlayerHand(GameDTO gameDTO) {
        while (gameDTO.getBoard().getPlayerHand().size() < 5) {
            if (gameDTO.getBoard().getCardDeck().get(gameDTO.getBoard().getCardDeck().size() - 1) instanceof LabyrinthCardDTO) {
                gameDTO.getBoard().getPlayerHand().add(gameDTO.getBoard().getCardDeck().remove(gameDTO.getBoard().getCardDeck().size() - 1));
            } else {
                gameDTO.getBoard().getLimboStack().add(gameDTO.getBoard().getCardDeck().remove(gameDTO.getBoard().getCardDeck().size() - 1));
            }
        }
    }

    private void playCard(GameDTO gameDTO, Integer playedCardIndex) {
        gameDTO.getBoard().getPlayedCards().add((LabyrinthCardDTO) gameDTO.getBoard().getPlayerHand().remove(playedCardIndex.intValue()));
    }

    private void discardCard(GameDTO gameDTO, Integer discardedCardIndex) {
        gameDTO.getBoard().getDiscardedCards().add(gameDTO.getBoard().getPlayerHand().remove(discardedCardIndex.intValue()));
    }

    private void drawCard(GameDTO gameDTO) {
        gameDTO.getBoard().getPlayerHand().add(gameDTO.getBoard().getCardDeck().remove(gameDTO.getBoard().getCardDeck().size() - 1));
    }

    private void discoverDoor(GameDTO gameDTO) {
        for (int i = 0; i < gameDTO.getBoard().getCardDeck().size(); i++) {
            if (gameDTO.getBoard().getCardDeck().get(i) instanceof DoorCardDTO doorCard) {
                if (doorCard.getColor().equals(gameDTO.getBoard().getPlayedCards().get(gameDTO.getBoard().getPlayedCards().size() - 1).getColor())) {
                    gameDTO.getBoard().getDiscoveredDoors().add((DoorCardDTO) gameDTO.getBoard().getCardDeck().remove(i));
                    break;
                }
            }
        }
        shuffleCardDeck(gameDTO);
    }

    private void showProphecyCards(GameDTO gameDTO) {
        int numberOfCardsToShow = Math.min(gameDTO.getBoard().getCardDeck().size(), 5);
        for (int i = 0; i < numberOfCardsToShow; i++) {
            gameDTO.getBoard().getCardsToShow().add(gameDTO.getBoard().getCardDeck().remove(gameDTO.getBoard().getCardDeck().size() - 1));
        }
    }

    private void rearrangeTopCardsOfTheCardDeck(GameDTO gameDTO, List<Integer> reorderedCardIndexes) {
        List<CardDTO> reorderedCardListDTO = new ArrayList<>();
        for (int i = 0; i < reorderedCardIndexes.size(); i++) {
            int j = 0;
            for (Integer index : reorderedCardIndexes) {
                if (i == index) {
                    reorderedCardListDTO.add(gameDTO.getBoard().getCardsToShow().get(j));
                }
                j++;
            }
        }
        while (!reorderedCardListDTO.isEmpty()) {
            gameDTO.getBoard().getCardDeck().add(reorderedCardListDTO.remove(reorderedCardListDTO.size() - 1));
        }
        gameDTO.getBoard().getCardsToShow().clear();
    }

    private void checkTypeOfCardDrawn(GameDTO gameDTO) {
        if (gameDTO.getBoard().getPlayerHand().get(gameDTO.getBoard().getPlayerHand().size() - 1) instanceof DoorCardDTO doorCard) {
            doorCardDrawnAction(gameDTO, doorCard);
        } else if (gameDTO.getBoard().getPlayerHand().get(gameDTO.getBoard().getPlayerHand().size() - 1) instanceof NightmareCardDTO) {
            nightmareCardDrawnAction(gameDTO);
        } else {
            checkPlayerHandSizeAndSetAllowedActions(gameDTO);
        }
    }

    private void checkPlayerHandSizeAndSetAllowedActions(GameDTO gameDTO) {
        if (gameDTO.getBoard().getPlayerHand().size() >= 5) {
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
        for (int i = 0; i < gameDTO.getBoard().getPlayerHand().size(); i++) {
            if (gameDTO.getBoard().getPlayerHand().get(i) instanceof LabyrinthCardDTO labyrinthCard) {
                // If we have it, we discover the door and discard the key card.
                if (labyrinthCard.getSymbol().equals(Symbol.KEY) && labyrinthCard.getColor().equals(doorCard.getColor())) {
                    cardWithKeySymbolAndSameColorFound = true;
                    gameDTO.getBoard().getDiscoveredDoors().add((DoorCardDTO) gameDTO.getBoard().getPlayerHand().remove(gameDTO.getBoard().getPlayerHand().size() - 1));
                    gameDTO.getBoard().getDiscardedCards().add(gameDTO.getBoard().getPlayerHand().remove(i));
                    break;
                }
            }
        }
        // If we don't have it, we put the door card in the limbo stack.
        if (!cardWithKeySymbolAndSameColorFound) {
            gameDTO.getBoard().getLimboStack().add(gameDTO.getBoard().getPlayerHand().remove(gameDTO.getBoard().getPlayerHand().size() - 1));
        }
        checkPlayerHandSizeAndSetAllowedActions(gameDTO);
    }

    private void nightmareCardDrawnAction(GameDTO gameDTO) {
        for (CardDTO cardDTO : gameDTO.getBoard().getPlayerHand()) {
            if (cardDTO instanceof LabyrinthCardDTO labyrinthCard && labyrinthCard.getSymbol().equals(Symbol.KEY)) {
                gameDTO.getAllowedActions().add(AllowedAction.DISCARD_KEY_CARD_FROM_HAND);
                break;
            }
        }
        if (!gameDTO.getBoard().getDiscoveredDoors().isEmpty()) {
            gameDTO.getAllowedActions().add(AllowedAction.LOSE_DOOR_CARD);
        }
        if (!gameDTO.getBoard().getCardDeck().isEmpty()) {
            gameDTO.getAllowedActions().add(AllowedAction.DISCARD_TOP_CARDS_FROM_DECK);
            if (gameDTO.getBoard().getCardDeck().size() >= 5) {
                gameDTO.getAllowedActions().add(AllowedAction.DISCARD_PLAYER_HAND);
            }
        }
    }

    private void validateAllowedAction(GameDTO gameDTO, AllowedAction allowedAction) {
        if (!gameDTO.getAllowedActions().contains(allowedAction)) {
            throw new ActionNotAllowedException("Action " + allowedAction.toString() + " not allowed. The actions allowed for the game '" + gameDTO.getId().toString() + "' are: " + gameDTO.getAllowedActions().toString() + ".");
        }
    }

    private void validatePlayedCardIndex(GameDTO gameDTO, Integer playedCardIndex) {
        if (playedCardIndex < 0 || playedCardIndex > (gameDTO.getBoard().getPlayerHand().size() - 1)) {
            throw new InvalidCardIndexException("The card index must be between '0' and '" + (gameDTO.getBoard().getPlayerHand().size() - 1) + "'.");
        }
    }

    private void validateDiscardedDoorIndex(GameDTO gameDTO, Integer doorCardIndex) {
        if (doorCardIndex < 0 || doorCardIndex > (gameDTO.getBoard().getDiscoveredDoors().size() - 1)) {
            throw new InvalidCardIndexException("The card index must be between '0' and '" + (gameDTO.getBoard().getDiscoveredDoors().size() - 1) + "'.");
        }
    }

    private void validateDifferentSymbol(GameDTO gameDTO, Integer playedCardIndex) {
        if (gameDTO.getBoard().getPlayerHand().get(playedCardIndex) instanceof LabyrinthCardDTO selectedCard) {
            if (!gameDTO.getBoard().getPlayedCards().isEmpty()) {
                LabyrinthCardDTO lastCardPlayed = gameDTO.getBoard().getPlayedCards().get(gameDTO.getBoard().getPlayedCards().size() -1);
                if (selectedCard.getSymbol().equals(lastCardPlayed.getSymbol())) {
                    throw new EqualCardSymbolException("The selected card must have a different symbol than the last card played.");
                }
            }
        } else {
            throw new NotALabyrinthCardException("The selected card is not a Labyrinth Card.");
        }
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
        if (gameDTO.getBoard().getCardDeck().isEmpty()) {
            gameDTO.setMessageToDisplay("Game Over. YOU LOSE.");
            gameDTO.setGameStatus(GameStatus.FINISHED);
        } else {
            gameDTO.setMessageToDisplay("");
        }
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private void validateChosenCardIsKeyCard(GameDTO gameDTO, Integer discardedCardIndex) {
        if (gameDTO.getBoard().getPlayerHand().get(discardedCardIndex) instanceof LabyrinthCardDTO discardedLabyrinthCard) {
            if (!discardedLabyrinthCard.getSymbol().equals(Symbol.KEY)) {
                throw new NotAKeyCardException("The selected card is not a Key Card.");
            }
        } else {
            throw new NotALabyrinthCardException("The selected card is not a Labyrinth Card.");
        }
    }

    private boolean validateAllDoorsNotDiscovered(GameDTO gameDTO) {
        if (gameDTO.getBoard().getDiscoveredDoors().size() == 8) {
            gameDTO.setMessageToDisplay("Game Over. YOU WIN.");
            gameDTO.setGameStatus(GameStatus.FINISHED);
        } else {
            gameDTO.setMessageToDisplay("");
        }
        return gameDTO.getMessageToDisplay().isEmpty();
    }

    private void validateProphecyCardsIndexes(GameDTO gameDTO, Integer discardedCardIndex, List<Integer> reorderedCardIndexes) {
        if (discardedCardIndex < 0 || discardedCardIndex > (gameDTO.getBoard().getCardsToShow().size() - 1)) {
            throw new InvalidCardIndexException("The discarded card index must be between '0' and '" + (gameDTO.getBoard().getCardsToShow().size() - 1) + "'.");
        }
        if ((gameDTO.getBoard().getCardsToShow().size() - 1) != reorderedCardIndexes.size()) {
            throw new InvalidCardIndexException("The number of reordered cards must be '" + (gameDTO.getBoard().getCardsToShow().size() - 1) + "'.");
        }
        reorderedCardIndexes.forEach(index -> {
            if (index < 0 || index > (gameDTO.getBoard().getCardsToShow().size() - 2)) {
                throw new InvalidCardIndexException("The reordered cards indexes must be between '0' and '" + (gameDTO.getBoard().getCardsToShow().size() - 2) + "'.");
            }
            if (Collections.frequency(reorderedCardIndexes, index) > 1) {
                throw new InvalidCardIndexException("The reordered cards indexes must not be repeated.");
            }
        });
    }

}
