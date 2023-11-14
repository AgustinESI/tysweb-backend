package edu.uclm.esi.tecsistweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String id = UUID.randomUUID().toString();
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String pwd;
    @Transient
    private String color;
    @Transient
    private String image;


    public void setPwd(String pwd) {
        this.pwd = DigestUtils.sha512Hex(pwd);
    }

    public void setImage() {
        List<String> images = new ArrayList<>();
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/256/Dog-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/256/Cat-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/256/Panda-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/256/Lion-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/256/Pig-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/256/Cocodrile-icon.png");
        images.add("https://icons.iconarchive.com/icons/iconarchive/incognito-animal-2/256/Mouse-icon.png");
        this.image = images.get(new Random().nextInt(images.size()));
    }
}
