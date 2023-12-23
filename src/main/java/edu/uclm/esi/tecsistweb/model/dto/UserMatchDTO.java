package edu.uclm.esi.tecsistweb.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserMatchDTO {
    private int total;
    private int win;
    private int lost;
    private int draw;
    private List<GameMatchDTO> games = new ArrayList<>();

}
