import java.util.*;

public class GameController {
	
	private Board board;
	
	private int currentPlayerIndexInPool;
	
	private Player playerForCurrentTurn;
	
	public static GameController startGameVsAI() {
		return new GameController(true);
	}
	
	public static GameController startGameVsHuman() {
		return new GameController(false);
	}
	
	private GameController(boolean useAIoppenent) {
		createPlayers(useAIoppenent);
		board = new Board();
	}
	
	public void drawBoard() {
		GraphicRenderer.renderBoardInConsole(board.getGameField());
	}

	public boolean isRunning() {
		return (!board.isGameFieldFull() && board.getWinnerIfAny() == null);
	}

	public void makeMove() {
		useNextPlayerOrSelectByRandomIfNone();
		Board.Point coords = playerForCurrentTurn.getCoordsForMove();
		if(board.isValidSpot(coords)) {
			Board.Mark mark = new Board.Mark(playerForCurrentTurn);
			board.placeMarkOnField(mark, coords);
			notifyPlayersAboutMove(mark, coords);
		} else {
			usePreviousPlayer();
			System.out.println("Invalid coordinates. Try again.");			
		}	
	}
	
	public void printGameOverMessage() {
		Player winner = board.getWinnerIfAny();
		GraphicRenderer.printGameOverMessage(winner);
	}

	private void createPlayers(boolean useAIopponent) {
		if(useAIopponent) {
			new ComputerPlayer(ComputerPlayer.COMPUTER_NAME, 
					Board.Mark.MarkSigns.values()[0].name());
		} else {
			new HumanPlayer(HumanPlayer.Names.values()[0].name(), 
					Board.Mark.MarkSigns.values()[0].name());
		}
		
		new HumanPlayer(HumanPlayer.Names.values()[1].name(), 
				Board.Mark.MarkSigns.values()[1].name());
	}

	private void notifyPlayersAboutMove(Board.Mark mark, Board.Point coordsOfMove) {
		for (int i = 0; i < Player.playerPool.size(); i++) {
			Player.playerPool.get(i).recieveNotificationAboutMove(mark, coordsOfMove);
		}
	}

	private void useNextPlayerOrSelectByRandomIfNone() {
		if(playerForCurrentTurn == null) {
			moveCurrentPlayerIndexToRandom();
		} else {
			moveCurrentPlayerIndexToNext();
		}		
		updateCurrentPlayerRef();
	}
	
	private void usePreviousPlayer() {
		moveCurrentPlayerIndexToPrev();
		updateCurrentPlayerRef();
	}
	
	private void updateCurrentPlayerRef() {
		playerForCurrentTurn = Player.playerPool.get(currentPlayerIndexInPool);
	}
	
	private void moveCurrentPlayerIndexToRandom() {
		 Random r = new Random();
		 int index = r.nextInt(Player.playerPool.size());
		 currentPlayerIndexInPool = index;
	}
	
	private void moveCurrentPlayerIndexToNext() {
		if(++currentPlayerIndexInPool >= Player.playerPool.size()) {
			currentPlayerIndexInPool = 0;
		} 
	}
	
	private void moveCurrentPlayerIndexToPrev() {
		if(--currentPlayerIndexInPool < 0) {
			currentPlayerIndexInPool = Player.playerPool.size() - 1;
		} 
	}
}
