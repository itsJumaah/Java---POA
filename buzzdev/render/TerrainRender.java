package buzzdev.render;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import buzzdev.models.RawModel;
import buzzdev.shaders.TerrainShader;
import buzzdev.terrain.Terrain;
import buzzdev.texture.TerrainTexturePack;
import buzzdev.toolbox.Maths;

public class TerrainRender {

	
	private TerrainShader shader;
	
	public TerrainRender(TerrainShader shader, Matrix4f projMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjMatrix(projMatrix);
		shader.connectTextureUnits();
		shader.stop();
		
	}
	
	
	public void render(List<Terrain> terrains) {
		for(Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, 
					terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			unbindTModel();
		}
	}
	
	
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		bindTextures(terrain);
		shader.loadShineVariables(1, 0);
		
	}
	
	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getFinalTexture().getID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getrTexture().getID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getgTexture().getID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getbTexture().getID());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getID());
		
	}
	
	private void unbindTModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transMatix = Maths.createTransMatrix(new Vector3f(terrain.getX(), -10, terrain.getZ()), 0, 
				0, 0, 1);
		
		shader.loadTransMatrix(transMatix);
	}
}
