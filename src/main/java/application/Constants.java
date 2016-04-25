package application;

public class Constants {
	public static final int GRAPHBOX_WIDTH = 800;
	public static final int GRAPHOX_HEIGHT = 600;
	
	public static final int ZOOMBOX_WIDTH = (int)(GRAPHBOX_WIDTH * (1.0 / 3.0));
	public static final int ZOOMBOX_HEIGHT = (int)(GRAPHOX_HEIGHT * (1.0 / 3.0));
	
	public static final int WINDOW_WIDTH = GRAPHBOX_WIDTH + ZOOMBOX_WIDTH + 30;
	public static final int WINDOW_HEIGHT = GRAPHOX_HEIGHT + 20;
}
