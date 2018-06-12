package net.vezaria.rpack.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

import net.vezaria.rpack.PackResult;
import net.vezaria.rpack.PackedRectangle;
import net.vezaria.rpack.RectanglePacker;

public class RPackTest2 {

	private static final int RECTANGLE_COUNT = 25;
	
	private static final int MIN_WIDTH = 40;
	private static final int MAX_WIDTH = 200;
	
	private static final int MIN_HEIGHT = 10;
	private static final int MAX_HEIGHT = 120;
	
	public static void main(String[] args) {
		RectanglePacker packer = new RectanglePacker();
		for(int i = 0; i < RECTANGLE_COUNT; i++) {
			BufferedImage img = createImage();
			packer.addRectangle(img.getWidth(), img.getHeight(), img);
		}
		
		long start = System.currentTimeMillis();
		PackResult res = packer.run();
		long time = System.currentTimeMillis() - start;
		System.out.println("Packed rectangles in " + time + "ms.");
		
		BufferedImage packedImage = new BufferedImage(res.getWidth(), res.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = packedImage.getGraphics();
		
		for(PackedRectangle r : res.getRectangles()) {
			BufferedImage img = (BufferedImage)r.userObject;
			g.drawImage(img, r.x, r.y, null);
		}
		
		try {
			ImageIO.write(packedImage, "PNG", new File("test2_out.png"));
			System.out.println("Wrote output to 'test2_out.png'.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static BufferedImage createImage() {
		Random r = new Random();
		int width = r.nextInt(MAX_WIDTH - MIN_WIDTH) + MIN_WIDTH;
		int height = r.nextInt(MAX_HEIGHT - MIN_HEIGHT) + MIN_HEIGHT;
		int color = r.nextInt();
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(new Color(color));
		g.fillRect(0, 0, width, height);
		g.setColor(Color.BLACK);
		g.drawString(width + "x" + height, 2, 14);
		
		return img;
	}
}
