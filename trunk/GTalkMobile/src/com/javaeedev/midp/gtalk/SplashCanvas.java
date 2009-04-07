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
package com.javaeedev.midp.gtalk;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Welcome screen that display a logo.
 * 
 * @author xuefeng
 */
public class SplashCanvas extends Canvas implements Runnable {

    private Image logo = null;
    private Thread waitingThread;

    public SplashCanvas() {
        try {
            logo = Image.createImage("/logo.png");
        }
        catch(IOException e) {
            throw new Error("Failed loading resource.");
        }
        setFullScreenMode(true);
        waitingThread = new Thread(this);
        waitingThread.start();
    }

    protected void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        g.setColor(0xffffff);
        g.fillRect(0, 0, w, h);
        g.drawImage(logo, w/2, h/2, Graphics.HCENTER|Graphics.VCENTER);
    }

    public void run() {
        try {
            Thread.sleep(3000);
        }
        catch(InterruptedException e) {}
        GTalkMIDlet.switchUI(new LoginForm());
    }

    protected void keyPressed(int keyCode) {
        waitingThread.interrupt();
    }

    protected void pointerPressed(int x, int y) {
        waitingThread.interrupt();
    }
}
