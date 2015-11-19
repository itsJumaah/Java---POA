package buzzdev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import buzzdev.entities.Camera;
import buzzdev.entities.Entity;
import buzzdev.entities.Light;
import buzzdev.models.RawModel;
import buzzdev.models.TexturedModel;
import buzzdev.obj.ModelData;
import buzzdev.obj.OBJFileLoader;
import buzzdev.render.DisplayManager;
import buzzdev.render.Loader;
import buzzdev.render.MasterRender;
import buzzdev.terrain.Terrain;
import buzzdev.texture.ModelTexture;


public class PoA {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		ModelData data = OBJFileLoader.loadOBJ("palm");
		
		RawModel rawModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
		
		TexturedModel palm = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("palm")));
		palm.getTexture().setHasTransparency(true);
		palm.getTexture().setFakeLighting(true);
		
		ModelTexture texture = palm.getTexture();
		texture.setShineDamper(20);
		texture.setReflectivity(10);
		
		//Entity entity = new Entity(texturedModel, new Vector3f(0, -10, -250), 0, 0, 0, 1);
		
		Light light = new Light(new Vector3f(30,20,20), new Vector3f(1,1,1)); //Light pos xyz and color rgb
		
		Camera camera = new Camera();
		
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass")));
		
		MasterRender render = new MasterRender();
		
		List<Entity> trees = new ArrayList<Entity>();
		Random rand = new Random();
		for(int i=0; i<50; i++) {
			float x = rand.nextFloat() * 1000;
			float y = -10;
			float z = rand.nextFloat() * -1000;
			trees.add(new Entity(palm, new Vector3f(x, y, z), 0, 0, 0, 1));
		}
		
		while(!Display.isCloseRequested()) {
			camera.move();
			//entity.increaseRotation(0, 1, 0);
			
			for(Entity tree: trees) {
				render.procEntity(tree);
			}
			
			
			render.procTerrain(terrain);
			render.procTerrain(terrain2);
			render.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		render.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}