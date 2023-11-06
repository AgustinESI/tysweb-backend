package edu.uclm.esi.tecsistweb.model;

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
    public Board doMovement() {
        return null;
    }

}
