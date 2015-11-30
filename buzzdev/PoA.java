package buzzdev;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import buzzdev.entities.Buzz;
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
import buzzdev.texture.TerrainTexture;
import buzzdev.texture.TerrainTexturePack;


public class PoA {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//-------------TERRAIN TEXTURE------------------
		TerrainTexture BGTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(BGTexture, rTexture, gTexture, bTexture);
		//----------------------------------------------
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);
		
		//---------------------------------------------
		
		ModelData palmData = OBJFileLoader.loadOBJ("palm");
		RawModel rawPalm = loader.loadToVAO(palmData.getVertices(), palmData.getTextureCoords(), palmData.getNormals(), palmData.getIndices());
		
		TexturedModel texturedPalm = new TexturedModel(rawPalm, new ModelTexture(loader.loadTexture("palm")));
		
		texturedPalm.getTexture().setHasTransparency(true); //no culling
		texturedPalm.getTexture().setFakeLighting(true); //make all lights even(not shiny)
//		ModelTexture texture = texturedPalm.getTexture();
//		texture.setShineDamper(20); //make it shiny
//		texture.setReflectivity(10); //make it shiny
		
		//----------------------------------------------
		
		List<Entity> trees = new ArrayList<Entity>();
		Random rand = new Random();
		for(int i=0; i<50; i++) {
			float x = rand.nextFloat() * 1000;
			float y = -10;
			float z = rand.nextFloat() * -1000;
			trees.add(new Entity(texturedPalm, new Vector3f(x, y, z), 0, 0, 0, 1));
		}
		
		//----------------------------------------------

		
		Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1)); //Light pos xyz and color rgb
		
		Camera camera = new Camera();
		MasterRender render = new MasterRender();
		
		
		//----------------------------------------------
		
		ModelData dragonData = OBJFileLoader.loadOBJ("dragon");
		RawModel dragonModel = loader.loadToVAO(dragonData.getVertices(), dragonData.getTextureCoords(), dragonData.getNormals(), dragonData.getIndices());
		
		TexturedModel dragon = new TexturedModel(dragonModel, new ModelTexture(loader.loadTexture("mud")));
		
		Buzz buzz = new Buzz(dragon, new Vector3f(1,-10,-10), 0, 0, 0, 1);
		

		
		//----------------------------------------------
		while(!Display.isCloseRequested()) {
			camera.move();
			buzz.move();
			render.procEntity(buzz);
			for(Entity tree: trees) {
				render.procEntity(tree);
			}
			
			render.procTerrain(terrain);
			render.procTerrain(terrain2);
			render.render(light, camera);
			DisplayManager.updateDisplay();
		}
		//-------------------------------------------------
		render.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
