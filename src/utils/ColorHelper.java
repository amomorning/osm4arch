package utils;

import processing.core.PImage;

public class ColorHelper {
	public static int RED = 0xff3333;
	public static int BLUE = 0x6666ff;

	public static int[] hexToRGB(int c) {
		int[] ret = new int[3];
		for (int i = 2; i >= 0; --i) {
			ret[i] = c & (0xff);
			c >>= 8;
		}
		return ret;
	}

	public static int rgbToHEX(int[] rgb) {
		int c = 0;
		for (int i = 0; i < 3; ++i) {
			c ^= rgb[i];
			if (i != 2)
				c <<= 8;
		}
		return c;
	}

	public static int rgbToHEX(int r, int g, int b) {
		return rgbToHEX(new int[] { r, g, b });
	}

	public static float[] rgbToHSL(int r, int g, int b) {
		float max = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.min(g, b));
		float h = 0.0f, s = 0.0f, l = 0.0f;

		// hue
		if (max == min)
			h = 0;
		if (max == r && g >= b)
			h = 60.0f * (g - b) / (max - min);
		if (max == r && g < b)
			h = 60.0f * (g - b) / (max - min) + 360;
		if (max == g)
			h = 60.0f * (b - r) / (max - min) + 120;
		if (max == b)
			h = 60.0f * (r - g) / (max - min) + 240;

		// lightness
		l = 0.5f * (max + min);

		// saturation
		if (l == 0 || max == min)
			s = 0;
		if (l > 0 && l <= 0.5)
			s = (max - min) / (2.0f * l);
		if (l > 0.5)
			s = (max - min) / (2.0f - 2.0f * l);

		return new float[] { h, s, l };
	}

	public static float[] rgbToHSV(int r, int g, int b) {
		float max = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.min(g, b));
		float h = 0.0f, s = 0.0f, v = 0.0f;

		System.out.println(r + ", " + g + ", " + b);

		// hue
		if (max == min)
			h = 0.0f;
		if (max == r && g >= b)
			h = 60.0f * (g - b) / (max - min);
		if (max == r && g < b)
			h = 60.0f * (g - b) / (max - min) + 360;
		if (max == g)
			h = 60.0f * (b - r) / (max - min) + 120;
		if (max == b)
			h = 60.0f * (r - g) / (max - min) + 240;

		// saturation
		if (max == 0)
			s = 0;
		else
			s = 1.0f - min / max;

		// brightness
		v = max;

		return new float[] { h / 360.0f * 255.0f, s * 255.0f, v };
	}

	public static float[] rgbToHSV(int[] c) {
		return rgbToHSV(c[0], c[1], c[2]);
	}

	public static int[] hsvToRGB(float h, float s, float v) {
		h *= 360.0f / 255.0f;
		s /= 255.0f;
		v /= 255.0f;

		int flag = ((int) Math.floor(h / 60.0f)) % 6;
		float f = h / 60.0f - flag;
		float p = v * (1 - s);
		float q = v * (1 - f * s);
		float t = v * (1 - (1 - f) * s);

		float r = 0, g = 0, b = 0;
		switch (flag) {
		case 0:
			r = v;
			g = t;
			b = p;
			break;
		case 1:
			r = q;
			g = v;
			b = p;
			break;
		case 2:
			r = p;
			g = v;
			b = t;
			break;
		case 3:
			r = p;
			g = q;
			b = v;
			break;
		case 4:
			r = t;
			g = p;
			b = v;
			break;
		case 5:
			r = v;
			g = p;
			b = q;
			break;
		}
		return new int[] { (int) Math.round(255*r+Tools.EPS), (int) Math.round(255*g+Tools.EPS), (int) Math.round(255*b+Tools.EPS)};
	}

	public static int[] hsvToRGB(float[] cc) {
		return hsvToRGB(cc[0], cc[1], cc[2]);
	}
	
	public static int[][] createGradientHSV(int num, float[] a, float[] b) {
		float min = Math.min(a[0], b[0]);
		float max = Math.max(a[0], b[0]);
		float s = Math.min(a[1], b[1]);
		float v = Math.max(a[2], b[2]);
		
		int[][] c = new int[num][3];
		float step = (max-min)/(num-1);
		for(int i = 0; i < num; ++ i) {
			float h = min + i*step;
			c[i] = hsvToRGB(h, s, v);
		}
		return c;
	}

	public static int[][] createGradientHSV(int num, int a, int b) {
		return createGradientHSV(num, hexToRGB(a), hexToRGB(b));
	}

	private static int[][] createGradientHSV(int num, int[] a, int[] b) {
		return createGradientHSV(num, rgbToHSV(a), rgbToHSV(b));
	}

}