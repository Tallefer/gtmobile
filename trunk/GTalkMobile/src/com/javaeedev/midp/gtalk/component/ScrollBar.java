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
package com.javaeedev.midp.gtalk.component;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.javaeedev.midp.gtalk.MainCanvas;

/**
 * Paint a scroll bar.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class ScrollBar extends ComponentUI implements Scrollable {

    private static final int MIN_BAR_HEIGHT = 10;

    private Image up;
    private Image down;

    private int value;
    private int max;
    private int inner_height;

    public ScrollBar(int x, int y, int height, Font font) {
        super(x, y, 15, height, font);
        try {
            // 11x9:
            up = Image.createImage("/up.png");
            down = Image.createImage("/down.png");
        }
        catch(IOException e) {
            throw new Error("Failed loading resource.");
        }
        inner_height = height - 26;
        //System.out.println(inner_height);
        setScroll(10, 0);
    }

    public void setScroll(int max, int value) {
        if(max<0)
            max = 0;
        this.max = max;
        if(value<0)
            value = 0;
        if(value>=max)
            value = max-1;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        g.setColor(MainCanvas.COLOR_BORDER);
        g.drawRect(0, 0, width-1, height-1);
        g.drawLine(0, 12, width-1, 12);
        g.drawLine(0, height-13, width-1, height-13);
        g.drawImage(up, 2, 2, Graphics.LEFT|Graphics.TOP);
        g.drawImage(down, 2, height-11, Graphics.LEFT|Graphics.TOP);
        // paint bar:
        if(max==0)
            return;
        int bar_height = inner_height / max;
        if(bar_height<MIN_BAR_HEIGHT)
            bar_height = MIN_BAR_HEIGHT;
        // now calculate position:
        int pos = (max==1) ? (inner_height-bar_height) : ((inner_height-bar_height) * value / (max-1));
        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
        g.fillRect(2, pos+14, width-4, bar_height-2);
    }

}
