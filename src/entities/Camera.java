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
    private static final int MAX_PITCH = 45;
    private static final int MIN_PITCH = -15;
    private static final int MAX_DISTANCE = 40;
    private static final int MIN_DISTANCE = -10;
    
    public Camera(Player player) {
        this.PLAYER = player;
    }

    public void moveCamera() {
        calculateZoom();
        calculatePitch();
        calculateAngle();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.YAW = (180 - (PLAYER.getRotY() + ANGLE)) % 360;
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
        if (DISTANCE < MIN_DISTANCE) {
            DISTANCE = MIN_DISTANCE;
        }
        else if (DISTANCE > MAX_DISTANCE)  {
            DISTANCE = MAX_DISTANCE;
        }
    }

    private void calculatePitch() { //up and down
        float pitchChange = Mouse.getDY() * 0.2f;
        HIGHT -= pitchChange;
        if (HIGHT < MIN_PITCH) {
            HIGHT = MIN_PITCH;
        } else if (HIGHT > MAX_PITCH) {
            HIGHT = MAX_PITCH;
        }
    }

    private void calculateAngle() { //left and right
        float angleChange = Mouse.getDX() * 0.3f;
        PLAYER.increaseRotation(0, -angleChange, 0);
    }

}
