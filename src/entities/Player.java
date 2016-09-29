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

    private TexturedModel model;
    public Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;
    private static final float RUN_SPEED = 40;
    //private static final float TURN_SPEED = 160;
    public static final float GRAVITY = -50;
    private static final float JUMP_POWER = 18;
    private static final int DAMAGE = 2;
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;

    private int ID;
    private int HP;
    private boolean attacked;

    private boolean isInAir = false;

    private int textureIndex = 0;

  
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

    public Player(int id, TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ,
            float scale, int hp) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;        
        this.ID = id;
        this.HP = hp;
        this.attacked = false;
    }

    public void move(Terrain terrain) {
        this.currentSpeed = 0;
        this.currentTurnSpeed = 0;
        setSpeed();
        
        //super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        //super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float zDistance = currentTurnSpeed * DisplayManager.getFrameTimeSeconds();
        //float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        //float dz = (float) (zDistance * Math.sin(Math.toRadians(super.getRotY())));
        //float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        //if (dx != 0 || dz != 0)
        //System.out.println("dx " + dx + " dz " + dz);
        System.out.println("PLAYER; x " + position.x + " y " + position.y + " z " + position.z);
 
        increasePosition(distance, 0, zDistance);
        if (position.x <= 0 || position.x >= 500 || position.z <= -500 || position.z >= 0) {
            increasePosition(-distance, 0, -zDistance);
        }
        //float x = super.getPosition().x;
        //float y = super.getPosition().y;
        //float z = super.getPosition().z;
//        System.out.println(getPosition());

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

    private void jump() {
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
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

        //System.out.println("x " + currentSpeed + " z " +  currentTurnSpeed + " rotY " + rotY);
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            jump();

        }

    }
    
    public void kill(List<Enemy> aliens) {
        if (Mouse.isButtonDown(0)) {
            
            float killZ = position.z;
            float killX = position.x;
            
            
            boolean isKilled = false;
            for (int j=0; j<400; j++) {
                if (isKilled) {
                    break;
                }
                
                
                killZ += (float) (RUN_SPEED * Math.cos(Math.toRadians(rotY))) * DisplayManager.getFrameTimeSeconds();
                killX += (float) (RUN_SPEED * Math.sin(Math.toRadians(rotY))) * DisplayManager.getFrameTimeSeconds();
                
                Iterator<Enemy> i = aliens.iterator();
                
                while(i.hasNext()) {
                    //System.out.println("killX: " + killX);
                    //System.out.println("killZ: " + killZ);
                    Enemy a = i.next();
                    if (Math.abs(a.getPosition().x - killX) < 2 && Math.abs(a.getPosition().z - killZ) < 2) {
                        //a.setModel(frozenEnemy);
                        if(a.getHP() >= 0) {
                            a.setHP(DAMAGE);
                        } else {
                            i.remove();  
                            isKilled = true;
                            System.out.println("KILLED: " + a.getID());
                            break;
                        }
                        
                    }
                }                
            }
        }
    }

    public void takeWound() {
        System.out.println("HP: " + this.HP);
        if(this.HP >= 5) {
            this.HP -= 5;
        } 
    }

}

