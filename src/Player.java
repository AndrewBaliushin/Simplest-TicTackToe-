public abstract class Player {
	
	public static enum Names {
		SASHA,
		MASHA
	}
	
	private String name;
	private String markSign;

	public Player(String name, String markSign) {
		this.name = name;
		this.markSign = markSign;
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
}
