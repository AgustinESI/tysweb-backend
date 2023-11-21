package edu.uclm.esi.tecsistweb.model;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class Board {

    private String id_board = UUID.randomUUID().toString();
    private char[][] board;


    public boolean checkWinner() {
        return this.checkWinner();
    }

    public void doMovement(Object obj) {
        this.doMovement(obj);
    }

    public boolean add(Match match, String combination) {
        return this.add(match, combination);
    }
}
