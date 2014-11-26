import java.util.*;
import java.util.Map.Entry;

public class GameController {
	
	private final static int MAX_PLAYERS = 2;
	
	private Player[] playerPool = new Player[MAX_PLAYERS];
	private int currentPlayerIndexInPool;
	private final static int LAST_INDEX_OF_PLAYER_POOL = MAX_PLAYERS - 1;
	
	private Board board;
	
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
	
	public void makeMove() {
		useNextPlayerOrSelectByRandomIfNone();
		Board.Point coords = getCoordsForNextMove();
		if(board.isValidSpot(coords)) {
			board.placeMarkOnField(new Board.Mark(playerForCurrentTurn), coords);
		} else {
			usePreviousPlayer();
			System.out.println("Invalid coordinates. Try again.");			
		}	
	}

	public void drawBoard() {
		GraphicRenderer.renderBoardInConsole(board.getGameField());
	}		

	public boolean isRunning() {
		return (!board.isGameFieldFull() && board.getWinnerIfAny() == null);
	}
	
	public void printGameOverMessage() {
		Player winner = board.getWinnerIfAny();
		GraphicRenderer.printGameOverMessage(winner);
	}
	
	private void addPlayersToPool(boolean useAIopponent) {
		for (int i = 0; i < playerPool.length; i++) {
			if(i == LAST_INDEX_OF_PLAYER_POOL && useAIopponent) {
				playerPool[i] = new ComputerPlayer(ComputerPlayer.computerName, 
						Board.Mark.MarkSigns.values()[i].name());
			}  else {
				playerPool[i] = new HumanPlayer(Player.Names.values()[i].name(), 
						Board.Mark.MarkSigns.values()[i].name());
			}
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
	
	public static Board.Point getBestMove(GameController game) {		
		List<Board.Point> freeSpots = game.board.getFreeSpots();
		
		Map<Board.Point, Integer> scoreForSpots = new HashMap<>();
		
		for (Board.Point point : freeSpots) {
			game.board.placeMarkOnField(new Board.Mark(game.playerForCurrentTurn), point);
			
			scoreForSpots.put(point, getScoreForCurrenMove(game, game.playerForCurrentTurn));
			
			game.board.removeMarkFromField(point);
		}
		
		int maxScore = Integer.MIN_VALUE;
		Board.Point bestMove = null;
		for(Entry<Board.Point, Integer> entry: scoreForSpots.entrySet()) {
		    if (entry.getValue() > maxScore) {
		    	maxScore = entry.getValue();
		    	bestMove = entry.getKey();
		    }
		}
		
		return bestMove;
	}
	
	public static int getScoreForCurrenMove(GameController game, Player selectedPlayer) {
		if(game.board.getWinnerIfAny() != null) {
			return (game.playerForCurrentTurn == selectedPlayer) ? 1 : -1;
		} 
		if(game.board.isGameFieldFull()){
			return 0;
		}
		
		List<Board.Point> freeSpots = game.board.getFreeSpots();
		int score = 0;
		for (Board.Point point : freeSpots) {
			game.useNextPlayerOrSelectByRandomIfNone();
			game.board.placeMarkOnField(new Board.Mark(game.playerForCurrentTurn), point);
			
			score += getScoreForCurrenMove(game, selectedPlayer);
			
			game.usePreviousPlayer();
			game.board.removeMarkFromField(point);
		}
		
		return score;
	}	
}
