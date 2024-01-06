package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.dto.UserMatchDTO;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.service.EmailSenderService;
import edu.uclm.esi.tecsistweb.service.HelperService;
import edu.uclm.esi.tecsistweb.service.UserService;
import edu.uclm.esi.tecsistweb.service.WeatherService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired()
    private UserService userService;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private EmailSenderService emailSenderService;

    @Value("${email.send.register}")
    private String sendEmailRegister;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(HttpSession session, @RequestBody Map<String, String> body) {

        String name = body.get("name");
        String email = body.get("email");
        String pwd = body.get("pwd1");
        String pwd2 = body.get("pwd2");


        String lat = body.get("lat");
        String lon = body.get("lon");

        User user = this.userService.register(name, email, pwd, pwd2);

        if (StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)) {
            weatherService.getTemperature(user, lat, lon);
        }

        if (Boolean.parseBoolean(sendEmailRegister)) {
            emailSenderService.sendEmailRegistration(user, HelperService.EMAIL_PATH.EMAIL_PATH_REGISTER_TEMPLATE.path());
        } else {
            user = this.userService.activateAccount(user);
        }

        session.setAttribute("id_user", user.getId());

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping(value = "/register/activate/{id}")
    public ResponseEntity<User> getActivationAccount(@PathVariable String id) {
        if (StringUtils.isNotBlank(id)) {
            Optional<User> optUser = userService.getUser(id);
            if (optUser.isPresent())
                return new ResponseEntity<>(this.userService.activateAccount(optUser.get()), HttpStatus.OK);
            else
                throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("User not found"));
        } else {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("ID user cannot be empty"));
        }
    }

    @GetMapping(value = "/matches/info/{id}")
    public ResponseEntity<UserMatchDTO> getUserMatchesInfo(@PathVariable String id) {
        if (StringUtils.isNotBlank(id)) {
            Optional<User> optUser = userService.getUser(id);
            if (optUser.isPresent())
                return new ResponseEntity<>(this.userService.getUserMatchesInfo(id), HttpStatus.OK);
            else
                throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("User not found"));
        } else {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("ID user cannot be empty"));
        }
    }

    @GetMapping("/{id}")
    public User getUserDetails(@PathVariable String id) {
        if (StringUtils.isNotBlank(id)) {
            Optional<User> optUser = userService.getUser(id);
            if (optUser.isPresent()) {
                return optUser.get();
            } else
                throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("User not found"));
        } else {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("ID user cannot be empty"));
        }
    }

    @GetMapping("name/{name}")
    public User getUserByName(@PathVariable String name) {
        if (StringUtils.isNotBlank(name)) {
            return userService.findByString(name);
        } else {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("ID user cannot be empty"));
        }
    }

    @PutMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User login(HttpSession session, @RequestBody Map<String, String> body) {

        if (body.get("email") == null || StringUtils.isBlank(body.get("email").toString())) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Email cannot be empty"));
        }

        if (body.get("pwd1") == null || StringUtils.isBlank(body.get("pwd1").toString())) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Password cannot be empty"));
        }

        String email = body.get("email");
        String lat = body.get("lat");
        String lon = body.get("lon");
        String pwd = DigestUtils.sha512Hex(body.get("pwd1").toString());

        User user = this.userService.login(email, pwd);

        if (StringUtils.isNotBlank(lat) && StringUtils.isNotBlank(lon)) {
            weatherService.getTemperature(user, lat, lon);
            this.userService.saveUser(user);
        }
        session.setAttribute("id_user", user.getId());

        return user;
    }


    @GetMapping("/request-delete/{email}")
    public String requestDelete(HttpSession session, @PathVariable String email, @RequestParam String pwd) {

        User user = this.userService.login(email, DigestUtils.sha512Hex(pwd));

        session.setAttribute("user_id", user.getId());

        return "Are you sure you want to delete your account?";
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(HttpSession session, @RequestParam boolean response) {

        if (response) {
            System.out.println("[INFO] user_id" + session.getAttribute("id_user"));
            String user_id = session.getAttribute("id_user").toString();
            this.userService.delete(user_id);
            session.invalidate();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("User deleted");
        } else {
            session.removeAttribute("id_user");
            return ResponseEntity.status(HttpStatus.OK).body("User not deleted");
        }
    }


}
