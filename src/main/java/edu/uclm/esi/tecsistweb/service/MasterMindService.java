package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Board;
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
public class MasterMindService {

    @Getter
    @Autowired
    private WaittingRoom waittingRoom;
    @Autowired
    private UserDAO userDAO;


    public Match add(Map<String, Object> body, String id_user) {
        Match currentMatch = null;
        if (body.get("id_match") == null || StringUtils.isBlank(body.get("id_match").toString())) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add an ID of the game"));
        }
        String id_match = body.get("id_match").toString();


        if (body.get("colors") == null || StringUtils.isBlank(body.get("colors").toString())) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add color to the game"));
        }

        String _colors = body.get("colors").toString();
        String[] colors = _colors.split(",");

        if (colors.length != 6) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("Invalid number colors"));
        }

        currentMatch = waittingRoom.getCurrent_matchs().get(id_match);

        if (!currentMatch.getCurrentUser().getId().equals(id_user)) {
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("Ilegal movement, not your turn"));
        }

        if (currentMatch != null) {
            Board currentBoard = currentMatch.getBoardList().get(0);
            currentBoard.doMovement(colors);
            currentMatch.getBoardList().add(0, currentBoard);
            if (!currentBoard.checkWinner()) {
                currentMatch.passTurn();
                return currentMatch;
            } else {
                User user = this.userDAO.findById(id_user).get();
                currentMatch.setWinner(user);
            }
        } else {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("Match not found with id: " + id_match));
        }

        return currentMatch;
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
    public Match newMatch(String id_user, Class<?> type) {
        return waittingRoom.playMatch(id_user, type);
    }
}
