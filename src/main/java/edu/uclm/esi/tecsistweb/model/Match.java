package edu.uclm.esi.tecsistweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
@Entity
@Table(name = "game_match")
public class Match {

    @Id
    @Column(length = 36)
    private String id_match = UUID.randomUUID().toString();

    @Transient
    private List<Board> boardList = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "match_players",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private List<User> players = new ArrayList<>();
    @Transient
    private User currentUser;
    @ManyToOne
    private User winner;
    @Column
    @JsonIgnore
    private String gameType;
    @Column(name = "timestamp")
    @JsonIgnore
    private String timestamp;


    public void addUser(User user) {
        players.add(user);
    }

    public void start() {
        this.currentUser = this.players.get(new Random().nextInt(this.players.size()));
    }

    public void passTurn() {
        for (User user : players) {
            if (!user.getId().equals(this.currentUser.getId())) {
                this.currentUser = user;
                break;
            }
        }
    }
}