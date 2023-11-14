package edu.uclm.esi.tecsistweb.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
//@Entity
//@Table(name = "match")
public class Match {

    @Id
    @Column(length = 36)
    private String id_match = UUID.randomUUID().toString();

    //        @OneToOne
//    @JoinColumn(name = "id")¡
    private User winner;
    //    @Transient
    private List<Board> boardList = new ArrayList<>();
    //        @ManyToMany
//    @JoinTable(
//            name = "match_user",
//            joinColumns = @JoinColumn(name = "id_match"),
//            inverseJoinColumns = @JoinColumn(name = "id_user")
//    )
    private List<User> players = new ArrayList<>();
    //    @Transient
    private User currentUser;

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
