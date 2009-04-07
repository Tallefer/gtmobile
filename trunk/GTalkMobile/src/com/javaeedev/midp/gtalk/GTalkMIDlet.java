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

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

/**
 * Main MIDlet.
 * 
 * @author xuefeng
 *
 */
public class GTalkMIDlet extends MIDlet {

    public static GTalkMIDlet instance = null;

    private Display display = null;
    private Displayable current = null;

    public GTalkMIDlet() {
        instance = this;
    }

    protected void destroyApp(boolean unconditional) {
    }

    protected void pauseApp() {
    }

    protected void startApp() {
        if(display==null) {
            display = Display.getDisplay(this);
            current = new SplashCanvas();
        }
        display.setCurrent(current);
    }

    public static void quitApp() {
        instance.destroyApp(true);
        instance.notifyDestroyed();
    }

    /**
     * Convenient method to switch ui.
     */
    public static void switchUI(Displayable d) {
        instance.current = d;
        instance.display.setCurrent(d);
    }

    /**
     * Convenient method to get a Display instance.
     */
    public static Display getDisplay() {
        return instance.display;
    }

}
