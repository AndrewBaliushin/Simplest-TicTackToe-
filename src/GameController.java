import java.util.*;

public class GameController {
	
	private final static int NUM_PLAYERS = 2;
	
	private Board board;
	
	private Player[] playerPool = new Player[NUM_PLAYERS];
	private int currentPlayerIndexInPool;
	private final static int LAST_INDEX_OF_PLAYER_POOL = NUM_PLAYERS - 1;
	
	private Player playerForCurrentTurn;
	
	public static GameController startGameVsAI() {
		return new GameController(true);
	}
	
	public static GameController startGameVsHuman() {
		return new GameController(false);
	}
	
	private GameController(boolean useAIoppenent) {
		createPlayerToPool(useAIoppenent);
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

	private void createPlayerToPool(boolean useAIopponent) {
		for (int i = 0; i < playerPool.length; i++) {
			if(i == LAST_INDEX_OF_PLAYER_POOL && useAIopponent) {
				playerPool[i] = new ComputerPlayer(ComputerPlayer.COMPUTER_NAME, 
						Board.Mark.MarkSigns.values()[i].name());
			}  else {
				playerPool[i] = new HumanPlayer(HumanPlayer.Names.values()[i].name(), 
						Board.Mark.MarkSigns.values()[i].name());
			}
		}
	}

	private void notifyPlayersAboutMove(Board.Mark mark, Board.Point coordsOfMove) {
		for (int i = 0; i < playerPool.length; i++) {
			playerPool[i].recieveNotificationAboutMove(mark, coordsOfMove);
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
		playerForCurrentTurn = playerPool[currentPlayerIndexInPool];
	}
	
	private void moveCurrentPlayerIndexToRandom() {
		 Random r = new Random();
		 int index = r.nextInt(playerPool.length);
		 currentPlayerIndexInPool = index;
	}
	
	private void moveCurrentPlayerIndexToNext() {
		if(++currentPlayerIndexInPool >= playerPool.length) {
			currentPlayerIndexInPool = 0;
		} 
	}
	
	private void moveCurrentPlayerIndexToPrev() {
		if(--currentPlayerIndexInPool < 0) {
			currentPlayerIndexInPool = playerPool.length - 1;
		} 
		updateCurrentPlayerRef();
	}
}
