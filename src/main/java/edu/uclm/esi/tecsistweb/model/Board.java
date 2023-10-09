package edu.uclm.esi.tecsistweb.model;

import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Data
public class Board {

    private String id_board = UUID.randomUUID().toString();
    private char[][] board;

    public Board(int col, int row) {
        board = new char[col][row];
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

    public boolean comprobarGanador() {
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

    public Integer getPositionLetter(char letter) {

        Integer position = null;
        if (letter >= 'A' && letter <= 'Z') {
            // Subtract 'A' (the first letter in the alphabet) and add 1 to get the position
            position = letter - 'A' + 1;
//            System.out.println("The letter '" + letter + "' is at position " + position + " in the alphabet.");
        } else {
            throw new TySWebException(HttpStatus.FORBIDDEN, new Exception("Invalid input. Please enter an uppercase letter."));
        }
        return position;
    }
}
