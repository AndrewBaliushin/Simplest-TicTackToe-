import java.util.Scanner;

public class UserInput {

	static Scanner keyboard = new Scanner(System.in);

	static Board.Point recieveCoordsFromHumanPlayer(Player activePlayer) {
		System.out.println("Make your move " + activePlayer.getName()
				+ "(" + activePlayer.getMarkSign() + "):");
		System.out.println("Choose Row (from 1 to 3):");
		int row = keyboard.nextInt() - 1;

		System.out.println("Choose Column (from 1 to 3):");
		int col = keyboard.nextInt() - 1;
		
		Board.Point point = Board.Point.create(row, col);

		return point;
	}

	static boolean isOpponentAI() {
		System.out.println("Do you want to play against AI?");
		System.out.println("1 -- Use AI. 2 -- VS mode.");
		return (keyboard.nextInt() == 1);
	}

}
