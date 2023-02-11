package com.example.boardservice.model.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {
    @GetMapping("/board")
    public String board(){
        System.out.println("board~~~");

        return "Board~~";
    }
}

