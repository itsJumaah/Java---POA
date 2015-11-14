package buzzdev.shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.*;

public abstract class ShaderProgram {

	private int progID;
	private int vertShaderID;
	private int fragShaderID;
	
	public ShaderProgram(String vertexFile, String fragFile){
		vertShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragShaderID = loadShader(fragFile, GL20.GL_FRAGMENT_SHADER);
		progID = GL20.glCreateProgram();
		GL20.glAttachShader(progID, vertShaderID);
		GL20.glAttachShader(progID, fragShaderID);
		bindAttributes();
		GL20.glLinkProgram(progID);
		GL20.glValidateProgram(progID);
	}
	
	public void start() {
		GL20.glUseProgram(progID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(progID, vertShaderID);
		GL20.glDetachShader(progID, fragShaderID);
		GL20.glDeleteShader(vertShaderID);
		GL20.glDeleteShader(fragShaderID);
		GL20.glDeleteProgram(progID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName) {
		GL20.glBindAttribLocation(progID, attribute, variableName);
	}
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}
		catch(IOException e){
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID,GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.out.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}
	
}
