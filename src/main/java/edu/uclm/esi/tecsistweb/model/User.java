package edu.uclm.esi.tecsistweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.uclm.esi.tecsistweb.model.dto.UserMatchDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
@Entity
@Table(indexes = {
        @Index(columnList = "email", unique = true)
},
        name = "user")
public class User {

    @Id
    @Column(length = 36)
    protected String id = UUID.randomUUID().toString();
    @Column(nullable = false)
    protected String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String pwd;
    @Column(nullable = false)
    private boolean active = false;
    @Transient
    private String color;
    @Column(nullable = false)
    private String image;
    private String city;
    private String temperature;
    @ManyToMany(mappedBy = "players")
    @JsonIgnore
    private List<Match> matches = new ArrayList<>();
    @Transient
    private UserMatchDTO userMatchesInfo;
    private Integer paidMatches=0;

    public User(){
        List<String> images = new ArrayList<>();
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Dog-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Cat-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Panda-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Lion-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Pig-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Mammoth-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Crocodile-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Mouse-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Turtle-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Yak-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Rhinoceros-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Sheep-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Frog-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Dino-Tyrannosaurus-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/128/Goose-icon.png");

        this.image = images.get(new Random().nextInt(images.size()));
    }

    public void setPwd(String pwd) {
        this.pwd = DigestUtils.sha512Hex(pwd);
    }
}
