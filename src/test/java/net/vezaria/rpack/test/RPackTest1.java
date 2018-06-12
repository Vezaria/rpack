package net.vezaria.rpack.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import net.vezaria.rpack.PackResult;
import net.vezaria.rpack.PackedRectangle;
import net.vezaria.rpack.RectanglePacker;

public class RPackTest1 {
	
	public static void main(String[] args) {
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		sprites.add(new Sprite("1.png"));
		sprites.add(new Sprite("2.png"));
		sprites.add(new Sprite("3.png"));
		sprites.add(new Sprite("4.png"));
		
		RectanglePacker packer = new RectanglePacker(true);
		for(Sprite s : sprites) {
			packer.addRectangle(s.getWidth(), s.getHeight(), s);
		}
		
		long start = System.currentTimeMillis();
		PackResult res = packer.run();
		long time = System.currentTimeMillis() - start;
		System.out.println("Packed rectangles in " + time + "ms.");
		
		BufferedImage packedImage = new BufferedImage(res.getWidth(), res.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = packedImage.getGraphics();
		
		for(PackedRectangle r : res.getRectangles()) {
			Sprite sprite = (Sprite)r.userObject;
			g.drawImage(sprite.getImage(), r.x, r.y, null);
		}
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, res.getWidth() - 1, res.getHeight() - 1);
		
		try {
			ImageIO.write(packedImage, "PNG", new File("test1_out.png"));
			System.out.println("Wrote output to 'test1_out.png'.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static class Sprite {
		
		private int width;
		private int height;
		
		private BufferedImage img;
		
		public Sprite(String name) {
			try {
				img = ImageIO.read(getClass().getClassLoader().getResource(name));
				width = img.getWidth();
				height = img.getHeight();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
		
		public BufferedImage getImage() {
			return img;
		}
	}
}
