package edu.uclm.esi.tecsistweb.model;

import java.util.Map;

public interface Game {

    boolean checkWinner();
    void doMovement(Object obj);

    boolean add(Match match, String combination);

}
