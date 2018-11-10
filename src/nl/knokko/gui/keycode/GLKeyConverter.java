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
package nl.knokko.gui.keycode;

import org.lwjgl.input.Keyboard;

import static nl.knokko.gui.keycode.KeyCode.*;

public class GLKeyConverter {
	
	private static int[][] CONVERT_MAP;
	
	static {
		CONVERT_MAP = new int[222][];
		put(Keyboard.KEY_NONE, UNDEFINED);
		
		put(Keyboard.KEY_0, KEY_0_BASE, KEY_0);
		put(Keyboard.KEY_1, KEY_1_BASE, KEY_1);
		put(Keyboard.KEY_2, KEY_2_BASE, KEY_2);
		put(Keyboard.KEY_3, KEY_3_BASE, KEY_3);
		put(Keyboard.KEY_4, KEY_4_BASE, KEY_4);
		put(Keyboard.KEY_5, KEY_5_BASE, KEY_5);
		put(Keyboard.KEY_6, KEY_6_BASE, KEY_6);
		put(Keyboard.KEY_7, KEY_7_BASE, KEY_7);
		put(Keyboard.KEY_8, KEY_8_BASE, KEY_8);
		put(Keyboard.KEY_9, KEY_9_BASE, KEY_9);
		
		put(Keyboard.KEY_A, KEY_A);
		put(Keyboard.KEY_B, KEY_B);
		put(Keyboard.KEY_C, KEY_C);
		put(Keyboard.KEY_D, KEY_D);
		put(Keyboard.KEY_E, KEY_E);
		put(Keyboard.KEY_F, KEY_F);
		put(Keyboard.KEY_G, KEY_G);
		put(Keyboard.KEY_H, KEY_H);
		put(Keyboard.KEY_I, KEY_I);
		put(Keyboard.KEY_J, KEY_J);
		put(Keyboard.KEY_K, KEY_K);
		put(Keyboard.KEY_L, KEY_L);
		put(Keyboard.KEY_M, KEY_M);
		put(Keyboard.KEY_N, KEY_N);
		put(Keyboard.KEY_O, KEY_O);
		put(Keyboard.KEY_P, KEY_P);
		put(Keyboard.KEY_Q, KEY_Q);
		put(Keyboard.KEY_R, KEY_R);
		put(Keyboard.KEY_S, KEY_S);
		put(Keyboard.KEY_T, KEY_T);
		put(Keyboard.KEY_U, KEY_U);
		put(Keyboard.KEY_V, KEY_V);
		put(Keyboard.KEY_W, KEY_W);
		put(Keyboard.KEY_X, KEY_X);
		put(Keyboard.KEY_Y, KEY_Y);
		put(Keyboard.KEY_Z, KEY_Z);
		
		put(Keyboard.KEY_ESCAPE, KEY_ESCAPE);
		put(Keyboard.KEY_GRAVE, KEY_GRAVE);
		put(Keyboard.KEY_CAPITAL, KEY_CAPSLOCK);
		put(Keyboard.KEY_LSHIFT, KEY_SHIFT);
		put(Keyboard.KEY_RSHIFT, KEY_SHIFT);
		put(Keyboard.KEY_LCONTROL, KEY_CONTROL);
		put(Keyboard.KEY_RCONTROL, KEY_CONTROL);
		put(Keyboard.KEY_LMENU, KEY_ALT);
		put(Keyboard.KEY_RMENU, KEY_ALT);
		put(Keyboard.KEY_SPACE, KEY_SPACE);
		put(Keyboard.KEY_APPS, KEY_APPS);
		
		put(Keyboard.KEY_F1, KEY_F1);
		put(Keyboard.KEY_F2, KEY_F2);
		put(Keyboard.KEY_F3, KEY_F3);
		put(Keyboard.KEY_F4, KEY_F4);
		put(Keyboard.KEY_F5, KEY_F5);
		put(Keyboard.KEY_F6, KEY_F6);
		put(Keyboard.KEY_F7, KEY_F7);
		put(Keyboard.KEY_F8, KEY_F8);
		put(Keyboard.KEY_F9, KEY_F9);
		put(Keyboard.KEY_F10, KEY_F10);
		put(Keyboard.KEY_F11, KEY_F11);
		put(Keyboard.KEY_F12, KEY_F12);
		
		put(Keyboard.KEY_PAUSE, KEY_PAUSE);
		put(Keyboard.KEY_INSERT, KEY_INSERT);
		put(Keyboard.KEY_DELETE, KEY_DELETE);
		put(Keyboard.KEY_MINUS, KEY_MINUS_BASE);
		put(Keyboard.KEY_EQUALS, KEY_EQUALS);
		put(Keyboard.KEY_NUMPADEQUALS, KEY_EQUALS);
		put(Keyboard.KEY_BACK, KEY_BACKSPACE);
		put(Keyboard.KEY_NUMLOCK, KEY_NUMLOCK);
		
		put(Keyboard.KEY_DIVIDE, KEY_DIVIDE_NUMPAD);
		put(Keyboard.KEY_MULTIPLY, KEY_MULTIPLY_NUMPAD);
		put(Keyboard.KEY_SUBTRACT, KEY_MINUS_NUMPAD);
		put(Keyboard.KEY_ADD, KEY_PLUS_NUMPAD);
		
		put(Keyboard.KEY_LBRACKET, KEY_OPENBRACKET);
		put(Keyboard.KEY_RBRACKET, KEY_CLOSEBRACKET);
		put(Keyboard.KEY_BACKSLASH, KEY_BACKSLASH);
		put(Keyboard.KEY_SEMICOLON, KEY_SEMICOLON);
		put(Keyboard.KEY_APOSTROPHE, KEY_QUOTE);
		put(Keyboard.KEY_RETURN, KEY_ENTER);
		put(Keyboard.KEY_NUMPADENTER, KEY_ENTER);
		put(Keyboard.KEY_COMMA, KEY_COMMA);
		put(Keyboard.KEY_NUMPADCOMMA, KEY_COMMA);
		put(Keyboard.KEY_PERIOD, KEY_PERIOD);
		put(Keyboard.KEY_SLASH, KEY_SLASH);
		
		put(Keyboard.KEY_DECIMAL, KEY_DECIMAL);
		put(Keyboard.KEY_NUMPAD0, KEY_0_NUMPAD, KEY_0);
		put(Keyboard.KEY_NUMPAD1, KEY_1_NUMPAD, KEY_1);
		put(Keyboard.KEY_NUMPAD2, KEY_2_NUMPAD, KEY_2);
		put(Keyboard.KEY_NUMPAD3, KEY_3_NUMPAD, KEY_3);
		put(Keyboard.KEY_NUMPAD4, KEY_4_NUMPAD, KEY_4);
		put(Keyboard.KEY_NUMPAD5, KEY_5_NUMPAD, KEY_5);
		put(Keyboard.KEY_NUMPAD6, KEY_6_NUMPAD, KEY_6);
		put(Keyboard.KEY_NUMPAD7, KEY_7_NUMPAD, KEY_7);
		put(Keyboard.KEY_NUMPAD8, KEY_8_NUMPAD, KEY_8);
		put(Keyboard.KEY_NUMPAD9, KEY_9_NUMPAD, KEY_9);
		
		put(Keyboard.KEY_LEFT, KEY_LEFT);
		put(Keyboard.KEY_UP, KEY_UP);
		put(Keyboard.KEY_RIGHT, KEY_RIGHT);
		put(Keyboard.KEY_DOWN, KEY_DOWN);
	}
	
	private static void put(int glKeyCode, int... guiKeyCodes){
		CONVERT_MAP[glKeyCode] = guiKeyCodes;
	}
	
	public static int[] get(int glKeyCode){
		int[] original = getDirect(glKeyCode);
		if(original == null || original[0] == UNDEFINED)
			return null;
		int[] copy = new int[original.length];
		for(int i = 0; i < original.length; i++)
			copy[i] = original[i];
		return copy;
	}
	
	public static int[] getDirect(int glKeyCode){
		return CONVERT_MAP[glKeyCode];
	}
}