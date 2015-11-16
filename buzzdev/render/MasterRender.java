package buzzdev.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import buzzdev.entities.Camera;
import buzzdev.entities.Entity;
import buzzdev.entities.Light;
import buzzdev.models.TexturedModel;
import buzzdev.shaders.StaticShader;
import buzzdev.shaders.TerrainShader;
import buzzdev.terrain.Terrain;

public class MasterRender {
	

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projMatrix;

	private StaticShader shader = new StaticShader();
	private TerrainShader terrainShader = new TerrainShader();
	private EntityRender render;
	private TerrainRender terrainRender;
	
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRender() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		createProjMatrix();
		render = new EntityRender(shader, projMatrix);
		terrainRender = new TerrainRender(terrainShader, projMatrix);
		
	}
	
	
	public void render(Light sun, Camera camera) {
		prepare();
		shader.start();
		shader.loadLight(sun);
		shader.loadViewMatrix(camera);
		render.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadLight(sun);
		terrainShader.loadViewMatrix(camera);
		terrainRender.render(terrains);
		terrainShader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void procTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void procEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		
		if(batch != null) {
			batch.add(entity);
		}
		else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 206, 250, 1); //Background color R G B
		
	}
	
	private void createProjMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        
        projMatrix = new Matrix4f();
        projMatrix.m00 = x_scale;
        projMatrix.m11 = y_scale;
        projMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projMatrix.m23 = -1;
        projMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projMatrix.m33 = 0;
	}
	
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
}
