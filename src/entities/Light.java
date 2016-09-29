package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f POSITION;
	private Vector3f COLOR;
	private Vector3f ATTENUATION = new Vector3f(1, 0, 0); //oslabienie swiatla
	
	public Light(Vector3f position, Vector3f colour) {
		this.POSITION = position;
		this.COLOR = colour;
	}
	
	public Light(Vector3f position, Vector3f colour, Vector3f attenuation) {
		this.POSITION = position;
		this.COLOR = colour;
		this.ATTENUATION = attenuation;
	}
	
	public Vector3f getAttenuation(){
		return ATTENUATION;
	}

	public Vector3f getPosition() {
		return POSITION;
	}

	public void setPosition(Vector3f position) {
		this.POSITION = position;
	}

	public Vector3f getColour() {
		return COLOR;
	}

	public void setColour(Vector3f colour) {
		this.COLOR = colour;
	}
	

}
