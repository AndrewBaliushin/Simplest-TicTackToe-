import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ComputerPlayer extends Player {

	public static final String COMPUTER_NAME = "AI";
	
	public ComputerPlayer(String name, String markSign) {
		super(name, markSign);
	}

	/**
	 * Uses minmax algorithm. 
	 * @param game
	 * @return
	 */
	public static Board.Point getBestMove(GameController game) {		
		List<Board.Point> freeSpots = game.getAvailibleSpots();
		
		Map<Board.Point, Integer> scoreForSpots = new HashMap<>();
		
		for (Board.Point point : freeSpots) {
			game.placeMarkOnField(new Board.Mark(game.getCurrentPlayer()), point);
			
			scoreForSpots.put(point, getScoreForCurrenMove(game, game.getCurrentPlayer()));
			
			game.removeMarkFromField(point);
		}
		
		return getBestScoredMove(scoreForSpots);
	}
	
	private static int getScoreForCurrenMove(GameController game, Player selectedPlayer) {
		if(game.getWinnerIfAny() != null) {
			return (game.getCurrentPlayer() == selectedPlayer) ? 1 : -1;
		} 
		if(game.isGameFieldFull()){
			return 0;
		}
		
		game.useNextPlayerOrSelectByRandomIfNone();
		Player playerForCurrentTurn = game.getCurrentPlayer();
		
		List<Board.Point> freeSpots = game.getAvailibleSpots();
		
		List<Integer> scores = new ArrayList<Integer>();
		for (Board.Point point : freeSpots) {
			game.placeMarkOnField(new Board.Mark(playerForCurrentTurn), point);
			scores.add(getScoreForCurrenMove(game, selectedPlayer));
			game.removeMarkFromField(point);
		}
		
		game.usePreviousPlayer();
		
		Collections.sort(scores);
		
		return (playerForCurrentTurn == selectedPlayer) ? scores.get(scores.size() - 1) : 
			scores.get(0);
	}	
	
	private static Board.Point getBestScoredMove (Map<Board.Point, Integer> scoreForSpots) {
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
}
