package buzzdev.entities;


import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f pos = new Vector3f(0,10,0);
	private float pitch = 20;
	private float yaw = 0;
	private float roll;
	
	private float distFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Buzz buzz;
	
	//----------------------------------
	public Camera(Buzz buzz) {
		this.buzz = buzz;
	}
	//----------------------------------
	public void move() {
		calZoom();
		calPitch();
		calAngle();
		float horiDist = calHoriDist();
		float vertDist = calVertDist();
		calCamPos(horiDist, vertDist);
		this.yaw = 180 - (buzz.getRotY() + angleAroundPlayer);
	}
	//-----------------------------------
	public Vector3f getPos() {
		return pos;
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
    //------------------------------------
	private void calZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f; //senstiviy
		distFromPlayer += zoomLevel; //change to - if the other way around wanted
	}
	
	private void calPitch() {
		if(Mouse.isButtonDown(1)) { //0 is left mouse, 1 is right mouse
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch += pitchChange; //change to - for the other way around
		}
	}

	private void calAngle() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer += angleChange;
		}
	}
	//------------------------------------
	private float calHoriDist() {
		float dist = (float) (distFromPlayer * Math.cos(Math.toRadians(pitch)));
		return dist;
	}
	
	private float calVertDist() {
		float dist = (float) (distFromPlayer * Math.sin(Math.toRadians(pitch)));
		return dist;
	}
	//------------------------------------
	private void calCamPos(float horiDist, float vertDist) {
		float theta = buzz.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horiDist * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horiDist * Math.cos(Math.toRadians(theta)));
		pos.x = buzz.getPosition().x - offsetX;
		pos.z = buzz.getPosition().z - offsetZ;
		pos.y = buzz.getPosition().y + vertDist;
	}
}
