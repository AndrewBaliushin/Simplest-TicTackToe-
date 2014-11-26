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
		Player verticalOrHorizontalWinner = lookForWinnerOnVerticalAndHorizontal();
		Player diagonalWinner = lookForWinnerOnDiagonal();
		
		if(verticalOrHorizontalWinner != null) {
			return verticalOrHorizontalWinner;
		} else if (diagonalWinner != null) {
			return diagonalWinner;
		} else {
			return null;
		}
	}
	
	private Player lookForWinnerOnVerticalAndHorizontal() {
		for (int i = 0; i < FIELD_SIZE; i++) {
            if (getSpotOwner(i,0) != null && getSpotOwner(i,0) == getSpotOwner(i,1) &&
                    getSpotOwner(i,1) == getSpotOwner(i,2)) {
            	return getSpotOwner(i,0);
            }
            if (getSpotOwner(0, i) != null && getSpotOwner(0,i) == getSpotOwner(1,i) &&
                    getSpotOwner(1,i) == getSpotOwner(2,i)) {
            	return getSpotOwner(0, i);
            }
        }
		return null;
	}
	
	private Player lookForWinnerOnDiagonal() {
        if (getSpotOwner(1,1) == null) return null;

        if (getSpotOwner(0,0) == getSpotOwner(1,1) && getSpotOwner(1,1) == getSpotOwner(2,2)) {
        	return getSpotOwner(0,0);
        }

        if (getSpotOwner(0,2) == getSpotOwner(1,1) && getSpotOwner(1,1) == getSpotOwner(2,0)) {
        	return getSpotOwner(0,2);
        }
        return null;
	}
}
