package com.avona.games.towerdefence.gfx;

final public class ImageColorFormat {
	public static ImageColorFormat RGBA = new ImageColorFormat(0, 1, 2, 3);
	public static ImageColorFormat ABGR = new ImageColorFormat(3, 2, 1, 0);
	public int redIndex;
	public int greenIndex;
	public int blueIndex;
	public int alphaIndex;
	public ImageColorFormat(int r, int g, int b, int a) {
		redIndex = r;
		greenIndex = g;
		blueIndex = b;
		alphaIndex = a;
	}
}
