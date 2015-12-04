package buzzdev.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import buzzdev.entities.Camera;
import buzzdev.entities.Light;
import buzzdev.toolbox.Maths;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/buzzdev/shaders/vertexShader.buzz";
	private static final String FRAGMENT_FILE = "src/buzzdev/shaders/fragmentShader.buzz";
	
	private int locationTransMatrix;
	private int locationProjMatrix;
	private int locationViewMatrix;
	private int locationLightPos;
	private int locationLightColor;
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationFakeLighting;
	private int locationSkyColor;
	private int locationRowNum;
	private int locationOffset;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoord");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransMatrix  = super.getUniformLocation("transMatrix");
		locationProjMatrix 	 = super.getUniformLocation("projMatrix");
		locationViewMatrix 	 = super.getUniformLocation("viewMatrix");
		locationLightPos 	 = super.getUniformLocation("lightPos");
		locationLightColor 	 = super.getUniformLocation("lightColor");
		locationShineDamper  = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationFakeLighting = super.getUniformLocation("fakeLighting");
		locationSkyColor     = super.getUniformLocation("skyColor");
		locationRowNum	     = super.getUniformLocation("rowNum");
		locationOffset	     = super.getUniformLocation("offset");
	}
	
	public void loadRowNum(int rowNum) {
		super.loadFloat(locationRowNum, rowNum);
	}
	
	public void loadOffset(float x, float y) {
		super.loadVector(locationOffset, new Vector2f(x,y));
	}
	
	public void loadSkyColor(float r, float g, float b){
		super.loadVector(locationSkyColor, new Vector3f(r,g,b));
	}
	
	public void loadFakeLighting(boolean useFake) {
		super.loadBoolean(locationFakeLighting, useFake);
	}
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}
	
	public void loadLight(Light light) {
		super.loadVector(locationLightPos, light.getPos());
		super.loadVector(locationLightColor, light.getColor());
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
	
	public void loadTransMatrix(Matrix4f matrix) {
		super.loadMatrix(locationTransMatrix, matrix);
	}
	
	public void loadProjMatrix(Matrix4f proj) {
		super.loadMatrix(locationProjMatrix, proj);
	}

}
