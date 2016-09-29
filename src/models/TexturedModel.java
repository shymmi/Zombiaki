package models;

import textures.ModelTexture;

public class TexturedModel {
	
	private RawModel RAW_MODEL;
	private ModelTexture MODEL_TEXTURE;

	
	public TexturedModel(RawModel model, ModelTexture texture){
		this.RAW_MODEL = model;
		this.MODEL_TEXTURE = texture;
	}

	public RawModel getRawModel() {
		return RAW_MODEL;
	}

	public ModelTexture getTexture() {
		return MODEL_TEXTURE;
	}
        

}
