package edu.uclm.esi.tecsistweb.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Board4L {

    private String id = UUID.randomUUID().toString();
    private char[][] boxes = new char[6][7];

}
