import java.util.*;

public class ComputerPlayer extends Player {

	private class Move {
		public int score;
		public Board.Point coords;
	}

	public static final String COMPUTER_NAME = "AI";
	
	public ComputerPlayer(String name, String markSign) {
		super(name, markSign);
	}

	public Board.Point getCoordsForMove() {
		return getBestMove(true).coords;
	}
	
	private Move getBestMove(boolean ourTurn) {
		Player currentPlayer = (ourTurn) ? this : getOppnent();
		Move bestMove = new Move();
		
		if(getInnerCopyBoard().getWinnerIfAny() != null) {
			int score = (ourTurn) ? -1 : 1; //opponent have win by last move
			bestMove.score = score;
			return bestMove;
		} 
		if(getInnerCopyBoard().isGameFieldFull()){
			bestMove.score = 0;
			return bestMove;
		}
		
		//set scores lo\hi than min\max so move would return anyway
		if(ourTurn) {
			bestMove.score = -2;
		} else {
			bestMove.score = 2;
		}
		
		List<Board.Point> freeSpots =  getInnerCopyBoard().getFreeSpots();
		for (Board.Point point : freeSpots) {
			getInnerCopyBoard().placeMarkOnField(new Board.Mark(currentPlayer), point);
			Move currentMove = getBestMove(!ourTurn);
			getInnerCopyBoard().removeMarkFromField(point);
			
			if ((ourTurn && (currentMove.score > bestMove.score)) ||
					(!ourTurn && (currentMove.score < bestMove.score))) {
				bestMove.coords = point;
				bestMove.score = currentMove.score;
			} 
		}
		
		return bestMove;
	}
	
	private Player getOppnent() {
		return (playerPool.get(0) == this) ? playerPool.get(1) : playerPool.get(0);
	}
}
