package models;

public class RawModel {
	
	private int vaoID;
	private int VERTEX_COUNT;
	
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.VERTEX_COUNT = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return VERTEX_COUNT;
	}
}
