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
package com.crackj2ee.midp.gtalk;

import javax.microedition.lcdui.*;

/**
 * Let user enter some text as new message.
 * 
 * @author xuefeng
 */
public class SendMessageTextBox extends TextBox implements CommandListener {

    private MainCanvas mainCanvas;
    private String name;
    private String email;

    private Command cancel = new Command("Cancel", Command.CANCEL, 1);
    private Command send = new Command("Send", Command.OK, 1);

    public SendMessageTextBox(MainCanvas mainCanvas, String name, String email) {
        super("Send to " + name, "", 1024, TextField.ANY);
        this.mainCanvas = mainCanvas;
        this.name = name;
        this.email = email;
        addCommand(send);
        addCommand(cancel);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if(c==send) {
            // send message:
            String msg = getString();
            if(!msg.equals(""))
                mainCanvas.sendMessage(name, email, msg);
        }
        GTalkMIDlet.switchUI(mainCanvas);
    }
}
