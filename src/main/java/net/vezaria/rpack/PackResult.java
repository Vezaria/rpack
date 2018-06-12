package net.vezaria.rpack;

import java.util.List;

public class PackResult {
	
	private int width, height;
	private List<PackedRectangle> rectangles;
	
	PackResult(int width, int height, List<PackedRectangle> rectangles) {
		this.width = width;
		this.height = height;
		this.rectangles = rectangles;
	}
	
	/**
	 * @return The width of the rectangle needed to contain all given rectangles.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return The height of the rectangle needed to contain all given rectangles.
	 */
	public int getHeight() {
		return height;
	}
	
	public List<PackedRectangle> getRectangles() {
		return rectangles;
	}
}
