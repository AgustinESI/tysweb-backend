package edu.uclm.esi.tecsistweb.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Random;
@Data
public class MasterMind extends Board implements Game{

    @Getter@Setter
    private String[] colors;
    public enum Color {
        BLACK('K'),
        BLUE('B'),
        BROWN('M'),
        GREEN('G'),
        ORANGE('O'),
        RED('R'),
        PURPLE('P'),
        YELLOW('Y');
        private final char code;

        Color(char code) {
            this.code = code;
        }
    }

    public MasterMind(){

        Color[] colors = Color.values();

        super.setBoard(new char[13][12]);
        for (int i = 0; i < super.getBoard().length; i++) {
            for (int j = 0; j < super.getBoard()[i].length; j++) {
                if (i == 0 && j < 6) {
                    super.getBoard()[i][j] = colors[new Random().nextInt(colors.length)].code;
                }else
                    super.getBoard()[i][j] = '-';
            }
        }
    }

    @Override
    public void doMovement(Object obj) {
        this.colors = (String[]) obj;
        int position;
        for (position = 0; position < super.getBoard().length; position++){
            if (super.getBoard()[position][0] == '-')
                break;
        }
        for (int i = 0 ; i < this.colors.length; i++){
            super.getBoard()[position][i] = this.colors[i].charAt(0);
        }

        char[] solAux = Arrays.copyOf(super.getBoard()[0], super.getBoard()[0].length / 2);
        char[] combAux = Arrays.copyOf(super.getBoard()[position], super.getBoard()[0].length / 2);
        // Comprobación por si están en la misma posición
        for (int j = 0; j < super.getBoard()[0].length / 2; j++) {
            if (solAux[j] == combAux[j]) {
                super.getBoard()[position][j + 6] = 'b';
                solAux[j] = 'X';
                combAux[j]= 'x';
            }
        }
        // Comprobación por si el color está pero en distinta posición
        for (int i = 0; i < solAux.length; i++) {
            if (solAux[i] == 'X')
                continue;
            for (int j = 0; j < combAux.length; j++) {
                if (combAux[j] == 'x')
                    continue;
                if (solAux[i] == combAux[j] && j != i) {
                    super.getBoard()[position][j+6] = 'w';
                    solAux[i] = 'X';
                    combAux[j]= 'x';
                    break;
                }
            }
        }
    }
    @Override
    public boolean checkWinner() {
        int cont = 0;
        int position;
        for (position = 0; position < super.getBoard().length; position++){
            if (super.getBoard()[position][0] == '-')
                break;
        }

        for (int i = 6 ; i < super.getBoard().length-1; i++){
            if (super.getBoard()[position-1][i] == 'b'){
                cont++;
            }
        }
        return cont == 6;
    }
}
