package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.FourInLine;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WaittingRoom {

    @Autowired
    private UserDAO userDAO;


    @Setter
    @Getter
    private Map<String, Match> current_matchs = new HashMap<String, Match>();
    @Setter
    @Getter
    private List<Match> pending_matchs = new ArrayList<>();


    public Match playMatch(String id_user, Class<?> type) {
        Optional<User> optUser = this.userDAO.findById(id_user);

        Match out = new Match();
        int numberBoards = 0;

        if (!optUser.isPresent()) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("User not found"));
        }

        if (this.pending_matchs.isEmpty()) {
            if (type == FourInLine.class) {
                numberBoards = 1;

                for (int k = 0; k < numberBoards; k++) {
                    FourInLine board = new FourInLine();
                    out.getBoardList().add(board);
                }
            }

            //TODO: add the other game

            this.getPending_matchs().add(out);

        } else {
            for (int i = 0; i <= this.pending_matchs.size(); i++) {
                Match _match = this.pending_matchs.get(i);
                if (_match.getBoardList() !=null && !_match.getBoardList().isEmpty() && _match.getBoardList().get(0).getClass().getName().equalsIgnoreCase(type.getName())) {
                    out = this.pending_matchs.remove(i);
                    out.start();
                    this.current_matchs.put(out.getId_match(), out);
                }
            }
        }

        out.addUser(optUser.get());
        return out;
    }

}
