package buzzdev.render;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import buzzdev.entities.Entity;
import buzzdev.models.RawModel;
import buzzdev.models.TexturedModel;
import buzzdev.shaders.StaticShader;
import buzzdev.texture.ModelTexture;
import buzzdev.toolbox.Maths;

public class EntityRender {

	
	private StaticShader shader;
	
	public EntityRender(StaticShader shader, Matrix4f projMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjMatrix(projMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for(TexturedModel model : entities.keySet()) {
			prepareTModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, 
						model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTModel();
		}
	}
	
	private void prepareTModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		ModelTexture texture = model.getTexture();
		
		shader.loadRowNum(texture.getRowNum());
		
		if(texture.isHasTransparency()) {
			MasterRender.disableCulling();
		}
		shader.loadFakeLighting(texture.isFakeLighting());
		
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}
	
	private void unbindTModel() {
		MasterRender.enableCulling();
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareInstance(Entity entity) {
		Matrix4f transMatix = Maths.createTransMatrix(entity.getPosition(), entity.getRotX(), 
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		
		shader.loadTransMatrix(transMatix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	}
}
