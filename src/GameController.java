import java.util.*;

public class GameController {
	
	private final static int MAX_PLAYERS = 2;
	
	private Board board;
	
	private Player[] playerPool = new Player[MAX_PLAYERS];
	private int currentPlayerIndexInPool;
	private final static int LAST_INDEX_OF_PLAYER_POOL = MAX_PLAYERS - 1;
	
	private Player playerForCurrentTurn;
	
	public static GameController startGameVsAI() {
		return new GameController(true);
	}
	
	public static GameController startGameVsHuman() {
		return new GameController(false);
	}
	
	private GameController(boolean useAIoppenent) {
		addPlayersToPool(useAIoppenent);
		createBoard();
	}
	
	private void createBoard() {
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
		Board.Point coords = getCoordsForNextMove();
		if(board.isValidSpot(coords)) {
			placeMarkOnField(new Board.Mark(playerForCurrentTurn), coords);
		} else {
			usePreviousPlayer();
			System.out.println("Invalid coordinates. Try again.");			
		}	
	}
	
	public void printGameOverMessage() {
		Player winner = board.getWinnerIfAny();
		GraphicRenderer.printGameOverMessage(winner);
	}

	protected void placeMarkOnField(Board.Mark mark, Board.Point coords) {
		board.placeMarkOnField(mark, coords);
	}
	
	protected void removeMarkFromField(Board.Point point) {
		board.removeMarkFromField(point);
	}

	protected Player getWinnerIfAny() {
		return board.getWinnerIfAny();
	}
	
	protected boolean isGameFieldFull() {
		return board.isGameFieldFull();
	}
	
	protected List<Board.Point> getAvailibleSpots() {
		return board.getFreeSpots();
	}

	private Board.Point getCoordsForNextMove() {
		Board.Point coords;
		if(playerForCurrentTurn instanceof HumanPlayer) {
			coords = askHumanPlayerForCoords();
		} else { //computer
			coords = askAIforCoords();
		}
		return coords;
	}

	private Board.Point askHumanPlayerForCoords() {
		Board.Point point = UserInput.recieveCoordsFromHumanPlayer(playerForCurrentTurn);
		return point;
	}

	private Board.Point askAIforCoords() {
		return ComputerPlayer.getBestMove(this);
	}

	private void addPlayersToPool(boolean useAIopponent) {
		for (int i = 0; i < playerPool.length; i++) {
			if(i == LAST_INDEX_OF_PLAYER_POOL && useAIopponent) {
				playerPool[i] = new ComputerPlayer(ComputerPlayer.COMPUTER_NAME, 
						Board.Mark.MarkSigns.values()[i].name());
			}  else {
				playerPool[i] = new HumanPlayer(Player.Names.values()[i].name(), 
						Board.Mark.MarkSigns.values()[i].name());
			}
		}
	}

	protected void useNextPlayerOrSelectByRandomIfNone() {
		if(playerForCurrentTurn == null) {
			moveCurrentPlayerIndexToRandom();
		} else {
			moveCurrentPlayerIndexToNext();
		}		
		updateCurrentPlayerRef();
	}
	
	protected void usePreviousPlayer() {
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
	
	protected Player getCurrentPlayer() {
		return playerForCurrentTurn;
	}
}
