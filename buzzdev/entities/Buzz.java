package buzzdev.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import buzzdev.models.TexturedModel;
import buzzdev.render.DisplayManager;
import buzzdev.terrain.Terrain;

public class Buzz extends Entity{
	
	private static final float RUN_SPEED      = 50;   //units per seconds
	private static final float TURN_SPEED     = 70; //degress per seconds
	private static final float GRAVITY        = -50;
	private static final float JUMP_POWER	  = 30;
	
	private float curSpeed    = 0;
	private float curTurn     = 0;
	private float upwardSpeed = 0;
	
	private boolean midAir = false;

	public Buzz(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		
	}
	//----------------------------------------
	public void move(Terrain terrain) {
		checkInputs();
		
		super.increaseRotation(0, curTurn * DisplayManager.getFrameTimeSeconds(), 0);
		
		float distance = curSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY()))); // x = d . sin ø
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY()))); // z = d . cos ø
		super.increasePosition(dx, 0, dz);
		
		upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		float dy = upwardSpeed * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, dy, 0);
		
		float terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight) {
			upwardSpeed = 0;
			midAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	//----------------------------------------
	private void jump() {
		if(midAir != true) {
			this.upwardSpeed = JUMP_POWER;
			midAir = true;
		}
	}
	private void checkInputs() {
	//-----------FORWARD AND BACKWARD---------
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.curSpeed = RUN_SPEED;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.curSpeed = -RUN_SPEED;
		}
		else { 
			this.curSpeed = 0; 
		}
		
	//------------LEFT AND RIGHT---------------
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.curTurn = TURN_SPEED;
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.curTurn = -TURN_SPEED;
		}
		else {
			this.curTurn = 0;
		}
		
	//------------JUMP ---------------
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	} 
}
