package edu.uclm.esi.tecsistweb.model;

import edu.uclm.esi.tecsistweb.model.exception.TySWebException;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class Battleship {


    public enum SHIP_TYPE {
        AIRCRAFTCARRIER("Aircraftcarrier", 5, 'P'),
        ARMORED("Armored", 4, 'A'),
        CRUISER("Cruiser", 3, 'C'),
        DESTROYER("Destroyer", 2, 'D'),
        SUBMARINE("Submarine", 1, 'S');

        public final String name;
        public final int size;
        public final char code;

        SHIP_TYPE(String name, int size, char code) {
            this.name = name;
            this.size = size;
            this.code = code;
        }
    }

    /**
     * Portaaviones
     */
    private List<String> aircraftCarrier = new ArrayList<String>();
    /**
     * Acorazado
     */
    private List<String> armored = new ArrayList<String>();
    /**
     * Crucero
     */
    private List<String> cruiser = new ArrayList<String>();
    /**
     * Destructor
     */
    private List<String> destroyer1 = new ArrayList<String>();
    private List<String> destroyer2 = new ArrayList<String>();
    /**
     * Submarino
     */
    private List<String> submarine1 = new ArrayList<String>();
    private List<String> submarine2 = new ArrayList<String>();


    public Board fillShip(List<String> ship, Board board, Battleship.SHIP_TYPE shipType) {

        if (isDiagonal(ship) && shipType.size > 1) {
            throw new TySWebException(HttpStatus.NOT_FOUND, new Exception("Its not possible to insert ships diagonally " + shipType.name));
        }

        try {
            for (int i = 0; i < shipType.size; i++) {
                int col = (board.getPositionLetter(ship.get(i).charAt(0))) - 1;
                int row = (ship.get(i).charAt(1) - '0');
                board.getBoard()[row][col] = shipType.code;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return board;
    }


    public static boolean isDiagonal(List<String> values) {
        for (int i = 0; i < values.size() - 1; i++) {
            String value1 = values.get(i);
            String value2 = values.get(i + 1);

            char col1 = value1.charAt(0);
            int row1 = Integer.parseInt(value1.substring(1));
            char col2 = value2.charAt(0);
            int row2 = Integer.parseInt(value2.substring(1));

            // Calculate the absolute difference between columns and rows
            int colDiff = Math.abs(col1 - col2);
            int rowDiff = Math.abs(row1 - row2);

            // Check if the difference is the same for both columns and rows
            if (colDiff != rowDiff) {
                return false;
            }
        }
        return true;
    }

}
