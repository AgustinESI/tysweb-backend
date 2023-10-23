package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.service.MatchesService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("matches")
public class MatchesController {

    @Autowired
    private MatchesService matchService;

    @GetMapping("/4line/start")
    public Match start4InALine(HttpSession session) {

        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no user ID in session"));
        }

        String id_user = session.getAttribute("id_user").toString();

        return this.matchService.newMatch(id_user, 1, 6, 7);
    }

    @PostMapping("/4line/add")
    public Object add4InALine(HttpSession session, @RequestBody Map<String, Object> body) {

        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no user ID in session"));
        }

        String id_user = session.getAttribute("id_user").toString();
        if (this.matchService.add(body, id_user)) {
            return "The end of the game";
        } else {
            return this.matchService.getMatch(body.get("id_match").toString());
        }
    }

    @GetMapping("/request-turn/{id_match}")
    public Boolean turn(HttpSession session, @PathVariable String id_match) {

        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no user ID in session"));
        }

        String id_user = session.getAttribute("id_user").toString();


        return this.matchService.requestTurn(id_match, id_user);
    }


}
