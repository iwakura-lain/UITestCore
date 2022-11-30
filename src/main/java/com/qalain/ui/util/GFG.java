package com.qalain.ui.util;

import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

@Slf4j
public class GFG {

	// Main driver method
	public static String compareImgAndGetPercentage(String imgPath1, String imgPath2) {

		BufferedImage imgA = null;
		BufferedImage imgB = null;

		try {
			InputStream img1 = GFG.class.getClassLoader().getResourceAsStream(imgPath1);
			InputStream img2 = GFG.class.getClassLoader().getResourceAsStream(imgPath2);
			imgA = ImageIO.read(img1);
			imgB = ImageIO.read(img2);
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		int width1 = imgA.getWidth();
		int width2 = imgB.getWidth();
		int height1 = imgA.getHeight();
		int height2 = imgB.getHeight();

		// Checking whether the images are of same size or
		// not
		if ((width1 != width2) || (height1 != height2)){
			log.error("Error: Images dimensions" + " mismatch");
			return "0%";
		} else {
			long difference = 0;
			for (int y = 0; y < height1; y++) {
				for (int x = 0; x < width1; x++) {
					int rgbA = imgA.getRGB(x, y);
					int rgbB = imgB.getRGB(x, y);
					int redA = (rgbA >> 16) & 0xff;
					int greenA = (rgbA >> 8) & 0xff;
					int blueA = (rgbA)&0xff;
					int redB = (rgbB >> 16) & 0xff;
					int greenB = (rgbB >> 8) & 0xff;
					int blueB = (rgbB)&0xff;

					difference += Math.abs(redA - redB);
					difference += Math.abs(greenA - greenB);
					difference += Math.abs(blueA - blueB);
				}
			}
			double total_pixels = width1 * height1 * 3;
			double avg_different_pixels = difference / total_pixels;
			double percentage
				= (avg_different_pixels / 255) * 100;
			System.out.println("Difference Percentage-->"
							+ percentage);
			return percentage + "%";
		}
	}
}
