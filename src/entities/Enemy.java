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

    private int textureIndex = 0;

  
    public float getTextureXOffset() {
        int column = textureIndex % MODEL.getTexture().getNumberOfRows();
        return (float) column / (float) MODEL.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = textureIndex / MODEL.getTexture().getNumberOfRows();
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

    public void moveToPlayer(Terrain terrain, Player player) {
               
        this.CURRENT_SPEED = 5;
        this.TURNING_SPEED = 1;
        
        // setSpeed();

        //super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        //super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = CURRENT_SPEED * DisplayManager.getFrameTimeSeconds();
        //float zDistance = currentTurnSpeed * DisplayManager.getFrameTimeSeconds();
        
        //float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        //float dz = (float) (zDistance * Math.sin(Math.toRadians(super.getRotY())));
        //float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        //if (dx != 0 || dz != 0)
        //System.out.println("dx " + dx + " dz " + dz);
        //System.out.println("ENEMY; x " + position.x + " y " + position.y + " z " + position.z);
        
        float player_x = player.getPosition().x;
        float player_z = player.getPosition().z;
        
        float alien_x = POSITION.x;
        float alien_z = POSITION.z;
        
        if (Math.abs(alien_x - player_x) < 1 && Math.abs(alien_z - player_z) < 1) {
            player.takeWound();
        }
        
        if (alien_x < player_x) {
            if (alien_z < player_z) {
                increasePosition(distance, 0, distance);
            }
            else {
                increasePosition(distance, 0, -distance);
            }
        }
        else {
            if (alien_z < player_z) {
                increasePosition(-distance, 0, distance);
            }
            else {
                increasePosition(-distance, 0, -distance);
            }
        }

        //float x = super.getPosition().x;
        //float y = super.getPosition().y;
        //float z = super.getPosition().z;
        //System.out.println(getPosition());

        //super.increasePosition(dx, 0, dz);
        MOVING_SPEED += GRAVITY_EFFECT * DisplayManager.getFrameTimeSeconds();
        increasePosition(0, MOVING_SPEED * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if (getPosition().y < terrainHeight) {
            MOVING_SPEED = 0;
            getPosition().y = terrainHeight;
        }
    }

    public int getHP() {
        return this.HP;
    }

    public void setHP(int damage) {
        this.HP -= damage ;
    }
    
    private void setSpeed() {
        float rotY = getRotY();
        
        while (rotY > 360) {
            rotY -= 360;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            CURRENT_SPEED = (float) (RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            TURNING_SPEED = (float) (RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            CURRENT_SPEED = -(float) (RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            TURNING_SPEED = -(float) (RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            TURNING_SPEED = (float) -(RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            CURRENT_SPEED = (float) (RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            TURNING_SPEED = (float) (RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            CURRENT_SPEED = (float) -(RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else {
            this.TURNING_SPEED = 0;
        }
//
//        //System.out.println("x " + currentSpeed + " z " +  currentTurnSpeed + " rotY " + rotY);
//        if (Mouse.isButtonDown(0)) {
//            if (!attacked) {
//                attacked = true;
//                attack();
//            }
//        }
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//            jump();
//
//        }

    }

    private void die() {
        
    }

}

