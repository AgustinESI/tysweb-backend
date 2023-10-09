package edu.uclm.esi.tecsistweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Match {


    private String id_match = UUID.randomUUID().toString();
    private String winner;
    @JsonIgnore
    private boolean end = false;
    private List<Board> boardList = new ArrayList<>();
    private char lastColor = '0';
}
