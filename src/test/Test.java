package test;

import processing.core.PApplet;
import utils.ColorHelper;

public class Test extends PApplet {

	int num = 10;

	int[][] c;

	public static void main(String[] args) {

		PApplet.main("test.Test");
	}

	public void settings() {
		size(700, 400);
	}

	public void setup() {
		colorMode(RGB);
		c = ColorHelper.createGradientHue(num, ColorHelper.RED, ColorHelper.BLUE);

	}

	public void draw() {

		background(255);

		for (int i = 0; i < num; ++i) {
			noStroke();
			fill(c[i][0], c[i][1], c[i][2]);
			rect(60 + i * 60, 200, 30, 60);
		}

	}

}
