package buzzdev.render;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import buzzdev.entities.Entity;
import buzzdev.models.RawModel;
import buzzdev.models.TexturedModel;
import buzzdev.shaders.StaticShader;
import buzzdev.toolbox.Maths;

public class Render {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projMatrix;
	
	public Render(StaticShader shader) {
		createProjMatrix();
		shader.start();
		shader.loadProjMatrix(projMatrix);
		shader.stop();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1); //Background color R G B
		
	}
	
	public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel();
		
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		Matrix4f transMatix = Maths.createTransMatrix(entity.getPosition(), entity.getRotX(), 
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		
		shader.loadTransMatrix(transMatix);
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
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
}
