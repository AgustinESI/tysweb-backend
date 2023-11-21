package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;


@Service
public class UserService extends HelperService {

    @Autowired
    private UserDAO userDAO;

    public User register(String name, String email, String pwd, String pwd2) {

        if (StringUtils.isBlank(name) || name.length() < 5) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("The name is too short"));
        }

        if (pwd.length() < 5) {
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("The password is too short"));
        }

        if (!pwd.equals(pwd2)) {
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("Password does not match"));
        }

        if (!patternMatches(email, super.email_regex)) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Not valid email format"));
        }

        User response = null;
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPwd(pwd);

        try {
            response = this.userDAO.save(user);
        } catch (DataAccessException e) {
            SQLException cause = (SQLException) e.getCause().getCause();
            throw new TySWebException(HttpStatus.CONFLICT, new Exception(cause.getMessage()));
        }

        return response;
    }

    public User login(String email, String pwd) {
        User user = null;

        if (StringUtils.isBlank(pwd)) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Password cannot be empty"));
        }

        if (StringUtils.isBlank(email)) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Email cannot be empty"));
        }

        if (!patternMatches(email, super.email_regex)) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Not valid email format"));
        }

        user = this.userDAO.findByEmailAndPwd(email, pwd);
        if (user == null) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("User not found"));
        }

        return user;
    }
    public Optional<User> getUser(String user_id){
        return this.userDAO.findById(user_id); }

    public void delete(String user_id) {
        this.userDAO.deleteById(user_id);
    }
}
