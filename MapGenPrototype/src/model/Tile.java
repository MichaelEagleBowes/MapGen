package model;

public class Tile {

	public int x;
	public int y;
	int heightValue;
	
	public Tile(int x, int y, int heightValue) {
		this.x = x;
		this.y = y;
		this.heightValue = heightValue;
	}
	
	public int getValue() {
		return heightValue;
	}
	
	public void setValue(int heightValue) {
		this.heightValue = heightValue;
	}
	
}
