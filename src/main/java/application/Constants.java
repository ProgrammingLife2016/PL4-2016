package application;

public class Constants {
	private int GRAPHBOX_WIDTH = 800;
	private int GRAPHOX_HEIGHT = 600;
	
	private int ZOOMBOX_WIDTH =  (int) (GRAPHBOX_WIDTH /3.0);
	private int ZOOMBOX_HEIGHT = (int) (GRAPHOX_HEIGHT /3.0);
	
	private int WINDOW_WIDTH = GRAPHBOX_WIDTH + ZOOMBOX_WIDTH + 30;
	private int WINDOW_HEIGHT = GRAPHOX_HEIGHT + 20;


	public Constants() {
	}

	public int getGRAPHBOX_WIDTH() {
		return GRAPHBOX_WIDTH;
	}

	public void setGRAPHBOX_WIDTH(int GRAPHBOX_WIDTH) {
		this.GRAPHBOX_WIDTH = GRAPHBOX_WIDTH;
	}

	public int getGRAPHOX_HEIGHT() {
		return GRAPHOX_HEIGHT;
	}

	public void setGRAPHOX_HEIGHT(int GRAPHOX_HEIGHT) {
		this.GRAPHOX_HEIGHT = GRAPHOX_HEIGHT;
	}

	public int getZOOMBOX_WIDTH() {
		return ZOOMBOX_WIDTH;
	}

	public void setZOOMBOX_WIDTH(int ZOOMBOX_WIDTH) {
		this.ZOOMBOX_WIDTH = ZOOMBOX_WIDTH;
	}

	public int getZOOMBOX_HEIGHT() {
		return ZOOMBOX_HEIGHT;
	}

	public void setZOOMBOX_HEIGHT(int ZOOMBOX_HEIGHT) {
		this.ZOOMBOX_HEIGHT = ZOOMBOX_HEIGHT;
	}

	public int getWINDOW_WIDTH() {
		return WINDOW_WIDTH;
	}

	public void setWINDOW_WIDTH(int WINDOW_WIDTH) {
		this.WINDOW_WIDTH = WINDOW_WIDTH;
	}

	public int getWINDOW_HEIGHT() {
		return WINDOW_HEIGHT;
	}

	public void setWINDOW_HEIGHT(int WINDOW_HEIGHT) {
		this.WINDOW_HEIGHT = WINDOW_HEIGHT;
	}
}
