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

import com.javaeedev.midp.gtalk.MainCanvas;

/**
 * Use this panel to a default implementation of ComponentUI.
 * 
 * @author xuefeng
 */
public class EmptyPanel extends ComponentUI {

    public EmptyPanel(int x, int y, int width, int height, Font font) {
        super(x, y, width, height, font);
    }

    protected void paintInternal(Graphics g) {
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        g.setColor(MainCanvas.COLOR_FOREGROUND);
        g.drawString("(Not impl)", 1, 1, Graphics.LEFT|Graphics.TOP);
    }

}
