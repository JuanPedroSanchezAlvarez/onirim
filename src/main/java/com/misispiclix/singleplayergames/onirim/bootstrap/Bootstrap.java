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
        Game game1 = new Game();
        Board board1 = new Board();
        game1.setBoard(board1);
        game1.setAllowedActions(List.of(AllowedAction.PLAY_CARD_FROM_HAND, AllowedAction.DISCARD_CARD_FROM_HAND));
        game1.setMessageToDisplay("Hello I am game 1");

        Game game2 = new Game();
        Board board2 = new Board();
        game2.setBoard(board2);
        game2.setAllowedActions(List.of(AllowedAction.ACTIVATE_PROPHECY, AllowedAction.CONFIRM_PROPHECY));
        game2.setMessageToDisplay("Hello I am game 2");

        Game game1Saved = onirimRepository.save(game1);
        Game game2Saved = onirimRepository.save(game2);

        System.out.println("In Bootstrap...");
    }

}
