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


public class UserAnonimo extends User{

    public UserAnonimo() {
        super();
        super.id = UUID.randomUUID().toString();;
        super.name = "Invitado "+new Random().nextInt(1000);
    }

}
