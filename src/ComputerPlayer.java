public class ComputerPlayer extends Player {

	public static String computerName = "AI";
	
	public ComputerPlayer(String name, String markSign) {
		super(name, markSign);
	}
	
	public static Board.Point getBestMove(GameController game) {
		return GameController.getBestMove(game);
	}
	
}
