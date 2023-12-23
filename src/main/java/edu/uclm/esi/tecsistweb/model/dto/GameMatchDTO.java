package edu.uclm.esi.tecsistweb.model.dto;

import lombok.Data;

@Data
public class GameMatchDTO {
    public GameMatchDTO(String gametype, int value) {
        this.gametype = gametype;
        this.value = value;
    }

    private String gametype;
    private int value;
}