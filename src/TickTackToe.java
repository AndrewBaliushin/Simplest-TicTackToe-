
public class TickTackToe {
	
	public static GameController game;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (UserInput.isOpponentAI()) {
			game = GameController.startGameVsAI();
		} else {
			game = GameController.startGameVsHuman();
		}
		
		game.drawBoard();
		
		while(game.isRunning()) {
			game.makeMove();
			game.drawBoard();
		}
		
		game.printGameOverMessage();
	}
}
