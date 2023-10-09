package edu.uclm.esi.tecsistweb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

@Data
@Entity
@Table(indexes = {
        @Index(columnList = "email", unique = true)
})
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

    public void setPwd(String pwd) {
        this.pwd = DigestUtils.sha512Hex(pwd);
    }
}
