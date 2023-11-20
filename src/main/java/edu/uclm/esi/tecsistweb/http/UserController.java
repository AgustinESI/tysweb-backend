package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired(required = true)
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
    @GetMapping("/{id}")
    public User getUserDetails(@PathVariable String id) {
        Optional<User> optUser = userService.getUser(id);
        return optUser.get();
    }
    @GetMapping("/{user_id}/image")
    public ResponseEntity<byte[]> getUserImage(@PathVariable("user_id") String user_id) throws IOException {
        Optional<User> userOptional = userService.getUser(user_id);

        String projectPath = System.getProperty("user.dir");
        String imagePath = userOptional.get().getImage();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + imagePath;


        if (absoluteImagePath != null) {
            try {
                File imageFile = new File(absoluteImagePath);

                // Verificar si el archivo existe
                if (imageFile.exists() && imageFile.isFile()) {
                    FileInputStream fileInputStream = new FileInputStream(imageFile);
                    byte[] imageBytes = fileInputStream.readAllBytes();
                    fileInputStream.close();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.IMAGE_PNG);

                    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/login")
    public User login(HttpSession session, @RequestBody Map<String, String> body) {

        if (body.get("email") == null || StringUtils.isBlank(body.get("email").toString())) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Email cannot be empty"));
        }

        if (body.get("pwd1") == null || StringUtils.isBlank(body.get("pwd1").toString())) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Password cannot be empty"));
        }

        String email = body.get("email");
        String pwd = DigestUtils.sha512Hex(body.get("pwd1").toString());

        User user = this.userService.login(email, pwd);

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
