package edu.uclm.esi.tecsistweb.repository;

import edu.uclm.esi.tecsistweb.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDAO extends CrudRepository<User, String> {

    User findByEmailAndPwd(String email, String pwd);

}
