package com.misispiclix.singleplayergames.onirim.bootstrap;

import com.misispiclix.singleplayergames.onirim.domain.Board;
import com.misispiclix.singleplayergames.onirim.domain.Game;
import com.misispiclix.singleplayergames.onirim.enums.AllowedAction;
import com.misispiclix.singleplayergames.onirim.repository.IOnirimRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {

    private final IOnirimRepository onirimRepository;

    public Bootstrap(IOnirimRepository onirimRepository) {
        this.onirimRepository = onirimRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Init Bootstrap...");

        Game game = new Game();
        Board board = new Board();
        game.setBoard(board);
        game.setAllowedActions(List.of(AllowedAction.PLAY_CARD_FROM_HAND, AllowedAction.DISCARD_CARD_FROM_HAND));
        game.setMessageToDisplay("Hello I am game 1");

        Game gameSaved = onirimRepository.save(game);

        System.out.println(gameSaved);
        System.out.println("End Bootstrap...");
    }

}
