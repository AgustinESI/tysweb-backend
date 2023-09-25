package edu.uclm.esi.tecsistweb.http;

import edu.uclm.esi.tecsistweb.model.Board4L;
import edu.uclm.esi.tecsistweb.service.MatchesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("matches")
public class MatchesController {

    @Autowired
    private MatchesService matchService;

    @GetMapping("/start")
    public Board4L start() {
        return this.matchService.newMatch();
    }

    @PostMapping("/add")
    public Board4L add(@RequestBody Map<String, Object> body) {
        return this.matchService.add(body);

    }

}
