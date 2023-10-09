package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.service.HelperService;
import edu.uclm.esi.tecsistweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired(required=true)
    private UserService userService;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(@RequestBody Map<String, String> body) {

        String name = body.get("name");
        String email = body.get("email");
        String pwd = body.get("pwd1");
        String pwd2 = body.get("pwd2");

        User user = this.userService.register(name, email, pwd, pwd2);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/login")
    public User login(@RequestBody Map<String, String> body) {

        if (body.get("email") == null || StringUtils.isBlank(body.get("email").toString())) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Email cannot be empty"));
        }

        if (body.get("pwd") == null || StringUtils.isBlank(body.get("pwd").toString())) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Password cannot be empty"));
        }

        String email = body.get("email");
        String pwd = DigestUtils.sha512Hex(body.get("pwd").toString());

        return this.userService.login(email, pwd);
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
            String user_id = session.getAttribute("user_id").toString();
            this.userService.delete(user_id);
            session.invalidate();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("User deleted");
        } else {
            session.removeAttribute("user_id");
            return ResponseEntity.status(HttpStatus.OK).body("User not deleted");
        }
    }


}
