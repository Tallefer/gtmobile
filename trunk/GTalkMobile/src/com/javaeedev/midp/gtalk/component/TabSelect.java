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

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.javaeedev.midp.gtalk.MainCanvas;

/**
 * Draw a Tab-styled panels.
 * 
 * @author Michael Liao (askxuefeng@gmail.com)
 */
public class TabSelect extends ComponentUI {

    private int selection = 1;
    private Image[] icons = new Image[4];
    private Image[] texts = new Image[4];
    private ComponentUI[] panels;

    public TabSelect(int x, int y, int width, Font font, ComponentUI[] panels) {
        super(x, y, width, 23, font);
        this.panels = panels;
        try {
            for(int i=0; i<4; i++) {
                icons[i] = Image.createImage("/i" + i + ".png");
                texts[i] = Image.createImage("/t" + i + ".png");
                panels[i].setVisible(selection==i);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new Error("Failed loading resource.");
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        panels[selection].paint(g);
    }

    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        int x = 1;
        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
        g.drawLine(0, 21, 1, 21);
        for(int i=0; i<texts.length; i++) {
            if(i==selection) {
                g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
                // draw "-"
                g.drawLine(x+1, 1, x+70, 1);
                g.drawImage(texts[i], x+19, 4, Graphics.LEFT|Graphics.TOP);
                // draw "|"
                g.drawLine(x, 2, x, 21);
                g.drawLine(x+71, 2, x+71, 21);
            }
            else {
                g.setColor(MainCanvas.COLOR_BORDER);
                // draw "|"
                if((i-1)!=selection)
                    g.drawLine(x, 2, x, 20);
                g.drawLine(x+19, 2, x+19, 20);
                // draw "-"
                g.drawLine(x+1, 2, x+19, 2);
                g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
                g.drawLine(x+1, 21, x+19, 21);
            }
            g.drawImage(icons[i], x+2, 4, Graphics.LEFT|Graphics.TOP);
            x += (i==selection ? 71 : 19 );
        }
        // draw rest "-":
        g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BORDER);
        g.drawLine(x, 21, width-1, 21);
    }

    public boolean moveLeft() {
        if(selection>0) {
            panels[selection].setVisible(false);
            selection--;
            panels[selection].setVisible(true);
            return true;
        }
        return false;
    }

    public boolean moveRight() {
        if(selection<3) {
            panels[selection].setVisible(false);
            selection++;
            panels[selection].setVisible(true);
            return true;
        }
        return false;
    }

    public int getSelection() {
        return selection;
    }
}
