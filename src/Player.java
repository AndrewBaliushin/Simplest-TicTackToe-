import java.util.ArrayList;

public abstract class Player {
	
	protected static ArrayList<Player> playerPool = new ArrayList<>();
	
	
	private String name;
	private String markSign;
	
	private Board innerCopyBoard = new Board();
	
	public Player(String name, String markSign) {
		this.name = name;
		this.markSign = markSign;
		playerPool.add(this);
	}
	
	abstract public Board.Point getCoordsForMove();
	
	public void recieveNotificationAboutMove(Board.Mark mark, Board.Point coordsOfMove) {
		innerCopyBoard.placeMarkOnField(mark, coordsOfMove);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMarkSign() {
		return markSign;
	}

	public void setMarkSign(String markSign) {
		this.markSign = markSign;
	}
	
	protected Board getInnerCopyBoard() {
		return innerCopyBoard;
	}
}
