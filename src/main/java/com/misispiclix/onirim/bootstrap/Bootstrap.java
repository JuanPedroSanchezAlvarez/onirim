package com.misispiclix.onirim.bootstrap;

import com.misispiclix.onirim.repository.IOnirimRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final IOnirimRepository onirimRepository;

    public Bootstrap(IOnirimRepository onirimRepository) {
        this.onirimRepository = onirimRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.debug("Init Bootstrap...");

        /* Game game = new Game();
        Board board = new Board();
        board.setCardDeck(new ArrayList<>());
        board.setDiscardedCards(new ArrayList<>());
        board.setLimboStack(new ArrayList<>());
        board.setCardsToShow(new ArrayList<>());
        board.setPlayedCards(new ArrayList<>());
        board.setDiscoveredDoors(new ArrayList<>());
        board.setPlayerHand(new ArrayList<>());

        DoorCard doorCard1 = new DoorCard();
        doorCard1.setColor(Color.BLUE);
        DoorCard doorCard2 = new DoorCard();
        doorCard2.setColor(Color.RED);
        DoorCard doorCard3 = new DoorCard();
        doorCard3.setColor(Color.GREEN);
        DoorCard doorCard4 = new DoorCard();
        doorCard4.setColor(Color.YELLOW);

        LabyrinthCard labyrinthCard1 = new LabyrinthCard();
        labyrinthCard1.setColor(Color.BLUE);
        labyrinthCard1.setSymbol(Symbol.KEY);
        LabyrinthCard labyrinthCard2 = new LabyrinthCard();
        labyrinthCard2.setColor(Color.YELLOW);
        labyrinthCard2.setSymbol(Symbol.SUN);
        LabyrinthCard labyrinthCard3 = new LabyrinthCard();
        labyrinthCard3.setColor(Color.GREEN);
        labyrinthCard3.setSymbol(Symbol.MOON);
        LabyrinthCard labyrinthCard4 = new LabyrinthCard();
        labyrinthCard4.setColor(Color.RED);
        labyrinthCard4.setSymbol(Symbol.KEY);
        LabyrinthCard labyrinthCard5 = new LabyrinthCard();
        labyrinthCard5.setColor(Color.BLUE);
        labyrinthCard5.setSymbol(Symbol.KEY);
        LabyrinthCard labyrinthCard6 = new LabyrinthCard();
        labyrinthCard6.setColor(Color.YELLOW);
        labyrinthCard6.setSymbol(Symbol.SUN);
        LabyrinthCard labyrinthCard7 = new LabyrinthCard();
        labyrinthCard7.setColor(Color.GREEN);
        labyrinthCard7.setSymbol(Symbol.MOON);
        LabyrinthCard labyrinthCard8 = new LabyrinthCard();
        labyrinthCard8.setColor(Color.RED);
        labyrinthCard8.setSymbol(Symbol.KEY);

        NightmareCard nightmareCard1 = new NightmareCard();
        NightmareCard nightmareCard2 = new NightmareCard();

        board.getCardDeck().add(nightmareCard1);
        board.getCardDeck().add(doorCard1);
        board.getDiscardedCards().add(nightmareCard2);
        board.getDiscardedCards().add(labyrinthCard1);
        board.getLimboStack().add(doorCard2);
        board.getLimboStack().add(labyrinthCard2);
        board.getCardsToShow().add(labyrinthCard3);
        board.getCardsToShow().add(labyrinthCard4);
        board.getPlayedCards().add(labyrinthCard5);
        board.getPlayedCards().add(labyrinthCard6);
        board.getPlayerHand().add(labyrinthCard7);
        board.getPlayerHand().add(labyrinthCard8);
        board.getDiscoveredDoors().add(doorCard3);
        board.getDiscoveredDoors().add(doorCard4);

        game.setBoard(board);
        game.setAllowedActions(List.of(AllowedAction.PLAY_CARD_FROM_HAND, AllowedAction.DISCARD_CARD_FROM_HAND));
        game.setMessageToDisplay("Hello I am game 1");

        Game gameSaved = onirimRepository.save(game);
        log.debug(gameSaved.toString());
        Optional<Game> gameRecovered = onirimRepository.findById(gameSaved.getId());
        log.debug(gameRecovered.get().toString()); */
        log.debug("End Bootstrap...");
    }

}
