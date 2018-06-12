package net.vezaria.rpack;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectanglePacker {

	/**
	 * The list of all user supplied rectangles.
	 */
	private List<Rectangle> rectangles;
	private int rectCount = 0;
	
	/**
	 * Should the output be a square, or a rectangle?
	 */
	private boolean enforceSquare;

	public RectanglePacker() {
		this(false);
	}
	
	/**
	 * @param square If set to true, will ensure that the output is a square. 
	 * This is useful when you want a square texture atlas for use with OpenGL.
	 */
	public RectanglePacker(boolean square) {
		this.rectangles = new ArrayList<Rectangle>();
		this.enforceSquare = square;
	}

	/**
	 * Adds a rectangle to be packed.
	 * @param width The width of the rectangle.
	 * @param height The height of the rectangle.
	 * @param userObject An object which will be returned along with the result.
	 */
	public void addRectangle(int width, int height, Object userObject) {
		rectangles.add(new Rectangle(width, height, userObject));
		rectCount++;
	}

	/**
	 * Runs the rectangle packing algorithm using the rectangles supplied with
	 * <code>addRectangle</code>.
	 * @return An object containing information about the packed rectanlge.
	 */
	public PackResult run() {
		int areaSum = 0;
		int minW = 0;
		int minH = 0;
		int boundingW = 0;
		int boundingH = 0;

		for (Rectangle rect : rectangles) {
			areaSum += rect.area;
			if (minW < rect.width)
				minW = rect.width;
			if (minH < rect.height)
				minH = rect.height;
		}

		rectangles.sort(new Comparator<Rectangle>() {
			public int compare(Rectangle r1, Rectangle r2) {
				return r1.area - r2.area;
			}
		});

		boolean run = true;
		while (run) {
			Map<Integer, Integer> factors = new HashMap<Integer, Integer>();
			for (int i = 1; i < Math.sqrt(areaSum); i++) {
				if (areaSum % i == 0) {
					if ((areaSum / i) >= minH && i >= minW) {
						factors.put(i, areaSum / i);
					}

					if (i >= minH && (areaSum / i) >= minW) {
						factors.put(areaSum / i, i);

					}
				}
			}
			
			if(!factors.isEmpty()) {
				for(Map.Entry<Integer, Integer> factor : factors.entrySet()) {
					int bigW = factor.getKey();
					int bigH = factor.getValue();
					boolean found = check(bigW, bigH);
					if(found) {
						run = false; 
				        boundingW = factor.getKey();
				        boundingH = factor.getValue();
				        
				        if(enforceSquare == true) {
				        	if(boundingW > boundingH) {
				        		boundingH = boundingW;
				        	} else {
				        		boundingW = boundingH;
				        	}
				        }
				        
				        break;
					}
				}
			}
			areaSum++;
		}
		
		List<PackedRectangle> resultRectangles = new ArrayList<PackedRectangle>();
		for(Rectangle rect : rectangles) {
			resultRectangles.add(new PackedRectangle(rect.location.x, rect.location.y, rect.width, rect.height, rect.object));
		}
		
		// Clear the rectangles array so the packer can be reused.
		rectangles.clear();
		rectCount = 0;
		
		return new PackResult(boundingW, boundingH, resultRectangles);
	}

	private boolean check(int width, int height) {
		int i = 0;
		int flag = 0;
		List<Point> coordinates = new ArrayList<Point>();
		Point p1 = new Point(rectangles.get(0).width, 0);
		Point p2 = new Point(0, rectangles.get(0).height);
		coordinates.add(p1);
		coordinates.add(p2);

		i++;
		flag++;

		while (i < rectCount) {
			for (Point p : coordinates) {
				int startX = p.x;
				int startY = p.y;
				rectangles.get(i).location = p;

				if (startX + rectangles.get(i).width <= width && startY + rectangles.get(i).height <= height
						&& !isOverlapping(i)) {
					coordinates.remove(p);

					coordinates.add(new Point(startX + rectangles.get(i).width, startY));
					coordinates.add(new Point(startX, startY + rectangles.get(i).height));

					flag++;
					break;
				}
			}
			i++;
		}

		return flag == rectCount;
	}

	private boolean isOverlapping(int i) {

		for (int j = 0; j < i; j++) {

			if (rectangles.get(i).location.x >= rectangles.get(j).location.x + rectangles.get(j).width
					|| rectangles.get(i).location.x + rectangles.get(i).width <= rectangles.get(j).location.x
					|| rectangles.get(i).location.y >= rectangles.get(j).location.y + rectangles.get(j).height
					|| rectangles.get(i).location.y + rectangles.get(i).height <= rectangles.get(j).location.y) {
			} else {
				return true;
			}
		}
		return false;

	}

	private static class Rectangle {

		private int width;
		private int height;
		private int area;
		private Object object;

		private Point location = new Point(0, 0);

		public Rectangle(int width, int height, Object userObject) {
			this.width = width;
			this.height = height;
			this.area = width * height;
			this.object = userObject;
		}
	}
}
