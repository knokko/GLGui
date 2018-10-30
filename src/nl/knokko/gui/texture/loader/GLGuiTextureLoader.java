/* 
 * The MIT License
 *
 * Copyright 2018 20182191.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
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
 */
package nl.knokko.gui.texture.loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import nl.knokko.gui.texture.GLGuiTexture;
import nl.knokko.gui.texture.GLPartGuiTexture;
import nl.knokko.gui.texture.GuiTexture;

public class GLGuiTextureLoader implements GuiTextureLoader {
	
	private final List<Integer> textures;
	
	private PrintStream errorOutput;
	
	private static final int[] POWERS = {
		1,8,16,32,64,128,256,512,1024,2048,4096,8192,16384,32768,65536,131072,262144,524288,1048576
	};
	
	private static boolean isPowerOf2(int size){
		for(int power : POWERS)
			if(power == size)
				return true;
		return false;
	}

	public GLGuiTextureLoader() {
		textures = new ArrayList<Integer>();
		errorOutput = System.out;
	}
	@Override
	public GuiTexture loadTexture(BufferedImage source, int minX, int minY, int maxX, int maxY) {
		boolean allowAlpha = source.getTransparency() != BufferedImage.OPAQUE;
		int effWidth = maxX - minX + 1;
		int effHeight = maxY - minY + 1;
		ByteBuffer buffer = BufferUtils.createByteBuffer(effWidth * effHeight * (allowAlpha ? 4 : 3)); //4 for RGBA, 3 for RGB
	    for(int y = minY; y <= maxY; y++){
	        for(int x = minX; x <= maxX; x++){
	        	int rgb = source.getRGB(x, y);
	        	buffer.put((byte) (rgb >> 16));
	        	buffer.put((byte) (rgb >> 8));
	        	buffer.put((byte) (rgb >> 0));
	        	if(allowAlpha)
	        		buffer.put((byte) (rgb >> 24));
	        }
	    }
	    buffer.flip();
	    int textureID = GL11.glGenTextures();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, allowAlpha ? GL11.GL_RGBA8 : GL11.GL_RGB8, source.getWidth(), source.getHeight(), 0, allowAlpha ? GL11.GL_RGBA : GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
	    textures.add(textureID);
	    return new GLGuiTexture(textureID);
	}

	@Override
	public GuiTexture loadTexture(BufferedImage source) {
		boolean allowAlpha = source.getTransparency() != BufferedImage.OPAQUE;
		ByteBuffer buffer = BufferUtils.createByteBuffer(source.getWidth() * source.getHeight() * (allowAlpha ? 4 : 3)); //4 for RGBA, 3 for RGB
	    for(int y = 0; y < source.getHeight(); y++){
	        for(int x = 0; x < source.getWidth(); x++){
	        	int rgb = source.getRGB(x, y);
	        	buffer.put((byte) (rgb >> 16));
	        	buffer.put((byte) (rgb >> 8));
	        	buffer.put((byte) (rgb >> 0));
	        	if(allowAlpha)
	        		buffer.put((byte) (rgb >> 24));
	        }
	    }
	    buffer.flip();
	    int textureID = GL11.glGenTextures();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, allowAlpha ? GL11.GL_RGBA8 : GL11.GL_RGB8, source.getWidth(), source.getHeight(), 0, allowAlpha ? GL11.GL_RGBA : GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);
	    textures.add(textureID);
	    return new GLGuiTexture(textureID);
	}

	@Override
	public GuiTexture loadTexture(String texturePath, int minX, int minY, int maxX, int maxY) {
		try {
			Texture texture = TextureLoader.getTexture("PNG", GLGuiTextureLoader.class.getClassLoader().getResource(texturePath).openStream());
			int id = texture.getTextureID();
			textures.add(id);
			int width = texture.getImageWidth();
			int height = texture.getImageHeight();
			return new GLPartGuiTexture(id, (float) minX / width, 1f - (float) maxY / height, (float) maxX / width, 1f - (float) minY / height);
		} catch (IOException e) {
			errorOutput.println("Can't load texture '" + texturePath + "': " + e.getMessage());
			e.printStackTrace(errorOutput);
			return null;
		}
	}

	@Override
	public GuiTexture loadTexture(String texturePath) {
		try {
			Texture texture = TextureLoader.getTexture("PNG", GLGuiTextureLoader.class.getClassLoader().getResource(texturePath).openStream());
			int id = texture.getTextureID();
			textures.add(id);
			return new GLGuiTexture(id);
		} catch (IOException e) {
			errorOutput.println("Can't load texture '" + texturePath + "': " + e.getMessage());
			e.printStackTrace(errorOutput);
			return null;
		}
	}

	@Override
	public GuiTextureLoader setErrorOutput(PrintStream output) {
		errorOutput = output;
		return this;
	}
	
	public void clean(){
		for(int texture : textures)
			GL11.glDeleteTextures(texture);
	}
}