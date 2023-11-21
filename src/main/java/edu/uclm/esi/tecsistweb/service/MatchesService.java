package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MatchesService {

    @Getter
    @Autowired
    private WaittingRoom waittingRoom;
    @Autowired
    private UserDAO userDAO;


    public Match start(String id_user, String game_type) {
        return waittingRoom.start(id_user, game_type);
    }

    public Match getMatch(String id_match) {

        if (StringUtils.isBlank(id_match)) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Empty ID Match"));
        }

        Match match = this.waittingRoom.getCurrent_matchs().get(id_match);

        if (match == null) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no match with id: " + id_match));
        }
        return match;
    }


    public void add(Map<String, Object> body, String id_user) {
        boolean out = false;

        if (body.get("id_match") == null || StringUtils.isBlank(body.get("id_match").toString())) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add an ID of the game"));
        }
        String id_match = body.get("id_match").toString();

        if (body.get("combination") == null || StringUtils.isBlank(body.get("combination").toString())) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add combination of the game"));
        }
        String combination = body.get("combination").toString();

        Match match = this.waittingRoom.getCurrent_matchs().get(id_match);

        if (match != null) {

            if (match.getWinner() != null) {
                throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("There is already a winner"));
            }

            if (!match.getCurrentUser().getId().equals(id_user)) {
                throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("Ilegal movement, not your turn"));
            }

            boolean isWinner = match.getBoardList().get(0).add(match, combination);

            if (isWinner) {
                User winner = userDAO.findById(id_user).get();
                match.setWinner(winner);
            }

        } else {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no match with id: " + id_match));
        }


    }

}
