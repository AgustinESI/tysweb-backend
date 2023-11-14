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

        if (!optUser.isPresent()) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("User not found"));
        }

        Match out = new Match();

        if (this.pending_matchs.isEmpty()) {
            if (type == FourInLine.class) {
                FourInLine board = new FourInLine();
                out.getBoardList().add(board);
            }

            //TODO: add the other game
            User user = optUser.get();
            user.setColor("R");
            user.setImage();
            out.addUser(user);
            this.getPending_matchs().add(out);

        } else {
            for (int i = 0; i <= this.pending_matchs.size(); i++) {
                Match _match = this.pending_matchs.get(i);
                if (_match.getBoardList() !=null && !_match.getBoardList().isEmpty()
                        && _match.getBoardList().get(0).getClass().getName().equalsIgnoreCase(type.getName())) {

                    // Para no introducir dos veces el mismo usuario, al recargar la pantalla.
                    for (User user: _match.getPlayers()){
                        if (user.getId().equals(id_user)){
                            return _match;
                        }
                    }

                    out = this.pending_matchs.remove(i);
                    User user = optUser.get();
                    user.setColor("Y");
                    out.addUser(user);
                    out.start();
                    this.current_matchs.put(out.getId_match(), out);
                }
            }
        }
        return out;
    }

}
