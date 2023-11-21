package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Board;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.util.*;

@Service
public class WaittingRoom {

    private static final String PATH_MODEL = "edu.uclm.esi.tecsistweb.model.";

    @Autowired
    private UserDAO userDAO;
    @Setter
    @Getter
    private Map<String, Match> current_matchs = new HashMap<String, Match>();
    @Setter
    @Getter
    private List<Match> pending_matchs = new ArrayList<>();


    public Match start(String id_user, String game_type) {
        Optional<User> optUser = this.userDAO.findById(id_user);

        if (!optUser.isPresent()) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("User not found"));
        }

        Match out = new Match();
        User user = optUser.get();
        boolean set = false;

        if (this.pending_matchs.isEmpty()) {
            out = this.createNewMatch(out, user, game_type);
            set = true;
        } else {
            for (int i = 0; i < this.pending_matchs.size(); i++) {
                Match _match = this.pending_matchs.get(i);
                if (_match.getBoardList() != null && !_match.getBoardList().isEmpty()
                        && _match.getBoardList().get(0).getClass().getName().equalsIgnoreCase(PATH_MODEL + game_type)) {

                    // Para no introducir dos veces el mismo usuario, al recargar la pantalla.
                    for (User _user : _match.getPlayers()) {
                        if (_user.getId().equals(id_user)) {
                            return _match;
                        }
                    }
                    out = this.pending_matchs.remove(i);
                    user.setColor("Y");
                    out.addUser(user);
                    out.start();
                    this.current_matchs.put(out.getId_match(), out);
                    set = true;
                }
            }
        }


        if (!set) {
            out = this.createNewMatch(out, user, game_type);
        }

        return out;
    }

    private Match createNewMatch(Match out, User user, String game_type) {

        Class<?> classz;
        Constructor<?> constructor;
        Board board;

        try {
            classz = Class.forName("edu.uclm.esi.tecsistweb.model." + game_type);
        } catch (Exception e) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("Game " + game_type + " not available"));
        }

        try {
            constructor = classz.getConstructor();
        } catch (Exception e) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Error creating game of type " + game_type));
        }

        try {
            board = (Board) constructor.newInstance((Object[]) null);
            if (board != null) {
                out.getBoardList().add(board);
                user.setColor("R");
                out.addUser(user);
                this.getPending_matchs().add(out);
            }
        } catch (Exception e) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Error creating game of type " + game_type));
        }

        return out;
    }

}
