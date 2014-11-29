public class HumanPlayer extends Player {
	
	public static enum Names {
		SASHA,
		MASHA
	}
	

	public HumanPlayer(String name, String markSign) {
		super(name, markSign);
	}
	
	public Board.Point getCoordsForMove() {
		return UserInput.recieveCoordsFromHumanPlayer(this);
	}
	
}
