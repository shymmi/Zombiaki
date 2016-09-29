package guis;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture {
	
	private int TEXTURE;
	private Vector2f POSITION;
	private Vector2f SCALE;
	
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		this.TEXTURE = texture;
		this.POSITION = position;
		this.SCALE = scale;
	}

	public int getTexture() {
		return TEXTURE;
	}

	public Vector2f getPosition() {
		return POSITION;
	}

	public Vector2f getScale() {
		return SCALE;
	}
	
	

}
