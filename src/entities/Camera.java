package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private float distanceFromPlayer = -10;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;
    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAngleAroundPlayer();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        //System.out.println("horizontal: "+horizontalDistance+"\tvertical: "+verticalDistance);
        calculateCameraPosition(horizontalDistance, verticalDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
        yaw %= 360;

    }

    public void invertPitch() {
        this.pitch = -pitch;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPosition(float horizDistance, float verticDistance) {
        float theta = player.getRotY() + angleAroundPlayer;
        float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + verticDistance + 12;
        //System.out.println("x: " + position.x + " y: " + position.y + " z: " + position.z);
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch + 4)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch + 4)));
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.03f;
        distanceFromPlayer -= zoomLevel;
        if (distanceFromPlayer < -10) {
            distanceFromPlayer = -10;
        }
        else if (distanceFromPlayer > 40)  {
            distanceFromPlayer = 40;
        }
    }

    private void calculatePitch() { //góra dół
        //if(Mouse.isButtonDown(1)){
        float pitchChange = Mouse.getDY() * 0.2f;
        pitch -= pitchChange;
        if (pitch < -15) {
            pitch = -15;
        } else if (pitch > 45) {
            pitch = 45;
        }

        //}
    }

    private void calculateAngleAroundPlayer() { //lewo prawo
        //if(Mouse.isButtonDown(0)){
        float angleChange = Mouse.getDX() * 0.3f;
			//angleAroundPlayer -= angleChange;
        //player.setRotY(angleAroundPlayer);
        player.increaseRotation(0, -angleChange, 0);
                        //System.out.println("angle: " + angleChange + "rotY: " + player.getRotY());

        //}
    }

}
