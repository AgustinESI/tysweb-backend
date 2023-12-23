package edu.uclm.esi.tecsistweb.service;

import edu.uclm.esi.tecsistweb.model.Board;
import edu.uclm.esi.tecsistweb.model.Match;
import edu.uclm.esi.tecsistweb.model.User;
import edu.uclm.esi.tecsistweb.model.dto.GameMatchDTO;
import edu.uclm.esi.tecsistweb.model.dto.UserMatchDTO;
import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import edu.uclm.esi.tecsistweb.repository.UserDAO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
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


    public Match start(User user, String game_type) {



        Match out = new Match();
        getImage(user);
        user.setUserMatchesInfo(this.getUserMatchesInfo(user.getId()));
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
                        if (_user.getId().equals(user.getId())) {
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
                out.setGameType(game_type);
                this.getPending_matchs().add(out);
            }
        } catch (Exception e) {
            throw new TySWebException(HttpStatus.BAD_REQUEST, new Exception("Error creating game of type " + game_type));
        }

        return out;
    }

    private void getImage(User user) {
        try {
            String projectPath = System.getProperty("user.dir");
            String imagePath = user.getImage();
            String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + imagePath;
            byte[] imageBytes = Files.readAllBytes(Path.of(absoluteImagePath));

            String[] fileParts = imagePath.split("\\."); // Dividir por el punto
            String imageExtension = fileParts[fileParts.length - 1].toLowerCase();
            String base64Image = "data:image/" + imageExtension + ";base64," + Base64.getEncoder().encodeToString(imageBytes);

            user.setImage(base64Image);

        } catch (Exception e) {
            throw new TySWebException(HttpStatus.INTERNAL_SERVER_ERROR, new Exception("Cannot process the user image"));
        }
    }

    public UserMatchDTO getUserMatchesInfo(String id_user) {

        UserMatchDTO out = new UserMatchDTO();
        Optional<User> optUser = this.userDAO.findById(id_user);

        if (optUser.isPresent()) {
            try {
                User user = optUser.get();

                out.setTotal(user.getMatches().size());
                out.setGames(new ArrayList<>());
                Map<String, Integer> map = new HashMap<>();

                int win = 0;
                int draw = 0;
                for (Match match : user.getMatches()) {
                    if (match.getWinner() == null){
                        draw++;
                    }
                    if (match.getWinner()!=null && match.getWinner().getId().equals(id_user)) {
                        win++;
                    }
                    if (map.get(match.getGameType()) == null) {
                        map.put(match.getGameType(), 1);
                    } else {
                        map.put(match.getGameType(), map.get(match.getGameType()) + 1);
                    }
                }

                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    out.getGames().add(new GameMatchDTO(entry.getKey(), entry.getValue()));
                }

                out.setWin(win);
                out.setDraw(draw);
                out.setLost(out.getTotal() - out.getWin()- out.getDraw());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return out;
    }

}
