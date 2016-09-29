package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private float DISTANCE = -10;
    private final float ANGLE = 0;

    private final Vector3f POSITION = new Vector3f(0, 0, 0);
    private float HIGHT = 45;
    private float YAW = 0;
    private final Player PLAYER;

    public Camera(Player player) {
        this.PLAYER = player;
    }

    public void moveCamera() {
        calculateZoom();
        calculatePitch();
        //calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        //System.out.println("horizontal: "+horizontalDistance+"\tvertical: "+verticalDistance);
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.YAW = 180 - (PLAYER.getRotY() + ANGLE);
        YAW %= 360;

    }

    public void invertPitch() {
        this.HIGHT = -HIGHT;
    }

    public Vector3f getPosition() {
        return POSITION;
    }

    public float getPitch() {
        return HIGHT;
    }

    public float getYaw() {
        return YAW;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = PLAYER.getRotY() + ANGLE;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        POSITION.x = PLAYER.getPosition().x - offsetX;
        POSITION.z = PLAYER.getPosition().z - offsetZ;
        POSITION.y = PLAYER.getPosition().y + verticDistance + 12;
    }

    private float calculateHorizontalDistance() {
        return (float) (DISTANCE * Math.cos(Math.toRadians(HIGHT + 4)));
    }

    private float calculateVerticalDistance() {
        return (float) (DISTANCE * Math.sin(Math.toRadians(HIGHT + 4)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.03f;
        DISTANCE -= zoomLevel;
        if (DISTANCE < -10) {
            DISTANCE = -10;
        }
        else if (DISTANCE > 40)  {
            DISTANCE = 40;
        }
    }

    private void calculatePitch() { //góra dół
        //if(Mouse.isButtonDown(1)){
        float pitchChange = Mouse.getDY() * 0.2f;
        HIGHT -= pitchChange;
        if (HIGHT < -15) {
            HIGHT = -15;
        } else if (HIGHT > 45) {
            HIGHT = 45;
        }

        //}
    }

    private void calculateAngleAroundPlayer() { //lewo prawo
        //if(Mouse.isButtonDown(0)){
        float angleChange = Mouse.getDX() * 0.3f;
			//angleAroundPlayer -= angleChange;
        //player.setRotY(angleAroundPlayer);
        PLAYER.increaseRotation(0, -angleChange, 0);
                        //System.out.println("angle: " + angleChange + "rotY: " + player.getRotY());

        //}
    }

}
