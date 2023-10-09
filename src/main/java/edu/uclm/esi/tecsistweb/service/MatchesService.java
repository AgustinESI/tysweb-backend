package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Battleship;
import edu.uclm.esi.tecsistweb.model.Board;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MatchesService extends HelperService {

    private char winner = '0';

    private Map<String, Match> matchs = new HashMap<String, Match>();

    public Match newMatch(int numberBoards, int col, int row) {
        Match match = new Match();

        for (int k = 0; k < numberBoards; k++) {
            Board board = new Board(col, row);

            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard()[i].length; j++) {
                    board.getBoard()[i][j] = '0';
                }
            }
            match.getBoardList().add(board);
        }
        matchs.put(match.getId_match(), match);

        return match;
    }


    public Boolean add(Map<String, Object> body) {
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


        Match match = matchs.get(id_match);
        Board board = null;
        boolean set = false;

        if (match != null) {

            if (match.getLastColor() == color) {
                throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("Ilegal movement, you only can do 1 movement"));
            }

            List<Board> list = match.getBoardList()
                    .stream()
                    .filter(_board -> _board.getId_board().equals(id_board))
                    .collect(Collectors.toList());

            if (list != null && !list.isEmpty()) {

                board = list.get(0);

                for (int i = board.getBoard().length - 1; i >= 0; i--) {
                    if (board.getBoard()[i][col] == '0' && !set) {
                        board.getBoard()[i][col] = color;
                        set = true;
                        match.setLastColor(color);
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

        out = board.comprobarGanador();
        if (out) {
            match.setEnd(Boolean.TRUE);
        }
        return out;
    }


    public MatchesService() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Match addBattleShip(Battleship battleship, String id_match, String id_board) {


        Match match = matchs.get(id_match);

        if (match != null) {
            List<Board> list = match.getBoardList()
                    .stream()
                    .filter(_board -> _board.getId_board().equals(id_board))
                    .collect(Collectors.toList());
            if (list != null && !list.isEmpty()) {

                Board board = list.get(0);
                board = battleship.fillShip(battleship.getAircraftCarrier(), board, Battleship.SHIP_TYPE.AIRCRAFTCARRIER);
                board = battleship.fillShip(battleship.getArmored(), board, Battleship.SHIP_TYPE.ARMORED);
                board = battleship.fillShip(battleship.getCruiser(), board, Battleship.SHIP_TYPE.CRUISER);
                board = battleship.fillShip(battleship.getDestroyer1(), board, Battleship.SHIP_TYPE.DESTROYER);
                board = battleship.fillShip(battleship.getDestroyer2(), board, Battleship.SHIP_TYPE.DESTROYER);
                board = battleship.fillShip(battleship.getSubmarine1(), board, Battleship.SHIP_TYPE.SUBMARINE);
                board = battleship.fillShip(battleship.getSubmarine2(), board, Battleship.SHIP_TYPE.SUBMARINE);

            } else {
                throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no board with id: " + id_board));
            }
        } else {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no match with id: " + id_match));
        }

        return match;
    }

    public Match getMatch(String id_match) {

        if (StringUtils.isBlank(id_match)) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Empty ID Match"));
        }

        Match match = matchs.get(id_match);

        if (match == null) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no match with id: " + id_match));
        }
        return match;
    }
}
