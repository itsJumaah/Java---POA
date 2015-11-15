package buzzdev.shaders;

import org.lwjgl.util.vector.Matrix4f;

import buzzdev.entities.Camera;
import buzzdev.entities.Light;
import buzzdev.toolbox.Maths;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/buzzdev/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/buzzdev/shaders/fragmentShader.txt";
	
	private int locationTransMatrix;
	private int locationProjMatrix;
	private int locationViewMatrix;
	private int locationLightPos;
	private int locationLightColor;
	private int locationShineDamper;
	private int locationReflectivity;
	
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
