package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.Battleship;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.service.MatchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("matches")
public class MatchesController {

    @Autowired
    private MatchesService matchService;

    @GetMapping("/4line/start")
    public Match start4InALine() {
        return this.matchService.newMatch(1, 6, 7);
    }

    @PostMapping("/4line/add")
    public Object add4InALine(@RequestBody Map<String, Object> body) {

        if (this.matchService.add(body)) {
            return "The end of the game";
        } else {
            return this.matchService.getMatch(body.get("id_match").toString());
        }

    }

    @GetMapping("/battleship/start")
    public Match startBattleship() {
        return this.matchService.newMatch(2, 10, 10);
    }

    @PostMapping("/battleship/add")
    public Match addBattleship(@RequestBody Map<String, Object> body) {
        Battleship battleship = new Battleship();
        battleship.setAircraftCarrier((List<String>) body.get("aircraftCarrier"));
        battleship.setArmored((List<String>) body.get("armored"));
        battleship.setCruiser((List<String>) body.get("cruiser"));
        battleship.setDestroyer1((List<String>) body.get("destroyer1"));
        battleship.setDestroyer2((List<String>) body.get("destroyer2"));
        battleship.setSubmarine1((List<String>) body.get("submarine1"));
        battleship.setSubmarine2((List<String>) body.get("submarine2"));
        return this.matchService.addBattleShip(battleship, body.get("id_match").toString(), body.get("id_board").toString());
    }


}
