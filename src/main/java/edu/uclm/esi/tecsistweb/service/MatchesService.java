package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Board4L;
import edu.uclm.esi.tecsistweb.model.exception.IlegalMovementException;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MatchesService {

    private Map<String, Board4L> boards = new HashMap<String, Board4L>();

    public Board4L newMatch() {
        Board4L board = new Board4L();

        for (int i = 0; i < board.getBoxes().length; i++) {
            for (int j = 0; j < board.getBoxes()[i].length; j++) {
                board.getBoxes()[i][j] = '0';
            }
        }

        boards.put(board.getId(), board);
        return board;
    }

    public Board4L add(Map<String, Object> body) {


        if (body.get("id") == null || StringUtils.isBlank(body.get("id").toString())) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add an ID"));
        }
        String id = body.get("id").toString();

        Integer col = (Integer) body.get("col");
        if (col == null) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add a number for column"));
        }

        if (body.get("color") == null) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("You have to add a color"));
        }
        char color = body.get("color").toString().charAt(0);

        Board4L board = boards.get(id);
        boolean set = false;

        if (col > 5) {
            throw new IlegalMovementException();
        }


        if (board != null) {
            for (int i = board.getBoxes().length - 1; i >= 0; i--) {
                if (board.getBoxes()[i][col] == '0' && !set) {
                    board.getBoxes()[i][col] = color;
                    set = true;
                }
            }
        } else {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("There is no board with id: " + id));
        }

        if (!set) {
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("The column is alredy fill"));
        }


        return board;
    }
}
