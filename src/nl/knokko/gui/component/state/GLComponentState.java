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
package nl.knokko.gui.component.state;

import nl.knokko.gui.window.GLGuiWindow;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GLComponentState implements GuiComponentState {
	
	private final GLGuiWindow window;
	
	public GLComponentState(GLGuiWindow window){
		this.window = window;
	}

	@Override
	public boolean isMouseOver() {
		return Mouse.isInsideWindow();
	}

	@Override
	public float getMouseX() {
		if(!Mouse.isInsideWindow())
			return Float.NaN;
		return (float) Mouse.getX() / Display.getWidth();
	}

	@Override
	public float getMouseY() {
		if(!Mouse.isInsideWindow())
			return Float.NaN;
		return (float) Mouse.getY() / Display.getHeight();
	}

	@Override
	public GLGuiWindow getWindow(){
		return window;
	}
}