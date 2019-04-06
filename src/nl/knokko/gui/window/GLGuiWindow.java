/*******************************************************************************
 * The MIT License
 *
 * Copyright (c) 2018 knokko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package nl.knokko.gui.window;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import nl.knokko.gui.component.GuiComponent;
import nl.knokko.gui.component.state.GLComponentState;
import nl.knokko.gui.component.state.GuiComponentState;
import nl.knokko.gui.keycode.GLKeyConverter;
import nl.knokko.gui.keycode.KeyCode;
import nl.knokko.gui.mousecode.GLMouseConverter;
import nl.knokko.gui.render.GLGuiRenderer;
import nl.knokko.gui.texture.loader.GLGuiTextureLoader;
import nl.knokko.gui.texture.loader.GuiTextureLoader;
import nl.knokko.gui.util.CharBuilder;
import nl.knokko.gui.window.input.CharacterFilter;

public class GLGuiWindow extends GuiWindow {
	
	private static void prepare(){
		File gameFolder = new File(System.getProperty("user.home") + File.separator + ".LWJGL");
		File destFolder = new File(gameFolder.getPath() + File.separator + "natives");
		if(!destFolder.exists()){
			System.out.println("natives folder does not exist; creating a new one");
			destFolder.mkdirs();
			try {
				URL url = GLGuiWindow.class.getClassLoader().getResource("natives");
				URLConnection uc = url.openConnection();
				if(uc instanceof JarURLConnection){//not in development (this requires the natives to be in any source folder)
					JarURLConnection jc = (JarURLConnection) uc;
					JarFile jar = jc.getJarFile();
					Enumeration<JarEntry> entries = jar.entries();
					while(entries.hasMoreElements()){
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if(name.startsWith("natives")){
							File f = new File(gameFolder + File.separator + entry.getName());
							if(entry.isDirectory()){
								f.mkdirs();
							}
							else {
								copy(jar.getInputStream(entry), new FileOutputStream(f));
							}
						}
					}
				}
				else {//in development (this requires the natives to be in the src source folder, edit the third line in this block to the correct source folder)
					System.out.println("Not a jar connection (" + uc + ")");
					File codeSource = new File(GLGuiWindow.class.getProtectionDomain().getCodeSource().getLocation().getFile());
					File natives = new File(codeSource.getParentFile() + File.separator + "src" + File.separator + "natives");
					copyFile(natives, gameFolder);
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("natives folder already exists");
		}
		String platformName = LWJGLUtil.getPlatformName();
		if(platformName.equals(LWJGLUtil.PLATFORM_LINUX_NAME)){
			String osName = System.getProperty("os.name");
			if(osName.startsWith("FreeBSD"))
				platformName = "freebsd";//I can't test this code, so I am not sure this works
			else if(osName.startsWith("SunOS"))
				platformName = "solaris";
		}
		System.setProperty("org.lwjgl.librarypath", destFolder.getAbsolutePath() + "/" + platformName);
	}
	
	private static void copyFile(File source, File dest) throws IOException {
		File[] children = source.listFiles();
		if(children == null){//source is a file
			File newFile = new File(dest + File.separator + source.getName());
			System.out.println(source.getAbsolutePath());
			copy(new FileInputStream(source), new FileOutputStream(newFile));
		}
		else {//source is a directory
			File newFolder = new File(dest + File.separator + source.getName());
			newFolder.mkdirs();
			for(File child : children){
				copyFile(child, newFolder);
			}
		}
	}
	
	private static void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[100000];
		int readBytes = input.read(buffer);
		while(readBytes != -1){
			output.write(buffer, 0, readBytes);
			readBytes = input.read(buffer);
		}
		input.close();
		output.close();
	}
	
	private final GLGuiTextureLoader textureLoader;
	private final GLGuiRenderer guiRenderer;
	private final CharBuilder charBuilder;
	
	private int mouseDX;
	private int mouseDY;
	
	private boolean wasInWindow;
	
	public GLGuiWindow(){
		textureLoader = new GLGuiTextureLoader();
		guiRenderer = new GLGuiRenderer(textureLoader);
		charBuilder = new CharBuilder(textureLoader);
		setRenderContinuously(true);
	}
	
	public GLGuiWindow(GuiComponent mainComponent){
		this();
		this.mainComponent = mainComponent;
		setRenderContinuously(true);
	}

	@Override
	protected void directOpen(String title, int width, int height, boolean border) {
		try {
			prepare();
			ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(true);
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(title);
			Display.setVSyncEnabled(true);
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			guiRenderer.init();
		} catch(LWJGLException ex){
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected void directOpen(String title, boolean border) {
		try {
			prepare();
			ContextAttribs attribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
			if (border) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
			} else {
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
				Display.setFullscreen(true);
			}
			Display.create(new PixelFormat(), attribs);
			Display.setTitle(title);
			Display.setVSyncEnabled(true);
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
			guiRenderer.init();
		} catch(LWJGLException ex){
			throw new RuntimeException(ex);
		}
	}

	@Override
	protected void preUpdate() {
		if (Display.isDirty()) {
			markChange();
		}
		if (wasInWindow != Mouse.isInsideWindow()) {
			markChange();
		}
		mouseDX = 0;
		mouseDY = 0;
		while(Mouse.next()){
			float scroll = Mouse.getEventDWheel() * 0.00025f;
			//it appears that gl scrolling is 40 times more sensitive than awt scrolling
			//I don't really like using magic numbers, but I don't have an alternative
			if(scroll != 0){
				if(listener != null)
					scroll = listener.preScroll(scroll);
				if(scroll != 0){
					mainComponent.scroll(scroll);
					listener.postScroll(scroll);
				}
			}
			if(Mouse.getEventButton() != -1){
				int button = GLMouseConverter.get(Mouse.getEventButton());
				if(Mouse.getEventButtonState())
					input.setMouseDown(button);
				else {// on release
					float x = (float) Mouse.getEventX() / Display.getWidth();
					float y = (float) Mouse.getEventY() / Display.getHeight();
					if(listener == null || !listener.preClick(x, y, button)){
						mainComponent.click(x, y, button);
						if(listener != null)
							listener.postClick(x, y, button);
					}
					input.setMouseUp(button);
				}
			}
			mouseDX += Mouse.getEventDX();
			mouseDY += Mouse.getEventDY();
		}
		if (mouseDX != 0 || mouseDY != 0) {
			markChange();
		}
		while(Keyboard.next()){
			//KeyEvent (awt) and Keyboard (lwjgl) use different key codes...
			int[] codes = GLKeyConverter.getDirect(Keyboard.getEventKey());
			char c = Keyboard.getEventCharacter();
			if(Keyboard.getEventKeyState() && c != Keyboard.CHAR_NONE && CharacterFilter.approve(c) && (listener == null || !listener.preKeyPressed(c))){
				mainComponent.keyPressed(c);
				if(listener != null)
					listener.postKeyPressed(c);
			}
			if(codes != null && codes[0] != KeyCode.UNDEFINED){
				for(int code : codes){
					if(Keyboard.getEventKeyState()){
						input.setKeyDown(code);
						if(listener == null || !listener.preKeyPressed(code)){
							mainComponent.keyPressed(code);
							if(listener != null)
								listener.postKeyPressed(code);
						}
					}
					else {
						if(listener == null || !listener.preKeyReleased(code)){
							mainComponent.keyReleased(code);
							if(listener != null)
								listener.postKeyReleased(code);
						}
						input.setKeyUp(code);
					}
				}
			}
			//when state is false (keyRelease), the character is unknown
		}
		wasInWindow = Mouse.isInsideWindow();
	}
	
	@Override
	protected void postUpdate() {}

	@Override
	protected void directRender() {
		guiRenderer.start();
		mainComponent.render(guiRenderer);
		guiRenderer.stop();
	}

	@Override
	protected void directClose() {
		textureLoader.clean();
		guiRenderer.clean();
		Display.destroy();
	}

	@Override
	public void run(int fps) {
		while(!Display.isCloseRequested() && !shouldStopRunning){
			if(listener == null || !listener.preRunLoop()){
				update();
				render();
				Display.update();
				Display.sync(fps);
				if(listener != null)
					listener.postRunLoop();
			}
		}
		close();
	}

	@Override
	public GuiTextureLoader getTextureLoader() {
		return textureLoader;
	}
	
	@Override
	public GLGuiRenderer getRenderer() {
		return guiRenderer;
	}
	
	@Override
	public CharBuilder getCharBuilder() {
		return charBuilder;
	}

	@Override
	protected GuiComponentState createState() {
		return new GLComponentState(this);
	}

	@Override
	public float getMouseX() {
		if (Mouse.isInsideWindow())
			return (float) Mouse.getX() / Display.getWidth();
		return Float.NaN;
	}

	@Override
	public float getMouseY() {
		if (Mouse.isInsideWindow())
			return (float) Mouse.getY() / Display.getHeight();
		return Float.NaN;
	}
	
	@Override
	public float getMouseDX() {
		return (float) mouseDX / Display.getWidth();
	}
	
	@Override
	public float getMouseDY() {
		return (float) mouseDY / Display.getHeight();
	}

	@Override
	public int getWindowPosX() {
		if (isOpen()) {
			return Display.getX();
		} else {
			return -1;
		}
	}

	@Override
	public int getWidth() {
		// TODO look into insets and fix this later
		if (isOpen()) {
			return Display.getWidth();
		} else {
			return -1;
		}
	}

	@Override
	public int getWindowPosY() {
		if (isOpen()) {
			return Display.getY();
		} else {
			return -1;
		}
	}

	@Override
	public int getHeight() {
		// TODO look into insets and fix this later
		if (isOpen()) {
			return Display.getHeight();
		} else {
			return -1;
		}
	}

	@Override
	public int getPosX() {
		// TODO look into insets and fix this later
		if (isOpen()) {
			return Display.getX();
		} else {
			return -1;
		}
	}

	@Override
	public int getWindowWidth() {
		if (isOpen()) {
			return Display.getWidth();
		} else {
			return -1;
		}
	}

	@Override
	public int getPosY() {
		// TODO look into insets and fix this later
		if (isOpen()) {
			return Display.getY();
		} else {
			return -1;
		}
	}

	@Override
	public int getWindowHeight() {
		if (isOpen()) {
			return Display.getHeight();
		} else {
			return -1;
		}
	}
}