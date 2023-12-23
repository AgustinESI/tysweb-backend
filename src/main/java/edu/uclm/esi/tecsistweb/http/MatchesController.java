package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.UserAnonimo;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.service.MatchesService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("matches")
@Slf4j
public class MatchesController {

    @Autowired
    private MatchesService matchesService;

    @GetMapping("/start/{game_type}")
    public Match start(HttpSession session, @PathVariable String game_type, HttpServletRequest request) {

        this.manageCookies(request, session);
        User user;

        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            user = new UserAnonimo();
            session.setAttribute("id_user", user.getId());
//            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("There is no user ID in session"));
        } else {
            user = this.matchesService.findUserById(session.getAttribute("id_user").toString());
        }

        return this.matchesService.start(user, game_type);
    }


    @PostMapping("/add")
    public Match add(HttpSession session, @RequestBody Map<String, Object> body, HttpServletRequest request) {
        this.manageCookies(request, session);
        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no user ID in session"));
        }

        String id_user = session.getAttribute("id_user").toString();
        this.matchesService.add(body, id_user);

        return this.matchesService.getMatch(body.get("id_match").toString());
    }

    @GetMapping("/board/{id_match}")
    public ResponseEntity getBoard(HttpSession session, @PathVariable String id_match) {

        if (StringUtils.isBlank(id_match)) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("ID Match can not be empty"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.matchesService.getMatch(id_match));
    }

    private void manageCookies(HttpServletRequest request, HttpSession session) {

        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {

            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                String cookieValue = cookie.getValue();
                if (cookieName.equals("id_user")) {
                    session.setAttribute("id_user", cookieValue);
                    break;
                }
            }
        }
    }


}
