package edu.uclm.esi.tecsistweb.model;

import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class FourInLine extends Board implements Game {

    public FourInLine() {

        super.setBoard(new char[6][7]);
        for (int i = 0; i < super.getBoard().length; i++) {
            Arrays.fill(super.getBoard()[i], '0');
        }
    }

    public boolean checkWinnerDirection(int row, int col, int df, int dc) {
        char jugador = this.getBoard()[row][col];
        for (int i = 0; i < 4; i++) {
            int newrow = row + i * df;
            int newcol = col + i * dc;

            // Verificar si la posición está dentro del this.getBoxes()
            if (newrow < 0 || newrow >= this.getBoard().length || newcol < 0 || newcol >= this.getBoard()[0].length) {
                return false;
            }

            // Verificar si el char en la posición actual coincide con el jugador actual
            if (this.getBoard()[newrow][newcol] != jugador) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkWinner() {
        boolean out = false;
        // Comprobar horizontalmente
        for (int i = 0; i < this.getBoard().length; i++) {
            for (int j = 0; j <= this.getBoard()[0].length - 4; j++) {
                if (this.getBoard()[i][j] != '0' && checkWinnerDirection(i, j, 0, 1)) {
                    out = true;
                }
            }
        }

        // Comprobar verticalmente
        for (int i = 0; i <= this.getBoard().length - 4; i++) {
            for (int j = 0; j < this.getBoard()[0].length; j++) {
                if (this.getBoard()[i][j] != '0' && checkWinnerDirection(i, j, 1, 0)) {
                    out = true;
                }
            }
        }

        // Comprobar diagonalmente (\)
        for (int i = 0; i <= this.getBoard().length - 4; i++) {
            for (int j = 0; j <= this.getBoard()[0].length - 4; j++) {
                if (this.getBoard()[i][j] != '0' && checkWinnerDirection(i, j, 1, 1)) {
                    out = true;
                }
            }
        }

        // Comprobar diagonalmente (/)
        for (int i = 0; i <= this.getBoard().length - 4; i++) {
            for (int j = 3; j < this.getBoard()[0].length; j++) {
                if (this.getBoard()[i][j] != '0' && checkWinnerDirection(i, j, 1, -1)) {
                    out = true;
                }
            }
        }

        return out;
    }


    @Override
    public Boolean add(Match match, String combination) {
        Board board = null;
        boolean set = false;
        boolean out = false;

        int col = Integer.parseInt(combination);

        board = match.getBoardList().get(0);

        for (int i = board.getBoard().length - 1; i >= 0; i--) {
            if (board.getBoard()[i][col] == '0' && !set) {
                board.getBoard()[i][col] = match.getCurrentUser().getColor().charAt(0);
                set = true;
                match.passTurn();
            }
        }

        if (!set) {
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("The column is already fill"));
        }

        out = board.checkWinner();
        boolean fill = true;
        if (!out) {
            for (int i = 0; i < board.getBoard().length; i++) {
                for (int j = 0; j < board.getBoard()[i].length; j++) {
                    if (board.getBoard()[i][j] == '0') {
                        fill = false;
                        break;
                    }
                }
            }
            if (fill) {
                return null;
            } else {
                out = fill;
            }
        }

        return out;
    }


}
