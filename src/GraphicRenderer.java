public class GraphicRenderer {
	
	public static void printGameOverMessage(Player winner) {
		StringBuffer sb = new StringBuffer();
		if(winner != null) {
			sb.append(winner.getName() + " wins.\n");
		} else {
			sb.append("Tie!");
		}
		sb.append("GameOver.");

		System.out.println(sb);
	}
	
	public static void renderBoardInConsole(Board.Mark[][] gameField) {
		StringBuilder sb = new StringBuilder();
		sb.append("  1|2|3");
		sb.append("\n");
		sb.append(convertGameFieldToString(gameField));	
		System.out.println(sb);
	}
	
	private static String convertGameFieldToString(Board.Mark[][] gameField) {
		StringBuilder sb = new StringBuilder();
		for (int row = 0; row < gameField.length; row++) {
			sb.append((row+1) + "|");
			for (int col = 0; col < gameField[row].length; col++) {
				Board.Mark mark = gameField[row][col];
				if (mark != null) {
					sb.append(mark.getOwner().getMarkSign());
				} else {
					sb.append("_");
				}
				sb.append("|");
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
