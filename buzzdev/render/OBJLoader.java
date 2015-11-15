package buzzdev.render;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import buzzdev.models.RawModel;

public class OBJLoader {
	
	public static RawModel loadObjModel(String fileName, Loader loader) {
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj"));
		} 
		catch (FileNotFoundException e) {
			System.err.println("**ERORR** Couldn't load OBJ file");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr);
		
		String line;
		
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verArray = null;
		float[] norArray = null;
		float[] texArray = null;
		int[] indArray = null;
		
		try {
			while(true) {
				line = reader.readLine();
				String[] curLine = line.split(" ");
				if(line.startsWith("v ")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(curLine[1]), 
							Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3]));
					vertices.add(vertex);
				}
				else if(line.startsWith("vt ")) {
					Vector2f texture = new Vector2f(Float.parseFloat(curLine[1]), 
							Float.parseFloat(curLine[2]));
					textures.add(texture);
				}
				else if(line.startsWith("vn ")) {
					Vector3f normal = new Vector3f(Float.parseFloat(curLine[1]), 
							Float.parseFloat(curLine[2]), Float.parseFloat(curLine[3]));
					normals.add(normal);
				}
				else if(line.startsWith("f ")) {
					texArray = new float[vertices.size()*2];
					norArray = new float[vertices.size()*3];
					break;
				}
			}
			
			while(line!=null) {
				if(!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				
				String[] curLine = line.split(" ");
				String[] vert1 = curLine[1].split("/");
				String[] vert2 = curLine[2].split("/");
				String[] vert3 = curLine[3].split("/");
				
				procVertex(vert1, indices, textures, normals, texArray, norArray);
				procVertex(vert2, indices, textures, normals, texArray, norArray);
				procVertex(vert3, indices, textures, normals, texArray, norArray);
				
				line = reader.readLine();
			}
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		verArray = new float[vertices.size() * 3];
		indArray = new int[indices.size()];
		
		int verPointer = 0;
		for(Vector3f vertex:vertices) {
			verArray[verPointer++] = vertex.x;
			verArray[verPointer++] = vertex.y;
			verArray[verPointer++] = vertex.z;
		}
		
		for(int i=0; i<indices.size(); i++) {
			indArray[i] = indices.get(i);
		}
		
		return loader.loadToVAO(verArray, texArray, norArray, indArray);
		
	}
	
	private static void procVertex(String[] vertData, List<Integer> indices, 
			List<Vector2f> textures, List<Vector3f> normals,
			float[] texArray, float[] norArray){
		
		int curVecPointer = Integer.parseInt(vertData[0]) -1;
		indices.add(curVecPointer);
		
		Vector2f curTex = textures.get(Integer.parseInt(vertData[1]) -1);
		texArray[curVecPointer*2] = curTex.x;
		texArray[curVecPointer*2 +1] = 1 - curTex.y;
		
		Vector3f curNor = normals.get(Integer.parseInt(vertData[2]) -1);
		norArray[curVecPointer*3   ] = curNor.x;
		norArray[curVecPointer*3 +1] = curNor.y;
		norArray[curVecPointer*3 +2] = curNor.z;
	}
}
