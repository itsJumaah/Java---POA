package buzzdev;

import org.lwjgl.opengl.Display;

<<<<<<< Updated upstream
=======
import buzzdev.entities.Entity;
>>>>>>> Stashed changes
import buzzdev.models.RawModel;
import buzzdev.models.TexturedModel;
import buzzdev.render.DisplayManager;
import buzzdev.render.Loader;
import buzzdev.render.Render;
import buzzdev.shaders.StaticShader;
import buzzdev.texture.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		Render render = new Render();
		StaticShader shader = new StaticShader();
		
		//OpenGL expects vertices to be defined counter clockwise by default
		float[] vertices = {
				-0.5f, 0.5f, 0,
				-0.5f, -0.5f, 0,
				0.5f, -0.5f, 0,
				0.5f, 0.5f, 0f
		};
		  
		int[] indices = {
				0,1,3,
				3,1,2
		};
		
		float[] textureCoord = {
				0,0,
				0,1,
				1,1,
				1,0
		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoord, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("box"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
<<<<<<< Updated upstream
		
		while(!Display.isCloseRequested()) {
=======
		Entity entity = new Entity(texturedModel, null, 0, 0, 0, 0);
		while(!Display.isCloseRequested()) {

			entity.increaseRotation(0, 1, 1);
			camera.move();

>>>>>>> Stashed changes
			render.prepare();
			shader.start();
			render.render(texturedModel);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
