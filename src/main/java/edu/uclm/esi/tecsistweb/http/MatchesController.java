package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.FourInLine;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.service.FourInLineService;
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
    private FourInLineService matchService;

    @GetMapping("/4line/start")
    public Match startFourInLine(HttpSession session) {
        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no user ID in session"));
        }
        String id_user = session.getAttribute("id_user").toString();
        return this.matchService.newMatch(id_user, FourInLine.class);
    }

    @PostMapping("/4line/add")
    public Object addFourInLine(HttpSession session, @RequestBody Map<String, Object> body) {

        if (StringUtils.isBlank((String) session.getAttribute("id_user"))) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no user ID in session"));
        }

        String id_user = session.getAttribute("id_user").toString();
        this.matchService.add(body, id_user);

        return this.matchService.getMatch(body.get("id_match").toString());

    }


    @GetMapping("/board/{id_match}")
    public ResponseEntity getBoard(HttpSession session, @PathVariable String id_match) {

        if (StringUtils.isBlank(id_match)) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("ID Match can not be empty"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.matchService.getMatch(id_match));
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
