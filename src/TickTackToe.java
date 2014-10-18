import java.util.*;

/**
 *
 * Simple TicTacToe for 2 players.
 *
 * Created on 17.10.14
 *
 * This is my second Java app. 5th day of learning Java.
 * @author Andrew Baliushin
 * @site http://andrewbaliushin.blogspot.ru/
 *
 */
public class TickTackToe {



    public static void main(String[] args) {

        Game game = new Game();
        Board board = new Board();
        Player player1 = new Player();
        Player player2 = new Player();

        //Track whose turn it is by storing ref to player.
        Player activePlayer;

        player1.name = "John";
        player1.mark = "X";
        player2.name = "Anna";
        player2.mark = "O";

        //decide who will be first
        activePlayer = game.chooseWhoStarts(player1, player2);

        //show empty board
        Render.draw(board);

        //while there is space on board and no one get the winning combination
        while(board.counter <9 && !game.isGameOver(board)) {

            //Ask for input from player by coordinates
            int[] markCoords;
            try {
                markCoords = InputControll.interactWithPlayer(activePlayer);
            } catch (InputMismatchException e) {
                System.out.println("Use only integers to place your marks.");
                continue;
            }

            //make move
            try {
                game.makeMove(activePlayer, board, markCoords[0], markCoords[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Don't try to place marks outside the board.");
                continue;
            } catch (IllegalArgumentException e) {
                System.out.println("This cell is occupied. Select another.");
                continue;
            }

            Render.draw(board);

            activePlayer = game.switchPlayers(activePlayer, player1, player2);

        } //end of game loop

        System.out.println("Game Over!");

    }
}


/**
 * Class with game controls;
 */
class Game {
    /**
     * Place mark on the board
     * @param row row
     * @param column column
     */
    public void makeMove(Player currentPlayer, Board b, int row, int column) {
        b.placeMark(new Mark(currentPlayer), row, column);
    }

    /**
     * This method randomly return one of the players
     * @param a
     * @param b
     * @return
     */
    public Player chooseWhoStarts(Player a, Player b) {
        Player chosenOne;
        chosenOne = (Math.random() < 0.5) ? a : b;
        System.out.println(chosenOne.name + ", great random selected you to make first move.");
        return chosenOne;
    }

    public Player switchPlayers (Player current, Player p1, Player p2) {
        if (current == p1) return p2;
        else  return p1;
    }

    /**
     * The game ends when three marks placed in a row, column, or diagonal.
     * @param b board to examine
     * @return
     */
    public boolean isGameOver(Board b) {

        //vertical
        for (int i=0; i<3; i++) {
            if (b.fieldOwner(i,0) != null && b.fieldOwner(i,0) == b.fieldOwner(i,1) &&
                    b.fieldOwner(i,1) == b.fieldOwner(i,2)) return true;
        }

        //horizontal
        for (int i=0; i<3; i++) {
            if (b.fieldOwner(0, i) != null && b.fieldOwner(0,i) == b.fieldOwner(1,i) &&
                    b.fieldOwner(1,i) == b.fieldOwner(2,i)) return true;
        }

        //fast check for diagonal. If there is no mark in the middle then it's no use for next examination.
        if (b.fieldOwner(1,1) == null) return false;

        //diagonal
        if (b.fieldOwner(0,0) == b.fieldOwner(1,1) && b.fieldOwner(1,1) == b.fieldOwner(2,2)) return true;

        //other diagonal
        if (b.fieldOwner(0,2)== b.fieldOwner(1,1) && b.fieldOwner(1,1) == b.fieldOwner(2,0)) return true;

        //if there is no winning combo
        return false;

    }

} //end of Game

/**
 * This class takes user input from keyboard
 */
class InputControll {

    //input scanner
    static Scanner keyboard = new Scanner(System.in);

    /**
     * Static method which
     * @param activePlayer
     * @return int[] with coordinates for mark
     */
    static int[] interactWithPlayer(Player activePlayer) {

        System.out.println("Make your move " + activePlayer.name + "(" + activePlayer.mark + "):" );
        System.out.println("Choose Row (from 1 to 3):");
        int row = keyboard.nextInt() - 1;

        System.out.println("Choose Column (from 1 to 3):");
        int line = keyboard.nextInt() - 1;

        int[] ouput = new int[]{row, line};

        return ouput;
    }
}

/**
 * The board class contains field with marks.
 */
class Board {

    //field
    public Mark[][] field = new Mark[3][3];


    //Count how many marks were placed so the game loop could stop when  its full.
    public int counter = 0;

    /**
     * Detect whose mark on the spot. If no one then returns null.
     * @param row
     * @param column
     * @return
     */
    public Player fieldOwner (int row, int column) {
        Mark playerMark = field[row][column];
        if (playerMark == null) return null;
        else return playerMark.owner;
    }

    /**
     * Place mark. Throws exception if field is occupied.
     * @param mark
     * @param row
     * @param column
     * @throws IllegalArgumentException
     */
    public void placeMark(Mark mark, int row, int column) throws IllegalArgumentException {
        if(field[row][column] != null) throw new IllegalArgumentException("Occupied cell.");
        field[row][column] = mark;
        counter++;
    }
}

/**
 * This class is drawing the board in console
 */
class Render {
    static void draw(Board b) {
        System.out.println("  1|2|3");
        for (int i=0; i<3; i++) {
            String out = (i+1) + "|";
            for (int j=0; j<3; j++) {
                if (b.field[i][j] != null) out += b.field[i][j].owner.mark + "|";

                else out += "_|";
            }
            System.out.println(out);

        }

    }
}

/**
 * For marks on board.
 */
class Mark {
    public Player owner;

    //constructor
    Mark(Player player) {
        owner = player;
    }

}

/**
 * simple player class with simple fields.
 */
class Player {
    public String name;
    public String mark;

}
