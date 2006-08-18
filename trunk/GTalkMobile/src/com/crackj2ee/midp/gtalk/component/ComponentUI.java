/**
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crackj2ee.midp.gtalk.component;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * Super class for all low-level components paint on Canvas.
 * 
 * @author xuefeng
 */
public abstract class ComponentUI {

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final Font font;
    private static int THREE_DOTS_WIDTH = 0;

    private boolean visible = false;

    private int store_x;
    private int store_y;

    /**
     * Create UI component at point (x, y) and specify width and height.
     */
    public ComponentUI(int x, int y, int width, int height, Font font) {
	    this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.font = font;
        if(THREE_DOTS_WIDTH==0)
            THREE_DOTS_WIDTH = font.charWidth('.') * 3;
    }

    public void paint(Graphics g) {
        if(visible) {
            g.translate(x-g.getTranslateX(), y-g.getTranslateY());
            paintInternal(g);
        }
	}

    protected abstract void paintInternal(Graphics g);

    public final boolean getVisible() {
        return visible;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void store(Graphics g) {
        store_x = g.getTranslateX();
        store_y = g.getTranslateY();
    }

    public void restore(Graphics g) {
        g.translate(store_x - g.getTranslateX(), store_y - g.getTranslateY());
    }

    /**
     * Return a suitable String. A suitable string means if the width of the String 
     * exceeded the max, a suitable "xxxx..." String will be returned.
     */
    protected String suitable(String s, int max) {
        int s_width = font.stringWidth(s);
        if(s_width<=max)
            return s;
        String leftPart = left(s, max - THREE_DOTS_WIDTH);
        return leftPart + "...";
    }

    /**
     * Return a left-String. A left-String means if the width of the String "abcdefg" 
     * exceeded the max, a left String "abcd" which is just in width will be returned. 
     * If this String is not exceeded, null will be returned.
     */
    protected String left(String s, int max) {
        int s_width = font.stringWidth(s);
        if(s_width<=max)
            return null;
        char[] cs = s.toCharArray();
        int estimate = cs.length;
        int ax = 0;
        do {
            estimate = estimate / 2;
            ax = font.charsWidth(cs, 0, estimate);
        }
        while(ax>max);
        // now [0-estimate) is less than s_max:
        for(int i=estimate; i<cs.length; i++) {
            ax += font.charWidth(cs[i]);
            if(ax>max) {
                // found!
                return new String(cs, 0, i-1);
            }
        }
        // there must be logic error in this method:
        return "?";
    }

}
