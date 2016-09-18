package entities;

import java.util.Random;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;

public class Enemy {

    private TexturedModel model;
    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    private static final float RUN_SPEED = 20;
    //private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private int ID;
    private int HP;
    private boolean attacked;

    private boolean isInAir = false;

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
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = textureIndex / model.getTexture().getNumberOfRows();
        return (float) row / (float) model.getTexture().getNumberOfRows();
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }
    
    public  int getID() {
        return ID;
    }
    
    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Enemy(int id, TexturedModel model, Vector3f position, float rotY, float scale, int hp) {
        this.model = model;
        this.position = position;
        this.rotX = 1;
        this.rotY = rotY;
        this.rotZ = 1;
        this.scale = scale;        
        this.ID = id;
        this.HP = hp;
        this.attacked = false;
    }

    public void moveToPlayer(Terrain terrain, Player player) {
               
        this.currentSpeed = 5;
        this.currentTurnSpeed = 1;
        
        // setSpeed();

        //super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        //super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        //float zDistance = currentTurnSpeed * DisplayManager.getFrameTimeSeconds();
        
        //float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        //float dz = (float) (zDistance * Math.sin(Math.toRadians(super.getRotY())));
        //float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        //if (dx != 0 || dz != 0)
        //System.out.println("dx " + dx + " dz " + dz);
        System.out.println("ENEMY; x " + position.x + " y " + position.y + " z " + position.z);
        
        float player_x = player.getPosition().x;
        float player_z = player.getPosition().z;
        
        float alien_x = position.x;
        float alien_z = position.z;
        
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
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        increasePosition(0, upwardsSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if (getPosition().y < terrainHeight) {
            upwardsSpeed = 0;
            isInAir = false;
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
            currentSpeed = (float) (RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            currentTurnSpeed = (float) (RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            currentSpeed = -(float) (RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            currentTurnSpeed = -(float) (RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            currentTurnSpeed = (float) -(RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            currentSpeed = (float) (RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            currentTurnSpeed = (float) (RUN_SPEED * Math.sin(Math.toRadians(rotY)));
            currentSpeed = (float) -(RUN_SPEED * Math.cos(Math.toRadians(rotY)));
        } else {
            this.currentTurnSpeed = 0;
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

