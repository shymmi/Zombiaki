package entities;

import java.util.Random;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;

public class Enemy {

    private TexturedModel MODEL;
    private Vector3f POSITION;
    private float ROT_X, ROT_Y, ROT_Z;
    private float SCALE;
    private static final float RUN_SPEED = 20;
    private static final float GRAVITY_EFFECT = -50;
    private float CURRENT_SPEED = 0;
    private float TURNING_SPEED = 0;
    private float MOVING_SPEED = 0;
    private final int ID;
    private int HP;
    private int TEXTURE_INDEX = 0;

  
    public float getTextureXOffset() {
        int column = TEXTURE_INDEX % MODEL.getTexture().getNumberOfRows();
        return (float) column / (float) MODEL.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = TEXTURE_INDEX / MODEL.getTexture().getNumberOfRows();
        return (float) row / (float) MODEL.getTexture().getNumberOfRows();
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.POSITION.x += dx;
        this.POSITION.y += dy;
        this.POSITION.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.ROT_X += dx;
        this.ROT_Y += dy;
        this.ROT_Z += dz;
    }
    
    public  int getID() {
        return ID;
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

    public Enemy(int id, TexturedModel model, Vector3f position, float rotY, float scale, int hp) {
        this.MODEL = model;
        this.POSITION = position;
        this.ROT_X = 1;
        this.ROT_Y = rotY;
        this.ROT_Z = 1;
        this.SCALE = scale;        
        this.ID = id;
        this.HP = hp;
    }

    public void moveToPlayerDirection(Terrain terrain, Player player) {
        this.CURRENT_SPEED = 5;
        this.TURNING_SPEED = 1;
        float distance = CURRENT_SPEED * DisplayManager.getFrameTimeSeconds();
        float player_X_position = player.getPosition().x;
        float player_Z_position = player.getPosition().z;
        float enemy_X_position = POSITION.x;
        float enemy_Z_position = POSITION.z;
        
        if (Math.abs(enemy_X_position - player_X_position) < 1 && Math.abs(enemy_Z_position - player_Z_position) < 1) {
            player.takeWound();
        }
        
        if (enemy_X_position < player_X_position) {
            if (enemy_Z_position < player_Z_position) {
                increasePosition(distance, 0, distance);
            }
            else {
                increasePosition(distance, 0, -distance);
            }
        }
        else {
            if (enemy_Z_position < player_Z_position) {
                increasePosition(-distance, 0, distance);
            }
            else {
                increasePosition(-distance, 0, -distance);
            }
        }

        MOVING_SPEED += GRAVITY_EFFECT * DisplayManager.getFrameTimeSeconds();
        increasePosition(0, MOVING_SPEED * DisplayManager.getFrameTimeSeconds(), 0);
        
        float terrain_Y_position = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if (getPosition().y < terrain_Y_position) {
            MOVING_SPEED = 0;
            getPosition().y = terrain_Y_position;
        }
    }

    public int getHP() {
        return this.HP;
    }

    public void setHP(int damage) {
        this.HP -= damage ;
    }

}

