import java.util.*;

public class Board {
	
	private static final int FIELD_SIZE = 3;
	
	private Mark[][] gameField = new Mark[FIELD_SIZE][FIELD_SIZE];
	
	public static class Mark {
		
		public static enum MarkSigns {
			X,
			O
		};
		
	    private Player owner;

	    Mark(Player player) {
	        owner = player;
	    }
	    
	    public Player getOwner() {
	    	return owner;
	    }
	}
	
	public static class Point {
		int row, col;

		public static Point create(int row, int col) {
			return new Point(row, col);
		}
		
		private Point(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public int getRow() {
			return row;
		}

		public int getCol() {
			return col;
		}
		
		public boolean isInField() {
			return (row < FIELD_SIZE && row >= 0 && col < FIELD_SIZE && col >= 0);
		}
	}

	
	
	public boolean isGameFieldFull() {
		return (getFreeSpots().size() == 0);
	}
	
	public boolean isValidSpot(Point coords) {
		return (coords.isInField() && 
				(gameField[coords.getRow()][coords.getCol()] == null));
	}
	
	public List<Point> getFreeSpots() {
		List<Point> freeSpots = new ArrayList<>();
		
		for (int row = 0; row < gameField.length; row++) {
			for (int col = 0; col < gameField[row].length; col++) {
				Point spot = new Point(row, col);
				if (isValidSpot(spot)) {
					freeSpots.add(spot);
				}
			}
		}
		
		return freeSpots;
	}
	
	public Player getSpotOwner(Point coords) {
		return getSpotOwner(coords.getRow(), coords.getCol());
	}
	
	private Player getSpotOwner(int row, int col) {
		Mark markOnSpot = gameField[row][col];
		if(markOnSpot != null) {
			return markOnSpot.getOwner();
		} else {
			return null;
		}
		
	}	

	public Mark[][] getGameField() {
		return gameField;
	}
	
	public void placeMarkOnField(Mark mark, Point coords) {
		gameField[coords.getRow()][coords.getCol()] = mark;
	}
	
	public void removeMarkFromField(Point coords) {
		gameField[coords.getRow()][coords.getCol()] = null;
	}
	
	public Player getWinnerIfAny() {		
		Set<Player> winners = new HashSet<>();
		
		winners.add(lookForWinnerAtVertical());
		winners.add(lookForWinnerOnHorizontal());
		winners.add(lookForWinnerOnLeftDiagonal());
		winners.add(lookForWinnerOnRightDiagonal());
		
		winners.remove(null);
		
		return (winners.size() > 0) ? (Player) winners.toArray()[0] : null;
	}
	
	private Player lookForWinnerOnHorizontal() {
		for (int i = 0; i < FIELD_SIZE; i++) {
			Set<Player> spotOwnersOnRow = new HashSet<>();
			for(int row = 0; row < FIELD_SIZE; row++){
				spotOwnersOnRow.add(getSpotOwner(i, row));
			}
			if(spotOwnersOnRow.size() == 1 && getSpotOwner(i, 0) != null) {
				return getSpotOwner(i, 0);
			}
        }
		return null;
	}
	
	private Player lookForWinnerAtVertical() {
		for (int i = 0; i < FIELD_SIZE; i++) {
			Set<Player> spotOwnersOnColumn = new HashSet<>();
			for(int column = 0; column < FIELD_SIZE; column++){
				spotOwnersOnColumn.add(getSpotOwner(column, i));
			}
			if(spotOwnersOnColumn.size() == 1 && getSpotOwner(0, i) != null) {
				return getSpotOwner(0, i);
			}
		}
		return null;
	}
	
	private Player lookForWinnerOnLeftDiagonal() {
		Set<Player> spotOwnersOnDiagonal = new HashSet<>();
		for (int i = 0; i < FIELD_SIZE; i++) {
			spotOwnersOnDiagonal.add(getSpotOwner(i, i));
		}
		if(spotOwnersOnDiagonal.size() == 1 && getSpotOwner(0, 0) != null) {
			return getSpotOwner(0, 0);
		} 
		return null;
	}
	
	private Player lookForWinnerOnRightDiagonal() {
		Set<Player> spotOwnersOnDiagonal = new HashSet<>();
		int maxFieldIndex = FIELD_SIZE - 1;
		
		for (int i = 0; i < FIELD_SIZE; i++) {
			spotOwnersOnDiagonal.add(getSpotOwner(maxFieldIndex - i, i));
		}
		if(spotOwnersOnDiagonal.size() == 1 && getSpotOwner(maxFieldIndex, 0) != null) {
			return getSpotOwner(maxFieldIndex, 0);
		} 
		return null;
	}
	
	
}
