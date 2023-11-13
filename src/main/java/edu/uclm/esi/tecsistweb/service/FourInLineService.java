package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Board;
import edu.uclm.esi.tecsistweb.model.FourInLine;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FourInLineService extends HelperService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    @Getter
    private WaittingRoom waittingRoom;

    private char winner = '0';


    public Match newMatch(String id_user, Class<?> type) {
        return waittingRoom.playMatch(id_user, type);
    }


    public Boolean add(Map<String, Object> body, String id_user) {
        boolean out = false;

        if (body.get("id_match") == null || StringUtils.isBlank(body.get("id_match").toString())) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add an ID of the game"));
        }
        String id_match = body.get("id_match").toString();

        if (body.get("id_board") == null || StringUtils.isBlank(body.get("id_board").toString())) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add an ID of the board"));
        }
        String id_board = body.get("id_board").toString();

        Integer col = (Integer) body.get("col");
        if (col == null) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add a number for column"));
        }

        if (body.get("color") == null) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add a color"));
        }
        char color = body.get("color").toString().charAt(0);


        Match match = this.waittingRoom.getCurrent_matchs().get(id_match);
        Board board = null;
        boolean set = false;

        if (match != null) {

            if (!match.getCurrentUser().getId().equals(id_user)) {
                throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("Ilegal movement, not your turn"));
            }

            List<Board> list = match.getBoardList()
                    .stream()
                    .filter(_board -> _board.getId_board().equals(id_board))
                    .collect(Collectors.toList());

            if (!list.isEmpty()) {

                board = list.get(0);

                for (int i = board.getBoard().length - 1; i >= 0; i--) {
                    if (board.getBoard()[i][col] == '0' && !set) {
                        board.getBoard()[i][col] = color;
                        set = true;
                        match.passTurn();
                    }
                }
            } else {
                throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no board with id: " + id_board));
            }
        } else {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no match with id: " + id_match));
        }

        if (!set) {
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("The column is alredy fill"));
        }

        out = board.checkWinner();
        if (out) {
            User winner = userDAO.findById(id_user).get();
            match.setWinner(winner);
        }
        return out;
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

    public Boolean requestTurn(String idMatch, String idUser) {
        if (this.waittingRoom.getCurrent_matchs().get(idMatch) != null) {
            return true;
        }
        return false;
    }
}