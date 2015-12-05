package buzzdev.render;


import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int WIDTH		= 1024;
	private static final int HEIGHT		= 640;
	private static final int FPS_CAP	= 30;
	private static final String TITLE	= "The Prince of Arabia";
	
	private static long lastFrameTime;
	private static float delta;
	
	private static int fps = 0;
	private static long lastFPS;
	
	
	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		//openGL ver 3.2 and its forward compatible
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(TITLE);
			Display.setVSyncEnabled(true);
			
			
		} 
		catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT); //tell openGL where to render
		
		lastFrameTime = getTime();
		lastFPS = getTime();
		
		
	}
	
	public static void updateDisplay() {
		
		Display.sync(FPS_CAP);
		Display.update();
		long curFrameTime = getTime();
		delta = (curFrameTime - lastFrameTime) / 1000f;
		lastFrameTime = curFrameTime;
		//-------------------------------------
		updateFPS();
	}
	
	//=======
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
            Display.setTitle(TITLE + " | FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
	}
	//=======
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
		
	}
	
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution(); //current time in ticks  / res (*1000 to make it in millisec)
	}
}
