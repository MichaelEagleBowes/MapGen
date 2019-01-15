package model;

public class MapContainer {

	int x;
	int y;
	double areaCount;
	double relativeSpace;
	/**
	 * The number of maps with similar attributes.
	 */
	int mapCount;
	/**
	 * The total number of sampled maps
	 */
	int totalMapCount;
	
	public MapContainer() {
		
	}
	
	public MapContainer(int x, int y, double areaCount, double relativeSpace, int mapCount, int totalMapCount) {
		this.x = x;
		this.y = y;
		this.areaCount = areaCount;
		this.relativeSpace = relativeSpace;
		this.mapCount = mapCount;
		this.totalMapCount = totalMapCount;
	}
	
	public int getMapCount() {
		return mapCount;
	}
	public void setMapCount(int mapCount) {
		this.mapCount = mapCount;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public double getAreaCount() {
		return areaCount;
	}
	public void setAreaCount(double areaCount) {
		this.areaCount = areaCount;
	}
	public double getRelativeSpace() {
		return relativeSpace;
	}
	public void setRelativeSpace(double relativeSpace) {
		this.relativeSpace = relativeSpace;
	}

	public int getTotalMapCount() {
		return totalMapCount;
	}

	public void setTotalMapCount(int totalMapCount) {
		this.totalMapCount = totalMapCount;
	}
	
}
