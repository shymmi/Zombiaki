package entities;

import fontMeshCreator.GUIText;
import java.util.Iterator;
import java.util.List;
import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;
import textures.ModelTexture;

public class Player {

    private TexturedModel TEXTURED_MODEL;
    public Vector3f POSITION;
    private float ROT_X, ROT_Y, ROT_Z;
    private float SCALE;
    private static final float RUN_SPEED = 40;
    public static final float GRAVITY = -50;
    private static final float JUMP_HEIGHT = 18;
    private static final int DAMAGE = 2;
    private float CURRENT_SPEED = 0;
    private float TURNING_SPEED = 0;
    private float MOVING_SPEED = 0;
    private final int ID;
    private int HP;
    private boolean IS_IN_AIR = false;
    private int TEXTURE_INDEX = 0;
    private int ENEMY_DAMAGE = 5;
  
    public float getTextureXOffset() {
        int column = TEXTURE_INDEX % TEXTURED_MODEL.getTexture().getNumberOfRows();
        return (float) column / (float) TEXTURED_MODEL.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = TEXTURE_INDEX / TEXTURED_MODEL.getTexture().getNumberOfRows();
        return (float) row / (float) TEXTURED_MODEL.getTexture().getNumberOfRows();
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

    public TexturedModel getModel() {
        return TEXTURED_MODEL;
    }

    public void setModel(TexturedModel model) {
        this.TEXTURED_MODEL = model;
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

    public Player(int id, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
            float scale, int hp) {
        this.TEXTURED_MODEL = model;
        this.POSITION = position;
        this.ROT_X = rotX;
        this.ROT_Y = rotY;
        this.ROT_Z = rotZ;
        this.SCALE = scale;        
        this.ID = id;
        this.HP = hp;
    }

    public void move(Terrain terrain) {
        this.CURRENT_SPEED = 0;
        this.TURNING_SPEED = 0;
        moveForward();

        float distance = CURRENT_SPEED * DisplayManager.getFrameTimeSeconds();
        float zDistance = TURNING_SPEED * DisplayManager.getFrameTimeSeconds();
 
        increasePosition(distance, 0, zDistance);
        
        if (POSITION.x <= 0 || POSITION.x >= 500 || POSITION.z <= -500 || POSITION.z >= 0) {
            increasePosition(-distance, 0, -zDistance);
        }

        MOVING_SPEED += GRAVITY * DisplayManager.getFrameTimeSeconds();
        increasePosition(0, MOVING_SPEED * DisplayManager.getFrameTimeSeconds(), 0);
        float terrainHeight = terrain.getHeightOfTerrain(getPosition().x, getPosition().z);
        if (getPosition().y < terrainHeight) {
            MOVING_SPEED = 0;
            IS_IN_AIR = false;
            getPosition().y = terrainHeight;
        }
    }

    public int getHP() {
        return this.HP;
    }

    private void jump() {
        if (!IS_IN_AIR) {
            this.MOVING_SPEED = JUMP_HEIGHT;
            IS_IN_AIR = true;
        }
    }

    private void moveForward() {
        float PLAYER_ROT_Y = getRotY();
        while (PLAYER_ROT_Y > 360) {
            PLAYER_ROT_Y -= 360;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            CURRENT_SPEED = (float) (RUN_SPEED * Math.sin(Math.toRadians(PLAYER_ROT_Y)));
            TURNING_SPEED = (float) (RUN_SPEED * Math.cos(Math.toRadians(PLAYER_ROT_Y)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            CURRENT_SPEED = -(float) (RUN_SPEED * Math.sin(Math.toRadians(PLAYER_ROT_Y)));
            TURNING_SPEED = -(float) (RUN_SPEED * Math.cos(Math.toRadians(PLAYER_ROT_Y)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            TURNING_SPEED = (float) -(RUN_SPEED * Math.sin(Math.toRadians(PLAYER_ROT_Y)));
            CURRENT_SPEED = (float) (RUN_SPEED * Math.cos(Math.toRadians(PLAYER_ROT_Y)));
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            TURNING_SPEED = (float) (RUN_SPEED * Math.sin(Math.toRadians(PLAYER_ROT_Y)));
            CURRENT_SPEED = (float) -(RUN_SPEED * Math.cos(Math.toRadians(PLAYER_ROT_Y)));
        } else {
            this.TURNING_SPEED = 0;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            jump();
        }

    }
    
    public void shoot(List<Enemy> aliens) {
        if (Mouse.isButtonDown(0)) {    
            float DESTINATION_Z_POSITION = POSITION.z;
            float DESTINATION_X_POSITION = POSITION.x;        
            boolean isEnemyHitted = false;
            
            for (int j=0; j<400; j++) {
                if (isEnemyHitted) {
                    break;
                }
                 
                DESTINATION_Z_POSITION += (float) (RUN_SPEED * Math.cos(Math.toRadians(ROT_Y))) * DisplayManager.getFrameTimeSeconds();
                DESTINATION_X_POSITION += (float) (RUN_SPEED * Math.sin(Math.toRadians(ROT_Y))) * DisplayManager.getFrameTimeSeconds();
                
                Iterator<Enemy> i = aliens.iterator();
                
                while(i.hasNext()) {
                    Enemy a = i.next();
                    if (Math.abs(a.getPosition().x - DESTINATION_X_POSITION) < 2 && Math.abs(a.getPosition().z - DESTINATION_Z_POSITION) < 2) {
                        if(a.getHP() >= 0) {
                            a.decreaseHP(DAMAGE);
                        } else {
                            i.remove();  
                            isEnemyHitted = true;
                            break;
                        }
                    }
                }                
            }
        }
    }

    public void takeWound() {
        if(this.HP >= ENEMY_DAMAGE) {
            this.HP -= ENEMY_DAMAGE;
        } 
    }

}

