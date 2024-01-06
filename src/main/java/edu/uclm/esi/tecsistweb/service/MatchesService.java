package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.MatchDAO;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MatchesService extends HelperService {

    @Getter
    @Autowired
    private WaittingRoom waittingRoom;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private MatchDAO matchDAO;


    public Match start(User user, String game_type) {
        return waittingRoom.start(user, game_type);
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

            Boolean isWinner = match.getBoardList().get(0).add(match, combination);

            if (isWinner == null) {
                super.getTimestamp(match);
                this.matchDAO.save(match);
                User draw = new User();
                draw.setName("DRAW GAME");
                match.setWinner(draw);
            } else if (isWinner) {

                if (userDAO.findById(id_user).isPresent()) {
                    User winner = userDAO.findById(id_user).get();
                    match.setWinner(winner);
                }

                super.getTimestamp(match);
                this.matchDAO.save(match);
            }

        } else {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no match with id: " + id_match));
        }
    }

    public User findUserById(String idUser) {
        return this.userDAO.findById(idUser).get();
    }

    public Match end(Map<String, Object> body) {

        String id_match = body.get("id_match").toString();
        String id_user = body.get("id_user").toString();

        Match match = this.waittingRoom.getCurrent_matchs().get(id_match);
        if (match != null) {
            User user = this.findUserById(id_user);
            match.setWinner(user);
            this.matchDAO.save(match);
            return match;
        } else {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no match with id: " + id_match));
        }

    }
}
