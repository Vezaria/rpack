package net.vezaria.rpack;

public class PackedRectangle {

	/**
	 * The position of the rectangle in the resulting packed configuration.
	 */
	public final int x, y;
	
	/**
	 * The width and height of this rectangle, as given when it was added
	 * to the rectangle packer.
	 */
	public final int width, height;
	
	/**
	 * The user supplied object which was passed to the rectangle packers
	 * <code>addRectangle</code> method.
	 */
	public final Object userObject;
	
	PackedRectangle(int x, int y, int width, int height, Object userObject) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.userObject = userObject;
	}
}
