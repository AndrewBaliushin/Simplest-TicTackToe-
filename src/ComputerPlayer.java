import java.util.*;

public class ComputerPlayer extends Player {

	public static final String COMPUTER_NAME = "AI";
	
	public ComputerPlayer(String name, String markSign) {
		super(name, markSign);
	}

	public Board.Point getCoordsForMove() {
		return getBestMove(true).getCoords();
	}
	
	private Move getBestMove(boolean ourTurn) {
		Player currentPlayer = (ourTurn) ? this : getOppnent();
		Move bestMove = new Move();
		
		if(getInnerCopyBoard().getWinnerIfAny() != null) {
			int score = (ourTurn) ? -1 : 1; //opponent have win by last move
			bestMove.setScore(score);
			return bestMove;
		} 
		if(getInnerCopyBoard().isGameFieldFull()){
			bestMove.setScore(0);
			return bestMove;
		}
		
		//set scores lo\hi than min\max so move would return anyway
		if(ourTurn) {
			bestMove.setScore(-2);
		} else {
			bestMove.setScore(2);
		}
		
		List<Board.Point> freeSpots =  getInnerCopyBoard().getFreeSpots();
		for (Board.Point point : freeSpots) {
			getInnerCopyBoard().placeMarkOnField(new Board.Mark(currentPlayer), point);
			Move currentMove = getBestMove(!ourTurn);
			getInnerCopyBoard().removeMarkFromField(point);
			
			if ((ourTurn && (currentMove.getScore() > bestMove.getScore())) ||
					(!ourTurn && (currentMove.getScore() < bestMove.getScore()))) {
				bestMove.setCoords(point);
				bestMove.setScore(currentMove.getScore());
			} 
		}
		
		return bestMove;
	}
	
	private Player getOppnent() {
		return (playerPool.get(0) == this) ? playerPool.get(1) : playerPool.get(0);
	}
	
	private class Move {
		private int score;
		private Board.Point coords;

		public void setScore(int score) {
			this.score = score;
		}

		public void setCoords(Board.Point coords) {
			this.coords = coords;
		}

		public int getScore() {
			return score;
		}
		
		public Board.Point getCoords() {
			return coords;
		}
	}
}
