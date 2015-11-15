package buzzdev;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import buzzdev.entities.Camera;
import buzzdev.entities.Entity;
import buzzdev.entities.Light;
import buzzdev.models.RawModel;
import buzzdev.models.TexturedModel;
import buzzdev.render.DisplayManager;
import buzzdev.render.Loader;
import buzzdev.render.OBJLoader;
import buzzdev.render.Render;
import buzzdev.shaders.StaticShader;
import buzzdev.texture.ModelTexture;


public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		StaticShader shader = new StaticShader();
		Render render = new Render(shader);
		
		RawModel rawModel = OBJLoader.loadObjModel("dragon", loader);
		TexturedModel texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("dragonT")));
		
		ModelTexture texture = texturedModel.getTexture();
		texture.setShineDamper(20);
		texture.setReflectivity(10);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -50), 0, 0, 0, 1);
		
		Light light = new Light(new Vector3f(0,0,0), new Vector3f(1,1,0)); //Light pos xyz and color rgb
		
		Camera camera = new Camera();
		
		
		
		while(!Display.isCloseRequested()) {
			entity.increaseRotation(0, 1, 0);
			camera.move();
			render.prepare();
			shader.start();
			shader.loadLight(light);
			shader.loadViewMatrix(camera);
			
			render.render(entity, shader);
			shader.stop();
			DisplayManager.updateDisplay();
		}
		
		shader.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
