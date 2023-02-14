package com.example.boardservice.model.board.controller;

import com.example.boardservice.model.board.domain.Board;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private List<Board> boardList = new ArrayList<>();

    @PostConstruct
    public void simpleBoardDataInit(){
        boardList.add(new Board("monkey","원숭이 엉덩이는 빨개"));
        boardList.add(new Board("apple","사과는 맛있어"));
        boardList.add(new Board("banana","바나나는 길어"));
        boardList.add(new Board("train","기차는 빠르다"));
    }

    @GetMapping("/all")
    public List<Board> allBoardList(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return boardList;
    }

    @GetMapping("/get/{idx}")
    public Board board(@PathVariable int idx){
        return boardList.get(idx);
    }

    @GetMapping
    public String demo(){

        return "board~~~";
    }


}

