package edu.uclm.esi.tecsistweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
public class Match {


    private String id_match = UUID.randomUUID().toString();
    private String winner;
    @JsonIgnore
    private boolean end = false;
    private List<Board> boardList = new ArrayList<>();
    private List<User> players = new ArrayList<>();
    @JsonIgnore
    private User currentUser;

    public void addUser(User user) {
        players.add(user);
    }

    public void start() {
        this.currentUser = this.players.get(new Random().nextInt(this.players.size()));
    }

    public void getTurn(){
        for (User user: players){
            if (!user.getId().equals(this.currentUser.getId())){
                this.currentUser = user;
            }
        }
    }

}
