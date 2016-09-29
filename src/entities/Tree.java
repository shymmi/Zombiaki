package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;

public class Tree {

    private TexturedModel MODEL;
    private Vector3f POSITION;
    private float ROT_X, ROT_Y, ROT_Z;
    private float SCALE;
    
    
    private int ID;

    private int textureIndex = 0;

  
/*
    public Player(TexturedModel model, int index, Vector3f position, float rotX, float rotY, float rotZ,
            float scale) {
        this.textureIndex = index;
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }
*/
    public float getTextureXOffset() {
        int column = textureIndex % MODEL.getTexture().getNumberOfRows();
        return (float) column / (float) MODEL.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = textureIndex / MODEL.getTexture().getNumberOfRows();
        return (float) row / (float) MODEL.getTexture().getNumberOfRows();
    }

    public TexturedModel getModel() {
        return MODEL;
    }

    public void setModel(TexturedModel model) {
        this.MODEL = model;
    }

    public Vector3f getPosition() {
        return POSITION;
    }

    public void setPosition(Vector3f position) {
        this.POSITION = position;
    }

    public float getRotX() {
        return ROT_X;
    }

    public void setRotX(float rotX) {
        this.ROT_X = rotX;
    }

    public float getRotY() {
        return ROT_Y;
    }

    public void setRotY(float rotY) {
        this.ROT_Y = rotY;
    }

    public float getRotZ() {
        return ROT_Z;
    }

    public void setRotZ(float rotZ) {
        this.ROT_Z = rotZ;
    }

    public float getScale() {
        return SCALE;
    }

    public void setScale(float scale) {
        this.SCALE = scale;
    }

    public Tree(int id, TexturedModel model, Vector3f position, float rotY, float scale) {
        this.MODEL = model;
        this.POSITION = position;
        this.ROT_X = 1;
        this.ROT_Y = rotY;
        this.ROT_Z = 1;
        this.SCALE = scale;        
        this.ID = id;
    }
    


}

