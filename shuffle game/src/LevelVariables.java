import java.awt.Color;

public class LevelVariables{
	
	private int level;
	private int tries;
	private int correctPoint;
	private int wrongPoint;
	private Color customColor;
	String imageFolderName;
	public int score;
	
	
	public LevelVariables() {
		this.level = 1;
		this.tries = 18;
		this.correctPoint = 5;
		this.wrongPoint = 1;
		customColor = new Color(68, 115, 201);
		this.imageFolderName = "Level1-InternetAssets/";
	}

	public LevelVariables(int level, int score){
		this.score = score;
		switch(level) {
		case 1:
			this.level = 1;
			this.tries = 18;
			this.correctPoint = 5;
			this.wrongPoint = 1;
			customColor = new Color(68, 115, 201);
			this.imageFolderName = "Level1-InternetAssets/";
			break;
		
		case 2:
			this.level = 2;
			this.tries = 15;
			this.correctPoint = 4;
			this.wrongPoint = 2;
			customColor = new Color(109, 66, 153);
			this.imageFolderName = "Level2-CyberSecurityAssets/";
			break;
		
		case 3:
			this.level = 3;
			this.tries = 12;
			this.correctPoint = 3;
			this.wrongPoint = 3;
			customColor = new Color(174, 30, 37);
			this.imageFolderName = "Level3-GamingComputerAssets/";
			break;
		default:
			this.level = 1;
			this.tries = 18;
			this.correctPoint = 5;
			this.wrongPoint = 1;
			customColor = new Color(68, 115, 201);
			this.imageFolderName = "Level1-InternetAssets/";
			break;
		}
			
	}
	
	public String folderName() {
		return imageFolderName;
	}
	
	public Color getColor() {
		return customColor;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getTries() {
		return this.tries;
	}
	
	public void setTries(int tries) {
		this.tries = tries;
	}
	public int getCorrectPoint() {
		return this.correctPoint;
	}
	
	public void setCorrectPoint(int correctPoint) {
		this.correctPoint = correctPoint;
	}
	
	public void setWrongPoint(int wrongPoint) {
		this.wrongPoint = wrongPoint;
	}
	public int getWrongPoint() {
		return this.wrongPoint;
	}

	public void increaseLevel() {
		this.level++;
		
	}
	
	
}