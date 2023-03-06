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

}
